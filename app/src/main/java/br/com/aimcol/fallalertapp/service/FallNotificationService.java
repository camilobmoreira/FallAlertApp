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
import com.google.gson.GsonBuilder;

import java.util.List;

import br.com.aimcol.fallalertapp.R;
import br.com.aimcol.fallalertapp.model.Caregiver;
import br.com.aimcol.fallalertapp.model.Configuration;
import br.com.aimcol.fallalertapp.model.Contact;
import br.com.aimcol.fallalertapp.model.Elderly;
import br.com.aimcol.fallalertapp.model.Person;
import br.com.aimcol.fallalertapp.model.User;
import br.com.aimcol.fallalertapp.util.PermissionUtils;
import br.com.aimcol.fallalertapp.util.RuntimeTypeAdapterFactory;

public class FallNotificationService extends IntentService {


    public static final String SMS_DELIVERED = "SMS_DELIVERED";
    public static final String SMS_SENT = "SMS_SENT";

    private DatabaseReference mDatabase;
    private Long lastSentInMillis;
    private Long minTimeToNotifyAgain = 30000L;
    private Gson gson;
    private BroadcastReceiver sentStatusReceiver;
    private BroadcastReceiver deliveredStatusReceiver;
    private User user;

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
        RuntimeTypeAdapterFactory<Person> runtimeTypeAdapterFactory = RuntimeTypeAdapterFactory
                .of(Person.class, "type")
                .registerSubtype(Elderly.class, Elderly.class.getSimpleName());
        this.gson = new GsonBuilder().registerTypeAdapterFactory(runtimeTypeAdapterFactory).create();

        if (this.mDatabase == null) {
            this.mDatabase = FirebaseDatabase.getInstance().getReference();
        }

        if (intent != null) {
            String userJson = intent.getStringExtra(User.USER_JSON);
            this.user = this.gson.fromJson(userJson, User.class);

            Long minTime = (Long) this.user.getConfigurations().get(Configuration.MIN_TIME_TO_NOTIFY_AGAIN);
            this.minTimeToNotifyAgain = minTime != null ? minTime : this.minTimeToNotifyAgain;

            this.sendNotification((Elderly) this.user.getPerson());
            FallHistoryService.startFallHistoryService(userJson, FallHistoryService.FALL_HISTORY_ACTION_REGISTER_NEW_FALL, this);
        }
        return super.onStartCommand(intent, flags, startId);
    }

    private void sendNotification(Elderly elderly) {
        if (this.isOkayToNotifyAgain()) {
            StringBuilder msg = null;
            String customMsg = (String) this.user.getConfigurations().get(Configuration.CUSTOM_MSG_FOR_FALL_EVENT);
            boolean customMsgIsNullOrEmpty = customMsg == null || customMsg.isEmpty();
            for (Caregiver caregiver : elderly.getCaregivers()) {
                if (customMsgIsNullOrEmpty) {
                    msg = new StringBuilder();
                    msg.append(caregiver.getName()).append(", ").append(elderly.getName())
                            .append(" ").append(super.getString(R.string.fell_down_message));
                }
                for (Contact contact : caregiver.getContacts()) {
                    switch (contact.getType()) {
                        case SMS:
                            this.sendSms(contact.getContact(), msg == null ? customMsg : msg.toString());
                            break;
                    }
                }
            }
            FallNotificationService.this.lastSentInMillis = System.currentTimeMillis();
        } else {
            Toast.makeText(this.getApplicationContext(),
                    super.getString(R.string.not_long_enough_since_last_notification), Toast.LENGTH_SHORT).show();
        }
    }

    private void sendSms(String contact,
                         String message) {

        if (PermissionUtils.checkPermission(this.getApplicationContext(), Manifest.permission.SEND_SMS)) {
            if (contact.isEmpty()) {
                Toast.makeText(this.getApplicationContext(),
                        super.getString(R.string.please_enter_a_valid_phone_number), Toast.LENGTH_SHORT).show();
            } else {
                SmsManager sms = SmsManager.getDefault();
                // if message length is too long, messages are divided
                List<String> messages = sms.divideMessage(message);
                for (String msg : messages) {
                    PendingIntent sentIntent = PendingIntent.getBroadcast(
                            FallNotificationService.this, 0, new Intent(SMS_SENT), 0);
                    PendingIntent deliveredIntent = PendingIntent.getBroadcast(
                            FallNotificationService.this, 0, new Intent(SMS_DELIVERED), 0);
                    sms.sendTextMessage(contact, null, msg, sentIntent, deliveredIntent);
                }
                FallNotificationService.this.registerBroadcastReceiverForSms();
            }
        } else {
            throw new RuntimeException(super.getString(R.string.no_permission_to_send_sms));
        }
    }

    private boolean isOkayToNotifyAgain() {
        return this.lastSentInMillis == null || (this.lastSentInMillis + this.minTimeToNotifyAgain) < System.currentTimeMillis();
    }

    public void registerBroadcastReceiverForSms() {
        this.sentStatusReceiver = new BroadcastReceiver() {

            @Override
            public void onReceive(Context arg0, Intent arg1) {
                String message = FallNotificationService.super.getString(R.string.unknown_error);
                switch (this.getResultCode()) {
                    case Activity.RESULT_OK:
                        message = FallNotificationService.super.getString(R.string.message_sent_succesfully);
                        break;
                    case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
                        message = FallNotificationService.super.getString(R.string.generic_failure_error);
                        break;
                    case SmsManager.RESULT_ERROR_NO_SERVICE:
                        message = FallNotificationService.super.getString(R.string.error_no_service);
                        break;
                    case SmsManager.RESULT_ERROR_NULL_PDU:
                        message = FallNotificationService.super.getString(R.string.error_null_pdu);
                        break;
                    case SmsManager.RESULT_ERROR_RADIO_OFF:
                        message = FallNotificationService.super.getString(R.string.error_radio_off);
                        break;
                    default:
                        break;
                }
                Toast.makeText(FallNotificationService.this.getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                FallNotificationService.this.unregisterReceiver(FallNotificationService.this.sentStatusReceiver);
            }
        };
        this.deliveredStatusReceiver = new BroadcastReceiver() {

            @Override
            public void onReceive(Context arg0, Intent arg1) {
                String message = FallNotificationService.super.getString(R.string.message_not_delivered);
                switch (this.getResultCode()) {
                    case Activity.RESULT_OK:
                        message = FallNotificationService.super.getString(R.string.message_delivered_succesfully);
                        break;
                    case Activity.RESULT_CANCELED:
                        break;
                }
                Toast.makeText(FallNotificationService.this.getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                FallNotificationService.this.unregisterReceiver(FallNotificationService.this.deliveredStatusReceiver);
            }
        };
        this.registerReceiver(this.sentStatusReceiver, new IntentFilter(SMS_SENT));
        this.registerReceiver(this.deliveredStatusReceiver, new IntentFilter(SMS_DELIVERED));
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

    public static void startFallNotificationService(Context context,
                                                    String userJson) {
        Intent fallNotificationServiceIntent = new Intent(context, FallNotificationService.class);
        fallNotificationServiceIntent.putExtra(User.USER_JSON, userJson);
        context.startService(fallNotificationServiceIntent);
    }
}
