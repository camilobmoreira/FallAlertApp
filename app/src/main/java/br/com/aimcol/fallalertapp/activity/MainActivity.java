package br.com.aimcol.fallalertapp.activity;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import br.com.aimcol.fallalertapp.R;
import br.com.aimcol.fallalertapp.model.Caregiver;
import br.com.aimcol.fallalertapp.model.ContactType;
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




        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            int hasSMSPermission = this.checkSelfPermission(Manifest.permission.SEND_SMS);
            if (hasSMSPermission != PackageManager.PERMISSION_GRANTED) {
                this.requestPermissions(new String[] {Manifest.permission.SEND_SMS}, REQUEST_SMS);
            }
        }





        this.gson = new Gson();

        Map<ContactType, String> contacts = new HashMap<>();
        contacts.put(ContactType.SMS, "+5512999999999");

        Caregiver caregiver = new Caregiver();
        caregiver.setContacts(contacts);

        List<Caregiver> caregiverList = new ArrayList<>();
        caregiverList.add(caregiver);

        this.elderly = new Elderly();
        this.elderly.setName("Teste");
        this.elderly.setCaregivers(caregiverList);

        Intent fallDetectionServiceIntent = new Intent(this, FallDetectionService.class);
        fallDetectionServiceIntent.putExtra("elderlyJson", this.gson.toJson(this.elderly));
        this.getBaseContext().startService(fallDetectionServiceIntent);
    }

    // Extract to another class
    private boolean checkPermission() {
        return (ContextCompat.checkSelfPermission(this.getApplicationContext(), Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_GRANTED);
    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.SEND_SMS}, REQUEST_SMS);
    }

//    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
//        switch (requestCode) {
//            case REQUEST_SMS:
//                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                    Toast.makeText(getApplicationContext(), "Permission Granted, Now you can access sms", Toast.LENGTH_SHORT).show();
//                } else {
//                    Toast.makeText(getApplicationContext(), "Permission Denied, You cannot access and sms", Toast.LENGTH_SHORT).show();
//                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//                        if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, Manifest.permission.SEND_SMS)) {
////                            this.showMessageOKCancel("You need to allow access to both the permissions",
////                                    new DialogInterface.OnClickListener() {
////                                        @Override
////                                        public void onClick(DialogInterface dialog, int which) {
////                                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
////                                                ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.SEND_SMS}, REQUEST_SMS);
////                                            }
////                                        }
////                                    });
////                            return;
//                        }
//                    }
//                }
//                break;
//            default:
//                //super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//                return;
//        }
//    }

    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new android.support.v7.app.AlertDialog.Builder(this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }
}
