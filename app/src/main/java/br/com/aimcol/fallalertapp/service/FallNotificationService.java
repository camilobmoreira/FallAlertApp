package br.com.aimcol.fallalertapp.service;

import android.Manifest;
import android.app.Activity;
import android.app.IntentService;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.annotation.Nullable;
import android.telephony.SmsManager;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import br.com.aimcol.fallalertapp.dto.ElderlyDTO;
import br.com.aimcol.fallalertapp.model.Caregiver;
import br.com.aimcol.fallalertapp.model.Contact;
import br.com.aimcol.fallalertapp.model.Elderly;
import br.com.aimcol.fallalertapp.model.Fall;
import br.com.aimcol.fallalertapp.model.FallHistory;
import br.com.aimcol.fallalertapp.model.User;
import br.com.aimcol.fallalertapp.util.PermissionUtils;

public class FallNotificationService extends IntentService {


    public static final String SMS_DELIVERED = "SMS_DELIVERED";
    public static final String SMS_SENT = "SMS_SENT";

    private DatabaseReference mDatabase;
    private Long lastSentInMillis;
    private Long minTimeToNotifyAgain;
    private Gson gson = new Gson();
    private BroadcastReceiver sentStatusReceiver;
    private BroadcastReceiver deliveredStatusReceiver;
//    private AlertDialog mAlertDialog;
//    private Handler mHandler;

