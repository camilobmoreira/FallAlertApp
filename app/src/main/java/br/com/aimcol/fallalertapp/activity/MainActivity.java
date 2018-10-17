package br.com.aimcol.fallalertapp.activity;

import android.Manifest;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

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

    private BroadcastReceiver mBroadcastReceiver;
    private User user;
    private Gson gson;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_main);

        // Request permissions
        PermissionUtils.requestPermission(this, Manifest.permission.SEND_SMS, REQUEST_SMS);
//        PermissionUtils.requestPermissionOnSettings(this, Settings.ACTION_MANAGE_OVERLAY_PERMISSION);

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
            this.setTextViewTo("Fall Detection Service Running");
        } else {
            //fixme remove from else and set the TextViews text to Elderly properties for editing an existing Elderly
            String elderlyJson = this.gson.toJson(elderly);
            this.startNewElderlyActivity(elderlyJson, this);
        }
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
        this.startActivityForResult(newElderlyActivityIntent, 1);
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
        if (resultCode == Activity.RESULT_OK && requestCode == 1) {
            String elderlyJson = data.getStringExtra(Elderly.ELDERLY_JSON);
            this.user.setPerson(this.gson.fromJson(elderlyJson, Elderly.class));
            String userJson = this.gson.toJson(this.user);
            BroadcastReceiverUtils.registerBroadcastReceiver(this, this.mBroadcastReceiver, UserService.USER_SERVICE_ACTION_UPDATE);
            UserService.startUserService(userJson, CrudAction.UPDATE, this);
            this.setTextViewTo(elderlyJson);
        }
    }

    //fixme Just to see if everything is okay
    private void setTextViewTo(String elderlyJson) {
        TextView textView = super.findViewById(R.id.hello_world_text_view);
        textView.setText(elderlyJson);
    }

    public static void startMainActivitySendUser(String userJson,
                                                 Context context) {
        Intent mainActivityIntent = new Intent(context, MainActivity.class);
        mainActivityIntent.putExtra(User.USER_JSON, userJson);
        context.startActivity(mainActivityIntent);
    }
}
