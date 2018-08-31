package br.com.aimcol.fallalertapp.service;

import android.Manifest;
import android.app.Activity;
import android.app.IntentService;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.telephony.SmsManager;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.List;

import br.com.aimcol.fallalertapp.model.Caregiver;
import br.com.aimcol.fallalertapp.model.Contact;
import br.com.aimcol.fallalertapp.model.Elderly;
import br.com.aimcol.fallalertapp.util.PermissionUtils;

public class FallNotificationService extends IntentService {


    public static final String SMS_DELIVERED = "SMS_DELIVERED";
    public static final String SMS_SENT = "SMS_SENT";
//    private Elderly elderly;
    private Gson gson = new Gson();
    private BroadcastReceiver sentStatusReceiver;
    private BroadcastReceiver deliveredStatusReceiver;

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

        String elderlyJson = intent.getStringExtra(Elderly.ELDERLY_JSON);
        Elderly elderly = this.gson.fromJson(elderlyJson, Elderly.class);
        this.sendNotification(elderly);
        return super.onStartCommand(intent, flags, startId);
    }

    private boolean sendNotification(Elderly elderly) {
        boolean success = false;
        String name = elderly.getName();
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
        return success;
    }

    private void sendSms(String contact,
                         String message) {

        //fixme check for last sms sent so it doesn't send too many texts in a short period of time
        if (PermissionUtils.checkPermission(this.getApplicationContext(), Manifest.permission.SEND_SMS) && false) {
            if (contact.isEmpty()) {
                Toast.makeText(this.getApplicationContext(), "Please Enter a Valid Phone Number", Toast.LENGTH_SHORT).show();
            } else {

                SmsManager sms = SmsManager.getDefault();
                // if message length is too long messages are divided
                List<String> messages = sms.divideMessage(message);

                for (String msg : messages) {
                    PendingIntent sentIntent = PendingIntent.getBroadcast(this, 0, new Intent(SMS_SENT), 0);
                    PendingIntent deliveredIntent = PendingIntent.getBroadcast(this, 0, new Intent(SMS_DELIVERED), 0);
                    sms.sendTextMessage(contact, null, msg, sentIntent, deliveredIntent);
                }
                this.registerBroadcastReceiverForSms();
            }
        } else {
            throw new RuntimeException("No permission to send SMS");
        }
    }

    public void registerBroadcastReceiverForSms() {
        sentStatusReceiver = new BroadcastReceiver() {

            @Override
            public void onReceive(Context arg0, Intent arg1) {
                String message = "Unknown Error";
                switch (getResultCode()) {
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
            }
        };
        deliveredStatusReceiver = new BroadcastReceiver() {

            @Override
            public void onReceive(Context arg0, Intent arg1) {
                String message = "Message Not Delivered";
                switch (getResultCode()) {
                    case Activity.RESULT_OK:
                        message = "Message Delivered Successfully";
                        break;
                    case Activity.RESULT_CANCELED:
                        break;
                }
            }
        };
        this.registerReceiver(sentStatusReceiver, new IntentFilter(SMS_SENT));
        this.registerReceiver(deliveredStatusReceiver, new IntentFilter(SMS_DELIVERED));
    }


    public void unregisterBroadcastReceiverForSms() {
        this.unregisterReceiver(sentStatusReceiver);
        this.unregisterReceiver(deliveredStatusReceiver);
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
}