    public FallNotificationService() {
        super(".FallNotificationService");
    }

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */
    public FallNotificationService(String name) {
        super(name);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {

    }

    @Override
    public int onStartCommand(@Nullable Intent intent,
                              int flags,
                              int startId) {

        this.minTimeToNotifyAgain = 3000000L;
        if (this.mDatabase == null) {
            this.mDatabase = FirebaseDatabase.getInstance().getReference();
        }

        if (intent != null) {
            String userJson = intent.getStringExtra(User.USER_JSON);
            User user = this.gson.fromJson(userJson, User.class);
            this.sendNotification((Elderly) user.getPerson());
            this.registerNewFall(user);
        }
        return super.onStartCommand(intent, flags, startId);
    }

    private void registerNewFall(User user) {
        FallHistory fallHistory = null;//getElderlyFallHistory(elderly);
        Elderly elderly = (Elderly) user.getPerson();
        if (fallHistory == null) {
            fallHistory = new FallHistory();
            fallHistory.setElderlyDTO(new ElderlyDTO(user.getKey(), elderly.getName()));
        }
        List<Fall> falls = fallHistory.getFalls();
        if (falls == null) {
            falls = new ArrayList<>();
        }
        Fall fall = new Fall();
        fall.setDate(new Date());
        falls.add(fall);

        this.mDatabase.child("fall_history");
    }

    private void sendNotification(Elderly elderly) {
        if (this.isItOkayToNotifyAgain()) {
            for (Caregiver caregiver : elderly.getCaregivers()) {
                for (Contact contact : caregiver.getContacts()) {
                    switch (contact.getType()) {
                        case SMS:
                            this.sendSms(contact.getContact(), "Someone fell down");
                            break;
//                    case EMAIL:
//                        success = this.sendEmail(name, contact);
//                        break;
//                    case WHATSAPP:
//                        success = this.sendWhatsapp(name, contact);
//                        break;
                    }
                }
            }
        } else {
            Toast.makeText(this.getApplicationContext(), "Not long enough since last notification", Toast.LENGTH_SHORT).show();
        }
    }

    private void sendSms(String contact,
                         String message) {

        if (PermissionUtils.checkPermission(this.getApplicationContext(), Manifest.permission.SEND_SMS)) {
            if (contact.isEmpty()) {
                Toast.makeText(this.getApplicationContext(), "Please Enter a Valid Phone Number", Toast.LENGTH_SHORT).show();
            } else {

                SmsManager sms = SmsManager.getDefault();
                // if message length is too long, messages are divided
                List<String> messages = sms.divideMessage(message);

                for (String msg : messages) {
//                    PendingIntent sentIntent = PendingIntent.getBroadcast(FallNotificationService.this, 0, new Intent(SMS_SENT), 0);
//                    PendingIntent deliveredIntent = PendingIntent.getBroadcast(FallNotificationService.this, 0, new Intent(SMS_DELIVERED), 0);
//                    sms.sendTextMessage(contact, null, msg, sentIntent, deliveredIntent);
                }
                FallNotificationService.this.lastSentInMillis = Calendar.getInstance().getTimeInMillis();
//                FallNotificationService.this.registerBroadcastReceiverForSms();
            }
        } else {
            throw new RuntimeException("No permission to send SMS");
        }
    }

    private boolean isItOkayToNotifyAgain() {
        return this.lastSentInMillis == null || (this.lastSentInMillis + this.minTimeToNotifyAgain) < Calendar.getInstance().getTimeInMillis();
    }

    public void registerBroadcastReceiverForSms() {
        this.sentStatusReceiver = new BroadcastReceiver() {

            @Override
            public void onReceive(Context arg0, Intent arg1) {
                String message = "Unknown Error";
                switch (this.getResultCode()) {
                    case Activity.RESULT_OK:
                        message = "Message Sent Successfully !!";
                        break;
                    case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
                        message = "Generic Failure Error";
                        break;
                    case SmsManager.RESULT_ERROR_NO_SERVICE:
                        message = "Error: No Service Available";
                        break;
                    case SmsManager.RESULT_ERROR_NULL_PDU:
                        message = "Error: Null PDU";
                        break;
                    case SmsManager.RESULT_ERROR_RADIO_OFF:
                        message = "Error: Radio is off";
                        break;
                    default:
                        break;
                }
                Toast.makeText(FallNotificationService.this.getApplicationContext(), message, Toast.LENGTH_SHORT).show();
            }
        };
        this.deliveredStatusReceiver = new BroadcastReceiver() {

            @Override
            public void onReceive(Context arg0, Intent arg1) {
                String message = "Message Not Delivered";
                switch (this.getResultCode()) {
                    case Activity.RESULT_OK:
                        message = "Message Delivered Successfully";
                        break;
                    case Activity.RESULT_CANCELED:
                        break;
                }
                Toast.makeText(FallNotificationService.this.getApplicationContext(), message, Toast.LENGTH_SHORT).show();
            }
        };
        this.registerReceiver(this.sentStatusReceiver, new IntentFilter(SMS_SENT));
        this.registerReceiver(this.deliveredStatusReceiver, new IntentFilter(SMS_DELIVERED));
    }


    public void unregisterBroadcastReceiverForSms() {
        this.unregisterReceiver(this.sentStatusReceiver);
        this.unregisterReceiver(this.deliveredStatusReceiver);
    }

//    private boolean sendWhatsapp(String name,
//                                 String contact) {
//        Intent intent = new Intent();
//        intent.setAction(Intent.ACTION_SENDTO);
//        intent.putExtra(Intent.EXTRA_TEXT, "This is my text to send.");
//        intent.setType("text/plain");
//        intent.setPackage("com.whatsapp");
//        intent.putExtra("address", contact); //não tá pegando o destinatario
//
//        try {
//            this.startActivity(intent);
//            //finish();
//            return true;
//        } catch (Exception e) {
//            Toast.makeText(this, "Sms not send", Toast.LENGTH_LONG).show();
//            e.printStackTrace();
//            return false;
//        }
//    }

//    private boolean sendEmail(String name,
//                              String contact) {
//        Intent intent = new Intent(Intent.ACTION_SENDTO);
//
//        intent.setData(Uri.parse("mailto:" + contact));
//        intent.putExtra("body", "caiu");
//        //Não tá enviando direto. É pra enviar sem confirmação
//
//        try {
//            this.startActivity(intent);
//            //finish();
//            return true;
//        } catch (Exception e) {
//            Toast.makeText(this, "Email not send", Toast.LENGTH_LONG).show();
//            e.printStackTrace();
//            return false;
//        }
//    }

//    private AlertDialog showMessageOKCancel(String message,
//                                            DialogInterface.OnClickListener okListener, DialogInterface.OnClickListener cancelListener) {
//        return new android.support.v7.app.AlertDialog.Builder(this)
//                .setMessage(message)
//                .setPositiveButton("I did", okListener)
//                .setNegativeButton("I did not", cancelListener)
//                .create();
//    }
}
