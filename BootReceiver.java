package com.gtavi.countdown;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class BootReceiver extends BroadcastReceiver {
    @Override public void onReceive(Context context, Intent intent) {
        CountdownWidget.updateAll(context);
        CountdownWidget.schedule(context);
    }
}
