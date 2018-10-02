package br.com.aimcol.fallalertapp.util;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.IntentFilter;

public class BroadcastReceiverUtils {
    public static void registerBroadcastReceiver(Context context,
                                                 BroadcastReceiver broadcastReceiver,
                                                 String action) {
        try {
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction(action);
            context.registerReceiver(broadcastReceiver, intentFilter);
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
