package br.com.aimcol.fallalertapp.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

import br.com.aimcol.fallalertapp.R;
import br.com.aimcol.fallalertapp.model.User;
import br.com.aimcol.fallalertapp.service.FallNotificationService;
import br.com.aimcol.fallalertapp.util.DialogUtils;

public class FallNotificationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.activity_fall_notification);

        this.turnOnScreen();

        Intent intent = super.getIntent();
        String userJson = intent.getStringExtra(User.USER_JSON);

        DialogUtils.generateDialog(this, "Fall Happened", "Send notification?", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Log.d("FNA-Send-Notification", "Sending Fall Notification");
                FallNotificationService.startFallNotificationService(FallNotificationActivity.this, userJson);
                FallNotificationActivity.this.finish();
            }
        }, "Send", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                FallNotificationActivity.this.finish();
            }
        }, "I'm okay. Cancel").show();

    }

    private void turnOnScreen() {
        Window window;
        window = this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);
        window.addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);
        window.addFlags(WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
    }

    public static void startFallNotificationActivity(Context context,
                                                 String userJson) {
        Intent FallNotificationActivity = new Intent(context, FallNotificationActivity.class);
        FallNotificationActivity.putExtra(User.USER_JSON, userJson);
        context.startActivity(FallNotificationActivity);
    }
}
