package br.com.aimcol.fallalertapp.activity;

import android.Manifest;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.common.collect.ImmutableList;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.List;

import br.com.aimcol.fallalertapp.R;
import br.com.aimcol.fallalertapp.model.Caregiver;
import br.com.aimcol.fallalertapp.model.Contact;
import br.com.aimcol.fallalertapp.model.Elderly;
import br.com.aimcol.fallalertapp.model.Person;
import br.com.aimcol.fallalertapp.model.User;
import br.com.aimcol.fallalertapp.service.FallDetectionService;
import br.com.aimcol.fallalertapp.service.UserService;
import br.com.aimcol.fallalertapp.util.BroadcastReceiverUtils;
import br.com.aimcol.fallalertapp.util.CrudAction;
import br.com.aimcol.fallalertapp.util.PermissionUtils;
import br.com.aimcol.fallalertapp.util.RuntimeTypeAdapterFactory;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_SMS = 0;

    private static final ImmutableList<String> PERMISSIONS = new ImmutableList.Builder<String>()
            .add(Manifest.permission.SEND_SMS)
            .add(Manifest.permission.ACCESS_COARSE_LOCATION)
            .add(Manifest.permission.ACCESS_FINE_LOCATION)
//            .add(Settings.ACTION_MANAGE_OVERLAY_PERMISSION)
            .build();

    private BroadcastReceiver mBroadcastReceiver;
    private User user;
    private Gson gson;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_main);

        // Request permissions
        for (String permission : PERMISSIONS) {
            PermissionUtils.requestPermission(this, permission, permission.hashCode());
        }

        // Initiate variables
        RuntimeTypeAdapterFactory<Person> runtimeTypeAdapterFactory = RuntimeTypeAdapterFactory
                .of(Person.class, "type")
                .registerSubtype(Elderly.class, Elderly.class.getSimpleName());
        this.gson = new GsonBuilder().registerTypeAdapterFactory(runtimeTypeAdapterFactory).create();

        Intent intent = super.getIntent();
        String userJson = null;
        if (this.user == null) {
            userJson = intent.getStringExtra(User.USER_JSON);
            this.user = this.gson.fromJson(userJson, User.class);
        }
        if (this.user.getPerson() == null) {
            this.user.setPerson(new Elderly());
        }
        Elderly elderly = (Elderly) this.user.getPerson();
        if (userJson == null) {
            userJson = this.gson.toJson(this.user);
        }

        // Initiate FallDetectionService
        if (this.isReadyToStartFallDetectionService()) {
            FallDetectionService.startFallDetectionService(userJson, this);
            this.setTextViewTo(super.getString(R.string.fall_detection_service_running));
        } else {
            this.setTextViewTo(super.getString(R.string.fall_detection_service_not_running));
        }
        Button editElderyButton = super.findViewById(R.id.edit_elderly_button);
        editElderyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String elderlyJson = MainActivity.this.gson.toJson(elderly);
                MainActivity.this.startNewElderlyActivity(elderlyJson, MainActivity.this);
            }
        });

        Button editConfigsButton = super.findViewById(R.id.edit_config_button);
        editConfigsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userJson = MainActivity.this.gson.toJson(MainActivity.this.user);
                MainActivity.this.startConfigurationsActivity(MainActivity.this, userJson);
            }
        });
    }

    private void startConfigurationsActivity(Context context, String userJson) {
        this.mBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                //fixme change to a better location
                String updateUserJson = intent.getStringExtra(User.USER_JSON);
                MainActivity.this.user = MainActivity.this.gson.fromJson(updateUserJson, User.class);
                MainActivity.super.unregisterReceiver(MainActivity.this.mBroadcastReceiver);
            }
        };

        Intent newConfigurationsActivityIntent = new Intent(context, ConfigurationsActivity.class);
        newConfigurationsActivityIntent.putExtra(User.USER_JSON, userJson);
        this.startActivityForResult(newConfigurationsActivityIntent, ConfigurationsActivity.REQUEST_CODE);
    }

    private void startNewElderlyActivity(String elderlyJson,
                                         Context context) {

        this.mBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                //fixme change to a better location
                String updateUserJson = intent.getStringExtra(User.USER_JSON);
                MainActivity.this.user = MainActivity.this.gson.fromJson(updateUserJson, User.class);
                MainActivity.super.unregisterReceiver(MainActivity.this.mBroadcastReceiver);
            }
        };

        Intent newElderlyActivityIntent = new Intent(context, NewElderlyActivity.class);
        newElderlyActivityIntent.putExtra(Elderly.ELDERLY_JSON, elderlyJson);
        this.startActivityForResult(newElderlyActivityIntent, NewElderlyActivity.REQUEST_CODE);
    }

    private boolean isReadyToStartFallDetectionService() {
        Person person = this.user.getPerson();
        if (person != null) {
            Elderly elderly = (Elderly) person;
            List<Caregiver> caregivers = elderly.getCaregivers();
            if (caregivers != null && !caregivers.isEmpty()) {
                for (Caregiver caregiver : caregivers) {
                    List<Contact> contacts = caregiver.getContacts();
                    if (contacts != null && !contacts.isEmpty()) {
                        for (Contact contact : contacts) {
                            if (contact.getType() != null && contact.getContact() != null) {
                                return true;
                            }
                        }
                    }
                }
            }
        }
        return false;
    }

    @Override
    public void onActivityResult(int requestCode,
                                 int resultCode,
                                 Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && requestCode == NewElderlyActivity.REQUEST_CODE) {
            String elderlyJson = data.getStringExtra(Elderly.ELDERLY_JSON);
            this.user.setPerson(this.gson.fromJson(elderlyJson, Elderly.class));
            String userJson = this.gson.toJson(this.user);
            BroadcastReceiverUtils.registerBroadcastReceiver(this, this.mBroadcastReceiver, UserService.USER_SERVICE_ACTION_UPDATE);
            UserService.startUserService(userJson, CrudAction.UPDATE, this);
            if (this.isReadyToStartFallDetectionService()) {
                FallDetectionService.startFallDetectionService(userJson, this);
                this.setTextViewTo(super.getString(R.string.fall_detection_service_running));
            }
        } else if (resultCode == Activity.RESULT_OK && requestCode == ConfigurationsActivity.REQUEST_CODE) {
            String userJson = data.getStringExtra(User.USER_JSON);
            BroadcastReceiverUtils.registerBroadcastReceiver(this, this.mBroadcastReceiver, UserService.USER_SERVICE_ACTION_UPDATE);
            UserService.startUserService(userJson, CrudAction.UPDATE, this);
        }
    }

    //fixme Just to see if everything is okay
    private void setTextViewTo(String text) {
        TextView textView = super.findViewById(R.id.hello_world_text_view);
        textView.setText(text);
    }

    public static void startMainActivitySendUser(String userJson,
                                                 Context context) {
        Intent mainActivityIntent = new Intent(context, MainActivity.class);
        mainActivityIntent.putExtra(User.USER_JSON, userJson);
        context.startActivity(mainActivityIntent);
    }
}
