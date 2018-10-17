package br.com.aimcol.fallalertapp.util;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;

public class DialogUtils {

    public static AlertDialog generateDialog(Context context,
                                             String title,
                                             String message,
                                             @NonNull DialogInterface.OnClickListener positiveClick,
                                             String positiveButtonMessage,
                                             DialogInterface.OnClickListener negativeClick,
                                             String negativeButtonMessage) {

        if (positiveButtonMessage == null || positiveButtonMessage.isEmpty()) {
            positiveButtonMessage = "Okay";
        }
        if (negativeButtonMessage == null || negativeButtonMessage.isEmpty()) {
            negativeButtonMessage = "Cancel";
        }

        AlertDialog.Builder adb = new AlertDialog.Builder(context);

        adb.setTitle(title);
        adb.setMessage(message);
        adb.setPositiveButton(positiveButtonMessage, positiveClick);

        if(negativeClick != null) {
            adb.setNegativeButton(negativeButtonMessage, negativeClick);
        }
        else {
            adb.setNegativeButton(negativeButtonMessage, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
        }

        return adb.create();
    }
}
