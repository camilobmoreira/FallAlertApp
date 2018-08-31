package br.com.aimcol.fallalertapp.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.google.gson.Gson;

import br.com.aimcol.fallalertapp.R;
import br.com.aimcol.fallalertapp.model.Elderly;
import br.com.aimcol.fallalertapp.model.User;
import br.com.aimcol.fallalertapp.service.FallDetectionService;
import br.com.aimcol.fallalertapp.service.UserService;
import br.com.aimcol.fallalertapp.util.CrudAction;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_SMS = 0;

    private User user;
    private Gson gson;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_main);

        // Request permissions
        this.requestSendSMSPermission();

        // Initiate variables
        this.gson = new Gson();

        Intent intent = super.getIntent();
        if (this.user == null) {
            String userJson = intent.getStringExtra(User.USER_JSON);
            this.user = this.gson.fromJson(userJson, User.class);
        }
        if (this.user.getPerson() == null) {
            this.user.setPerson(new Elderly());
        }
        Elderly elderly = (Elderly) this.user.getPerson();
        String elderlyJson = this.gson.toJson(elderly);

        // Initiate FallDetectionService
        if (this.isReadyToStartFallDetectionService()) {
            FallDetectionService.startFallDetectionService(elderlyJson, this);
        }

        this.startNewElderlyActivity(elderlyJson, this);
    }

    private void startNewElderlyActivity(String elderlyJson,
                                         Context context) {

        Intent newElderlyActivityIntent = new Intent(context, NewElderlyActivity.class);
        newElderlyActivityIntent.putExtra(Elderly.ELDERLY_JSON, elderlyJson);
        this.startActivityForResult(newElderlyActivityIntent, 1);
    }

    private boolean isReadyToStartFallDetectionService() {
        return false;
    }

    private void requestSendSMSPermission() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            int hasSMSPermission = this.checkSelfPermission(Manifest.permission.SEND_SMS);
            if (hasSMSPermission != PackageManager.PERMISSION_GRANTED) {
                this.requestPermissions(new String[] {Manifest.permission.SEND_SMS}, REQUEST_SMS);
            }
        }
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
            UserService.startUserService(userJson, CrudAction.UPDATE, this);
            //Just to see if everything is okay
            TextView textView = super.findViewById(R.id.hello_world_text_view);
            textView.setText(elderlyJson);
        }
    }

    public static void startMainActivitySendUser(String userJson,
                                                 Context context) {
        Intent mainActivityIntent = new Intent(context, MainActivity.class);
        mainActivityIntent.putExtra(User.USER_JSON, userJson);
        context.startActivity(mainActivityIntent);
    }

    private void showMessageOKCancel(String message,
                                     DialogInterface.OnClickListener okListener) {
        new android.support.v7.app.AlertDialog.Builder(this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }
}
