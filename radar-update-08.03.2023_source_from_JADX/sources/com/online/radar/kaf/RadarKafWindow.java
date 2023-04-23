package com.online.radar.kaf;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.IBinder;
import android.os.PowerManager;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import com.google.android.gms.location.FusedLocationProviderClient;
import java.util.Timer;
import java.util.TimerTask;
import org.json.JSONObject;

public class RadarKafWindow extends Service {
    private FusedLocationProviderClient fusedLocationClient;
    private ImageButton imageButton;
    JSONObject jsonObjectTariffs;
    /* access modifiers changed from: private */
    public LocationManager locationManager;
    /* access modifiers changed from: private */
    public View myView;
    /* access modifiers changed from: private */
    public SharedPreferences sharedPreferences;
    /* access modifiers changed from: private */
    public TextView textView;
    private TextView textView2;
    /* access modifiers changed from: private */

    /* renamed from: wm */
    public WindowManager f310wm;

    public IBinder onBind(Intent intent) {
        return null;
    }

    public void onCreate() {
        super.onCreate();
        View inflate = ((LayoutInflater) getSystemService("layout_inflater")).inflate(C1043R.layout.fragment_radar, (ViewGroup) null);
        this.myView = inflate;
        this.imageButton = (ImageButton) inflate.findViewById(C1043R.C1046id.imageButton_close);
        this.textView = (TextView) this.myView.findViewById(C1043R.C1046id.tv_radar);
        this.textView2 = (TextView) this.myView.findViewById(C1043R.C1046id.textView2);
        this.f310wm = (WindowManager) getSystemService("window");
        final WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams(-2, -2, Build.VERSION.SDK_INT >= 26 ? 2038 : 2002, 262184, -3);
        layoutParams.gravity = 8388661;
        layoutParams.x = 0;
        layoutParams.y = 0;
        this.f310wm.addView(this.myView, layoutParams);
        this.myView.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() != 2 && motionEvent.getAction() != 0) {
                    return false;
                }
                int width = view.getWidth() + view.getWidth();
                int height = view.getHeight() - (view.getHeight() / 2);
                layoutParams.x = width - ((int) motionEvent.getRawX());
                layoutParams.y = ((int) motionEvent.getRawY()) - height;
                RadarKafWindow.this.f310wm.updateViewLayout(RadarKafWindow.this.myView, layoutParams);
                return true;
            }
        });
        startGpsKaf(this.myView);
    }

    public int onStartCommand(final Intent intent, int i, int i2) {
        prepareForegroundNotification();
        this.sharedPreferences = getSharedPreferences("info", 0);
        this.imageButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                PendingIntent broadcast = PendingIntent.getBroadcast(RadarKafWindow.this.getApplicationContext(), 0, new Intent(RadarKafWindow.this.getApplicationContext(), MainActivity.class), 33554432);
                if (Build.VERSION.SDK_INT >= 26) {
                    RadarKafWindow.this.stopForeground(1);
                    RadarKafWindow.this.locationManager.removeUpdates(broadcast);
                    RadarKafWindow.this.stopService(intent);
                } else {
                    RadarKafWindow.this.locationManager.removeUpdates(broadcast);
                    RadarKafWindow.this.stopService(intent);
                }
                RadarKafWindow.this.f310wm.removeView(RadarKafWindow.this.myView);
                System.exit(0);
            }
        });
        return 1;
    }

    public void onDestroy() {
        super.onDestroy();
        System.exit(0);
    }

    private void startGpsKaf(View view) {
        this.locationManager = (LocationManager) this.myView.getContext().getSystemService("location");
        ((PowerManager) getSystemService("power")).newWakeLock(1, "MyApp::MyWakelockTag").acquire();
        if (ActivityCompat.checkSelfPermission(this, "android.permission.ACCESS_FINE_LOCATION") == 0 || ActivityCompat.checkSelfPermission(this, "android.permission.ACCESS_COARSE_LOCATION") == 0) {
            this.locationManager.requestLocationUpdates("gps", 1000, 10.0f, PendingIntent.getBroadcast(getApplicationContext(), 0, new Intent(getApplicationContext(), MainActivity.class), 33554432));
            if (Build.VERSION.SDK_INT >= 23) {
                Intent intent = new Intent();
                if (!((PowerManager) getSystemService("power")).isIgnoringBatteryOptimizations(getPackageName())) {
                    intent.setAction("android.settings.REQUEST_IGNORE_BATTERY_OPTIMIZATIONS");
                    intent.setData(Uri.parse("package:com.online.radar.kaf"));
                    startActivity(intent);
                }
            }
            new Timer().schedule(new TimerTask() {
                public void run() {
                    Location location;
                    if (ActivityCompat.checkSelfPermission(RadarKafWindow.this.myView.getContext(), "android.permission.ACCESS_FINE_LOCATION") == 0 || ActivityCompat.checkSelfPermission(RadarKafWindow.this.myView.getContext(), "android.permission.ACCESS_COARSE_LOCATION") == 0) {
                        try {
                            location = RadarKafWindow.this.locationManager.getLastKnownLocation("gps");
                        } catch (Exception unused) {
                            location = null;
                        }
                        KafTaskBackground kafTaskBackground = new KafTaskBackground();
                        if (location != null) {
                            kafTaskBackground.execute(new Float[]{Float.valueOf((float) location.getLatitude()), Float.valueOf((float) location.getLongitude())});
                        }
                    }
                }
            }, 0, 2500);
        }
    }

    private void prepareForegroundNotification() {
        PendingIntent pendingIntent;
        if (Build.VERSION.SDK_INT >= 26) {
            ((NotificationManager) getSystemService(NotificationManager.class)).createNotificationChannel(new NotificationChannel("chanel1", "Location Service Channel", 4));
        }
        Intent intent = new Intent(this, RadarKafWindow.class);
        if (Build.VERSION.SDK_INT > 26) {
            pendingIntent = PendingIntent.getActivity(this, 1, intent, 33554432);
        } else {
            pendingIntent = PendingIntent.getActivity(this, 1, intent, 134217728);
        }
        Notification build = new NotificationCompat.Builder((Context) this, "chanel1").setContentTitle("Радар запущен").setSmallIcon((int) C1043R.mipmap.ic_launcher).setContentIntent(pendingIntent).setOngoing(true).build();
        if (Build.VERSION.SDK_INT >= 29) {
            startForeground(2, build, 8);
        } else {
            startForeground(2, build);
        }
    }

    private class KafTaskBackground extends AsyncTask<Float, Void, String[]> {
        private KafTaskBackground() {
        }

        /* access modifiers changed from: protected */
        public String[] doInBackground(Float... fArr) {
            return PaidUtil.getKaf(fArr[0].floatValue(), fArr[1].floatValue(), RadarKafWindow.this.getApplicationContext());
        }

        /* access modifiers changed from: protected */
        public void onPostExecute(String[] strArr) {
            super.onPostExecute(strArr);
            String str = "";
            if (RadarKafWindow.this.sharedPreferences.getBoolean("econom", true)) {
                str = str + "Эко= " + strArr[0] + "\n";
            }
            if (RadarKafWindow.this.sharedPreferences.getBoolean("comfort", true)) {
                str = str + "Ком= " + strArr[1] + "\n";
            }
            if (RadarKafWindow.this.sharedPreferences.getBoolean("children", true)) {
                str = str + "Дет= " + strArr[2] + "\n";
            }
            if (RadarKafWindow.this.sharedPreferences.getBoolean("express", true)) {
                str = str + "Дост= " + strArr[6] + "\n";
            }
            if (RadarKafWindow.this.sharedPreferences.getBoolean("comfortplus", true)) {
                str = str + "Ком+= " + strArr[4] + "\n";
            }
            if (RadarKafWindow.this.sharedPreferences.getBoolean("minivan", true)) {
                str = str + "Мнв= " + strArr[7] + "\n";
            }
            if (RadarKafWindow.this.sharedPreferences.getBoolean("vip", true)) {
                str = str + "Биз= " + strArr[3] + "\n";
            }
            if (RadarKafWindow.this.sharedPreferences.getBoolean("cargo", true)) {
                str = str + "Груз= " + strArr[8] + "\n";
            }
            RadarKafWindow.this.textView.setText(str);
        }
    }
}
