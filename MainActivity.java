package com.gtavi.countdown;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.graphics.Color;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.TextView;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class MainActivity extends Activity {
    private static final long LAUNCH_UTC = 1795006800000L;
    private final Handler handler = new Handler();
    private TextView timer;

    private final Runnable tick = new Runnable() {
        @Override public void run() {
            long left = LAUNCH_UTC - System.currentTimeMillis();
            if (left <= 0) {
                timer.setText("GTA VI\nIS OUT NOW!");
                return;
            }
            long d = TimeUnit.MILLISECONDS.toDays(left);
            long h = TimeUnit.MILLISECONDS.toHours(left) % 24;
            long m = TimeUnit.MILLISECONDS.toMinutes(left) % 60;
            long s = TimeUnit.MILLISECONDS.toSeconds(left) % 60;
            timer.setText(String.format(Locale.US,
                "%02d DAYS\n%02d HOURS\n%02d MINUTES\n%02d SECONDS", d, h, m, s));
            handler.postDelayed(this, 1000);
        }
    };

    @Override public void onCreate(Bundle state) {
        super.onCreate(state);
        getWindow().getDecorView().setBackgroundColor(Color.rgb(25, 10, 52));
        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setGravity(Gravity.CENTER);
        layout.setPadding(24, 24, 24, 24);

        TextView logo = new TextView(this);
        logo.setText("GTA VI");
        logo.setTextColor(Color.rgb(255, 133, 205));
        logo.setTextSize(54);
        logo.setGravity(Gravity.CENTER);
        logo.setTypeface(null, 1);

        timer = new TextView(this);
        timer.setTextColor(Color.WHITE);
        timer.setTextSize(27);
        timer.setGravity(Gravity.CENTER);
        timer.setPadding(0, 40, 0, 30);

        TextView date = new TextView(this);
        date.setText("18 NOVEMBER 2026 • 4:00 PM • YEMEN");
        date.setTextColor(Color.rgb(255, 175, 220));
        date.setTextSize(14);
        date.setGravity(Gravity.CENTER);

        layout.addView(logo);
        layout.addView(timer);
        layout.addView(date);
        setContentView(layout);
        handler.post(tick);
    }

    @Override protected void onDestroy() {
        handler.removeCallbacks(tick);
        super.onDestroy();
    }
}
