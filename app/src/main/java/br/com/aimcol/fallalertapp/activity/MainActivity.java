package br.com.aimcol.fallalertapp.activity;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.google.gson.Gson;

import br.com.aimcol.fallalertapp.R;
import br.com.aimcol.fallalertapp.model.Elderly;
import br.com.aimcol.fallalertapp.service.FallDetectionService;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_SMS = 0;

    private Elderly elderly;
    private Gson gson;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_main);

        // Request permissions
        this.requestSendSMSPermission();

        // Initiate variables
        this.gson = new Gson();

//        this.elderly == getLoggedUser().getElderly();
        if (this.elderly == null) {
            this.elderly = new Elderly();
        }

        // Initiate FallDetectionService
        if (this.isReadyToStartFallDetectionService()) {
            this.startFallDetectionService();
        }

        this.startNewElderlyActivity();
    }

    private void startFallDetectionService() {
        Intent fallDetectionServiceIntent = new Intent(this, FallDetectionService.class);
        fallDetectionServiceIntent.putExtra(Elderly.ELDERLY_JSON, this.gson.toJson(this.elderly));
        this.getBaseContext().startService(fallDetectionServiceIntent);
    }

    private void startNewElderlyActivity() {
        Intent newElderlyActivityIntent = new Intent(this, NewElderlyActivity.class);
        newElderlyActivityIntent.putExtra(Elderly.ELDERLY_JSON, this.gson.toJson(this.elderly));
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
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && requestCode == 1) {
            String elderlyJson = data.getStringExtra(Elderly.ELDERLY_JSON);
            this.elderly = this.gson.fromJson(elderlyJson, Elderly.class);
            //Just to see if everything is okay
            TextView textView = super.findViewById(R.id.hello_world_text_view);
            textView.setText(elderlyJson);
        }
    }

    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new android.support.v7.app.AlertDialog.Builder(this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }
}
