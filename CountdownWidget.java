package com.gtavi.countdown;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;
import android.widget.RemoteViews;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class CountdownWidget extends AppWidgetProvider {
    // 18 Nov 2026, 16:00 Yemen (UTC+3) = 13:00 UTC.
    private static final long LAUNCH_UTC = 1795006800000L;
    private static final String ACTION_UPDATE = "com.gtavi.countdown.UPDATE";

    @Override public void onUpdate(Context context, AppWidgetManager manager, int[] ids) {
        updateAll(context);
        schedule(context);
    }

    @Override public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        if (ACTION_UPDATE.equals(intent.getAction())) {
            updateAll(context);
            schedule(context);
        }
    }

    static void updateAll(Context context) {
        AppWidgetManager manager = AppWidgetManager.getInstance(context);
        int[] ids = manager.getAppWidgetIds(new ComponentName(context, CountdownWidget.class));
        long left = Math.max(0L, LAUNCH_UTC - System.currentTimeMillis());

        String count, subtitle;
        if (left == 0L) {
            count = "IS OUT NOW!";
            subtitle = "WELCOME TO LEONIDA";
        } else {
            long d = TimeUnit.MILLISECONDS.toDays(left);
            long h = TimeUnit.MILLISECONDS.toHours(left) % 24;
            long m = TimeUnit.MILLISECONDS.toMinutes(left) % 60;
            count = String.format(Locale.US, "%02dD  %02dH  %02dM", d, h, m);
            subtitle = "18 NOV • 4:00 PM • YEMEN";
        }

        for (int id : ids) {
            RemoteViews v = new RemoteViews(context.getPackageName(), R.layout.countdown_widget);
            v.setTextViewText(R.id.widget_count, count);
            v.setTextViewText(R.id.widget_subtitle, subtitle);
            Intent open = new Intent(context, MainActivity.class);
            PendingIntent pi = PendingIntent.getActivity(context, 0, open,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
            v.setOnClickPendingIntent(R.id.widget_root, pi);
            manager.updateAppWidget(id, v);
        }
    }

    static void schedule(Context context) {
        Intent i = new Intent(context, CountdownWidget.class);
        i.setAction(ACTION_UPDATE);
        PendingIntent pi = PendingIntent.getBroadcast(context, 77, i,
            PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
        AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        if (am != null) {
            am.setInexactRepeating(AlarmManager.ELAPSED_REALTIME,
                SystemClock.elapsedRealtime() + 60_000L, 60_000L, pi);
        }
    }
}
