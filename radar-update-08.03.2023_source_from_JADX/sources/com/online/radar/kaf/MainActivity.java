package com.online.radar.kaf;

import android.app.DownloadManager;
import android.app.PendingIntent;
import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.PowerManager;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import com.google.android.gms.location.FusedLocationProviderClient;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONObject;
import org.osmdroid.api.IMapController;
import org.osmdroid.config.Configuration;
import org.osmdroid.events.MapEventsReceiver;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.MapEventsOverlay;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.compass.CompassOverlay;
import org.osmdroid.views.overlay.compass.InternalCompassOrientationProvider;

public class MainActivity extends AppCompatActivity {
    public static final MediaType JSON = MediaType.get("application/json; charset=utf-8");
    private static int REQUEST_CODE = 1;
    private final int REQUEST_PERMISSIONS_REQUEST_CODE = 1;
    Button button_start;
    CheckBox checkBox_cargo;
    CheckBox checkBox_children;
    CheckBox checkBox_comfort;
    CheckBox checkBox_comfortplus;
    CheckBox checkBox_econom;
    CheckBox checkBox_express;
    CheckBox checkBox_minivan;
    CheckBox checkBox_vip;
    FusedLocationProviderClient fusedLocationClient;
    ImageButton imageButton_current_location;
    ImageButton imageButton_feedback;
    ImageButton imageButton_settings;
    boolean isFirstBootApp = true;
    /* access modifiers changed from: private */
    public boolean isSrollable;
    JSONObject jsonObjectTariffs;
    LocationManager locationManager;
    CompassOverlay mCompassOverlay;
    IMapController mapController;
    /* access modifiers changed from: private */
    public MapView mapView = null;
    private BroadcastReceiver receiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            if ("android.intent.action.DOWNLOAD_COMPLETE".equals(intent.getAction())) {
                MainActivity.this.installAPK();
            }
        }
    };
    SharedPreferences sharedPreferences;
    Marker startMarker;
    Timer timer;
    TextView tv_location;
    /* access modifiers changed from: private */
    public View view_settings;

    public boolean onTouchEvent(MotionEvent motionEvent) {
        if (motionEvent.getAction() == 1) {
            this.isSrollable = false;
        } else {
            this.isSrollable = true;
        }
        return super.onTouchEvent(motionEvent);
    }

    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView((int) C1043R.layout.activity_main);
        checkDrawOverlayPermission();
        PackageInfo packageInfo = null;
        View inflate = getLayoutInflater().inflate(C1043R.layout.dialog_settings, (ViewGroup) null, true);
        this.view_settings = inflate;
        this.checkBox_econom = (CheckBox) inflate.findViewById(C1043R.C1046id.checkBox_econom);
        this.checkBox_comfort = (CheckBox) this.view_settings.findViewById(C1043R.C1046id.checkBox_comfort);
        this.checkBox_children = (CheckBox) this.view_settings.findViewById(C1043R.C1046id.checkBox_children);
        this.checkBox_express = (CheckBox) this.view_settings.findViewById(C1043R.C1046id.checkBox_express);
        this.checkBox_minivan = (CheckBox) this.view_settings.findViewById(C1043R.C1046id.checkBox_minivan);
        this.checkBox_cargo = (CheckBox) this.view_settings.findViewById(C1043R.C1046id.checkBox_cargo);
        this.checkBox_vip = (CheckBox) this.view_settings.findViewById(C1043R.C1046id.checkBox_vip);
        this.checkBox_comfortplus = (CheckBox) this.view_settings.findViewById(C1043R.C1046id.checkBox_comfortplus);
        this.sharedPreferences = getSharedPreferences("info", 0);
        new CheckInfo(this, this, this.sharedPreferences).checkInfo();
        this.imageButton_feedback = (ImageButton) findViewById(C1043R.C1046id.imageButtonFeedBack);
        this.imageButton_current_location = (ImageButton) findViewById(C1043R.C1046id.ib_current_location);
        this.imageButton_settings = (ImageButton) findViewById(C1043R.C1046id.ib_settings);
        Context applicationContext = getApplicationContext();
        Configuration.getInstance().load(applicationContext, PreferenceManager.getDefaultSharedPreferences(applicationContext));
        MapView mapView2 = (MapView) findViewById(C1043R.C1046id.mapView);
        this.mapView = mapView2;
        mapView2.setTileSource(TileSourceFactory.MAPNIK);
        this.mapView.setBuiltInZoomControls(true);
        this.mapView.setClickable(true);
        this.mapView.setMultiTouchControls(true);
        this.mapView.getOverlayManager().getTilesOverlay().setLoadingBackgroundColor(C1043R.C1044color.black);
        this.mapView.getOverlayManager().getTilesOverlay().setLoadingLineColor(C1043R.C1044color.black);
        CompassOverlay compassOverlay = new CompassOverlay(applicationContext, new InternalCompassOrientationProvider(applicationContext), this.mapView);
        this.mCompassOverlay = compassOverlay;
        compassOverlay.enableCompass();
        this.mCompassOverlay.setCompassCenter(30.0f, 45.0f);
        this.mapView.getOverlays().add(this.mCompassOverlay);
        IMapController controller = this.mapView.getController();
        this.mapController = controller;
        controller.setZoom(18.5d);
        Marker marker = new Marker(this.mapView);
        this.startMarker = marker;
        marker.setIcon(getDrawable(C1043R.C1045drawable.ic_point_marker));
        try {
            downloadTask();
        } catch (Exception unused) {
        }
        this.mapView.getOverlays().add(new MapEventsOverlay(applicationContext, new MapEventsReceiver() {
            public boolean singleTapConfirmedHelper(GeoPoint geoPoint) {
                return false;
            }

            public boolean longPressHelper(GeoPoint geoPoint) {
                MainActivity.this.startMarker.setPosition(geoPoint);
                MainActivity.this.mapController.animateTo(geoPoint);
                return false;
            }
        }));
        requestPermissionsIfNecessary(new String[]{"android.permission.ACCESS_COARSE_LOCATION", "android.permission.ACCESS_FINE_LOCATION", "android.permission.WRITE_EXTERNAL_STORAGE"});
        this.imageButton_feedback.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                MainActivity mainActivity = MainActivity.this;
                new CheckInfo(mainActivity, mainActivity, mainActivity.sharedPreferences).showInfoWindow("Контакты", "Появились какие то вопросы или предложения? Тогда с нетерпением ждём Вас в нашем уютном Telegram чате '' Такси изнутри'' <a href=\"tg://resolve?domain=taix_work_inside\">@taxi_work_inside</a> ");
            }
        });
        this.imageButton_settings.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                boolean z = MainActivity.this.sharedPreferences.getBoolean("econom", true);
                boolean z2 = MainActivity.this.sharedPreferences.getBoolean("comfort", true);
                boolean z3 = MainActivity.this.sharedPreferences.getBoolean("children", true);
                boolean z4 = MainActivity.this.sharedPreferences.getBoolean("comfortplus", true);
                boolean z5 = MainActivity.this.sharedPreferences.getBoolean("express", true);
                boolean z6 = MainActivity.this.sharedPreferences.getBoolean("minivan", true);
                boolean z7 = MainActivity.this.sharedPreferences.getBoolean("vip", true);
                boolean z8 = MainActivity.this.sharedPreferences.getBoolean("cargo", true);
                MainActivity.this.checkBox_econom.setChecked(z);
                MainActivity.this.checkBox_comfort.setChecked(z2);
                MainActivity.this.checkBox_children.setChecked(z3);
                MainActivity.this.checkBox_comfortplus.setChecked(z4);
                MainActivity.this.checkBox_express.setChecked(z5);
                MainActivity.this.checkBox_minivan.setChecked(z6);
                MainActivity.this.checkBox_cargo.setChecked(z8);
                MainActivity.this.checkBox_vip.setChecked(z7);
                AlertDialog create = new AlertDialog.Builder(MainActivity.this).create();
                create.setCancelable(false);
                create.setView(MainActivity.this.view_settings);
                create.setButton(-1, (CharSequence) "применить", (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if ((MainActivity.this.checkBox_children.isChecked() | MainActivity.this.checkBox_comfort.isChecked() | MainActivity.this.checkBox_econom.isChecked() | MainActivity.this.checkBox_comfortplus.isChecked() | MainActivity.this.checkBox_cargo.isChecked() | MainActivity.this.checkBox_vip.isChecked() | MainActivity.this.checkBox_express.isChecked()) || MainActivity.this.checkBox_minivan.isChecked()) {
                            MainActivity.this.sharedPreferences.edit().putBoolean("econom", MainActivity.this.checkBox_econom.isChecked()).commit();
                            MainActivity.this.sharedPreferences.edit().putBoolean("comfort", MainActivity.this.checkBox_comfort.isChecked()).commit();
                            MainActivity.this.sharedPreferences.edit().putBoolean("children", MainActivity.this.checkBox_children.isChecked()).commit();
                            MainActivity.this.sharedPreferences.edit().putBoolean("express", MainActivity.this.checkBox_express.isChecked()).commit();
                            MainActivity.this.sharedPreferences.edit().putBoolean("minivan", MainActivity.this.checkBox_minivan.isChecked()).commit();
                            MainActivity.this.sharedPreferences.edit().putBoolean("comfortplus", MainActivity.this.checkBox_comfortplus.isChecked()).commit();
                            MainActivity.this.sharedPreferences.edit().putBoolean("vip", MainActivity.this.checkBox_vip.isChecked()).commit();
                            MainActivity.this.sharedPreferences.edit().putBoolean("cargo", MainActivity.this.checkBox_cargo.isChecked()).commit();
                            MainActivity.this.sharedPreferences.edit().apply();
                            ((ViewGroup) MainActivity.this.view_settings.getParent()).removeView(MainActivity.this.view_settings);
                            return;
                        }
                        Toast.makeText(MainActivity.this, "нужно выбрать хотябы один тариф", 1).show();
                        ((ViewGroup) MainActivity.this.view_settings.getParent()).removeView(MainActivity.this.view_settings);
                        MainActivity.this.imageButton_settings.performClick();
                    }
                });
                create.setButton(-2, (CharSequence) "отмена", (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogInterface, int i) {
                        ((ViewGroup) MainActivity.this.view_settings.getParent()).removeView(MainActivity.this.view_settings);
                    }
                });
                create.show();
            }
        });
        if (Build.VERSION.SDK_INT >= 23) {
            Intent intent = new Intent();
            if (!((PowerManager) getSystemService("power")).isIgnoringBatteryOptimizations(getPackageName())) {
                intent.setAction("android.settings.REQUEST_IGNORE_BATTERY_OPTIMIZATIONS");
                intent.setData(Uri.parse("package:com.online.radar.kaf"));
                startActivity(intent);
            }
        }
        this.button_start = (Button) findViewById(C1043R.C1046id.button_start);
        this.tv_location = (TextView) findViewById(C1043R.C1046id.tv_location);
        try {
            packageInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException unused2) {
        }
        final int i = packageInfo.versionCode;
        new Thread(new Runnable() {
            public void run() {
                final Integer lastAppVersion = MainActivity.this.getLastAppVersion();
                if (lastAppVersion.intValue() > i) {
                    MainActivity.this.runOnUiThread(new Runnable() {
                        public void run() {
                            AlertDialog create = new AlertDialog.Builder(MainActivity.this).create();
                            create.setTitle("Приложение устарело");
                            create.setCancelable(false);
                            create.setMessage(Html.fromHtml("Для далнейшего использования нужно обновить приложение"));
                            create.setButton(-3, (CharSequence) "закрыть приложение", (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.dismiss();
                                    System.exit(0);
                                }
                            });
                            create.setButton(-1, (CharSequence) "обновить", (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    new Thread(new Runnable() {
                                        public void run() {
                                            MainActivity.this.downloadAppAndInstall(lastAppVersion.toString());
                                        }
                                    }).start();
                                }
                            });
                            create.show();
                            ((TextView) create.findViewById(16908299)).setMovementMethod(LinkMovementMethod.getInstance());
                        }
                    });
                }
            }
        }).start();
        this.button_start.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                boolean unused = MainActivity.this.checkDrawOverlayPermission();
                AlertDialog create = new AlertDialog.Builder(MainActivity.this).create();
                create.setTitle("Важно!");
                create.setCancelable(false);
                create.setMessage("Убедитесь, что все запрошенные разрешения от программы были приняты. При нажатии ''Продолжить'', будет запущено инфо окно поверх ваших приложений. Обновление информации производится по текущей геолокации и раз в 2 секунды.");
                create.setButton(-1, (CharSequence) "Продолжить", (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent intent = new Intent(MainActivity.this, RadarKafWindow.class);
                        PendingIntent broadcast = PendingIntent.getBroadcast(MainActivity.this.getApplicationContext(), 0, new Intent(MainActivity.this.getApplicationContext(), MainActivity.class), 33554432);
                        if (Build.VERSION.SDK_INT >= 26) {
                            MainActivity.this.startForegroundService(intent);
                            MainActivity.this.locationManager.removeUpdates(broadcast);
                            MainActivity.this.timer.cancel();
                            MainActivity.this.finish();
                            return;
                        }
                        MainActivity.this.locationManager.removeUpdates(broadcast);
                        MainActivity.this.timer.cancel();
                        MainActivity.this.startService(intent);
                        MainActivity.this.finish();
                    }
                });
                create.setButton(-2, (CharSequence) "Отмена", (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                });
                create.show();
            }
        });
        this.imageButton_current_location.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Location lastKnownLocation;
                if ((ActivityCompat.checkSelfPermission(MainActivity.this, "android.permission.ACCESS_FINE_LOCATION") == 0 || ActivityCompat.checkSelfPermission(MainActivity.this, "android.permission.ACCESS_COARSE_LOCATION") == 0) && (lastKnownLocation = MainActivity.this.locationManager.getLastKnownLocation("gps")) != null) {
                    GeoPoint geoPoint = new GeoPoint(lastKnownLocation.getLatitude(), lastKnownLocation.getLongitude());
                    MainActivity.this.startMarker.setPosition(geoPoint);
                    MainActivity.this.startMarker.setAnchor(0.5f, 0.5f);
                    MainActivity.this.startMarker.showInfoWindow();
                    MainActivity.this.mapView.getOverlays().add(MainActivity.this.startMarker);
                    MainActivity.this.mapController.animateTo(geoPoint);
                    MainActivity.this.mapController.setZoom(18);
                }
            }
        });
        if (ActivityCompat.checkSelfPermission(this, "android.permission.ACCESS_FINE_LOCATION") == 0 || ActivityCompat.checkSelfPermission(this, "android.permission.ACCESS_COARSE_LOCATION") == 0) {
            LocationManager locationManager2 = (LocationManager) getSystemService("location");
            this.locationManager = locationManager2;
            if (locationManager2 != null) {
                this.locationManager.requestLocationUpdates("gps", 1000, 10.0f, PendingIntent.getBroadcast(getApplicationContext(), 33, new Intent(getApplicationContext(), MainActivity.class), 33554432));
            }
            Timer timer2 = new Timer();
            this.timer = timer2;
            timer2.schedule(new TimerTask() {
                public void run() {
                    final Location location;
                    if (ActivityCompat.checkSelfPermission(MainActivity.this, "android.permission.ACCESS_FINE_LOCATION") == 0 || ActivityCompat.checkSelfPermission(MainActivity.this, "android.permission.ACCESS_COARSE_LOCATION") == 0) {
                        try {
                            location = MainActivity.this.locationManager.getLastKnownLocation("gps");
                        } catch (Exception unused) {
                            location = null;
                        }
                        if (location != null) {
                            if (MainActivity.this.isFirstBootApp && (location != null)) {
                                MainActivity.this.runOnUiThread(new Runnable() {
                                    public void run() {
                                        GeoPoint geoPoint = new GeoPoint(location.getLatitude(), location.getLongitude());
                                        MainActivity.this.startMarker.setPosition(geoPoint);
                                        MainActivity.this.startMarker.showInfoWindow();
                                        MainActivity.this.startMarker.setAnchor(0.5f, 0.5f);
                                        MainActivity.this.mapView.getOverlays().add(MainActivity.this.startMarker);
                                        MainActivity.this.mapController.animateTo(geoPoint);
                                        MainActivity.this.isFirstBootApp = false;
                                    }
                                });
                                return;
                            }
                            final KafTask kafTask = new KafTask();
                            MainActivity.this.runOnUiThread(new Runnable() {
                                public void run() {
                                    GeoPoint geoPoint = new GeoPoint(MainActivity.this.startMarker.getPosition());
                                    kafTask.execute(new Float[]{Float.valueOf((float) geoPoint.getLatitude()), Float.valueOf((float) geoPoint.getLongitude())});
                                }
                            });
                        }
                    }
                }
            }, 0, 2500);
        }
    }

    /* access modifiers changed from: private */
    public boolean checkDrawOverlayPermission() {
        if (Build.VERSION.SDK_INT < 23) {
            return true;
        }
        if (!Settings.canDrawOverlays(this)) {
            Log.d("ContentValues", "canDrawOverlays NOK");
            startActivityForResult(new Intent("android.settings.action.MANAGE_OVERLAY_PERMISSION", Uri.parse("package:" + getPackageName())), REQUEST_CODE);
            return false;
        }
        Log.d("ContentValues", "canDrawOverlays OK");
        return true;
    }

    public void onRequestPermissionsResult(int i, String[] strArr, int[] iArr) {
        super.onRequestPermissionsResult(i, strArr, iArr);
        ArrayList arrayList = new ArrayList();
        for (int i2 = 0; i2 < iArr.length; i2++) {
            arrayList.add(strArr[i2]);
        }
        if (arrayList.size() > 0) {
            ActivityCompat.requestPermissions(this, (String[]) arrayList.toArray(new String[0]), 1);
        }
    }

    /* access modifiers changed from: package-private */
    public void downloadAppAndInstall(String str) {
        File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/radar.apk");
        if (file.exists()) {
            file.delete();
        }
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse("https://github.com/vitg8/radar_update/releases/download/v1/" + str + ".apk"));
        request.setTitle("update radar");
        request.setDescription("Downloading");
        request.setVisibleInDownloadsUi(true);
        request.setNotificationVisibility(1);
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "/radar.apk");
        registerReceiver(this.receiver, new IntentFilter("android.intent.action.DOWNLOAD_COMPLETE"));
        ((DownloadManager) getSystemService("download")).enqueue(request);
        startActivityForResult(new Intent("android.settings.MANAGE_UNKNOWN_APP_SOURCES").setData(Uri.parse(String.format("package:%s", new Object[]{getPackageName()}))), 1234);
    }

    /* access modifiers changed from: protected */
    public void onActivityResult(int i, int i2, Intent intent) {
        super.onActivityResult(i, i2, intent);
    }

    /* access modifiers changed from: package-private */
    public void installAPK() {
        String str = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/radar.apk";
        if (new File(str).exists()) {
            Intent intent = new Intent("android.intent.action.VIEW");
            intent.setDataAndType(uriFromFile(getApplicationContext(), new File(str)), "application/vnd.android.package-archive");
            intent.addFlags(268435456);
            intent.addFlags(1);
            try {
                getApplicationContext().startActivity(intent);
            } catch (ActivityNotFoundException e) {
                e.printStackTrace();
            }
        } else {
            Toast.makeText(getApplicationContext(), "not installing", 1).show();
        }
        try {
            Thread.sleep(5000);
            System.exit(0);
        } catch (InterruptedException e2) {
            throw new RuntimeException(e2);
        }
    }

    /* access modifiers changed from: package-private */
    public Uri uriFromFile(Context context, File file) {
        if (Build.VERSION.SDK_INT >= 24) {
            return FileProvider.getUriForFile(context, "com.online.radar.kaf.provider", file);
        }
        return Uri.fromFile(file);
    }

    /* access modifiers changed from: package-private */
    public Integer getLastAppVersion() {
        try {
            String string = new OkHttpClient().newCall(new Request.Builder().url("https://github.com/vitg8/radar_update/releases/expanded_assets/v1").get().build()).execute().body().string();
            int indexOf = string.indexOf("class=\"Truncate-text text-bold\">") + 32;
            int indexOf2 = string.indexOf(".apk</span>");
            if (indexOf != -1) {
                String trim = string.substring(indexOf, indexOf2).trim();
                Log.e("check update", "Last release version: " + trim);
                return Integer.valueOf(Integer.parseInt(trim));
            }
            Log.d("check update", "Failed to get last release version!");
            return 0;
        } catch (Exception e) {
            Log.d("check update", "Failed to get last release version:");
            e.printStackTrace();
        }
    }

    private void requestPermissionsIfNecessary(String[] strArr) {
        ArrayList arrayList = new ArrayList();
        for (String str : strArr) {
            if (ContextCompat.checkSelfPermission(this, str) != 0) {
                arrayList.add(str);
            }
        }
        if (arrayList.size() > 0) {
            ActivityCompat.requestPermissions(this, (String[]) arrayList.toArray(new String[0]), 1);
        }
        if (Build.VERSION.SDK_INT >= 26 && !getPackageManager().canRequestPackageInstalls()) {
            startActivityForResult(new Intent("android.settings.MANAGE_UNKNOWN_APP_SOURCES").setData(Uri.parse(String.format("package:%s", new Object[]{getPackageName()}))), 1234);
        }
    }

    public void onBackPressed() {
        super.onBackPressed();
        this.locationManager.removeUpdates(PendingIntent.getBroadcast(getApplicationContext(), 0, new Intent(getApplicationContext(), MainActivity.class), 33554432));
        System.exit(0);
    }

    /* access modifiers changed from: protected */
    public void onPause() {
        super.onPause();
    }

    /* access modifiers changed from: protected */
    public void onStart() {
        super.onStart();
    }

    /* access modifiers changed from: protected */
    public void onResume() {
        super.onResume();
    }

    class KafTask extends AsyncTask<Float, Void, String[]> {
        String[] result;

        KafTask() {
        }

        /* access modifiers changed from: protected */
        public String[] doInBackground(Float... fArr) {
            return PaidUtil.getKaf(fArr[0].floatValue(), fArr[1].floatValue(), MainActivity.this.getApplicationContext());
        }

        /* access modifiers changed from: protected */
        public void onPreExecute() {
            super.onPreExecute();
        }

        /* access modifiers changed from: protected */
        public void onPostExecute(String[] strArr) {
            Location location;
            super.onPostExecute(strArr);
            Geocoder geocoder = new Geocoder(MainActivity.this, Locale.getDefault());
            if (ActivityCompat.checkSelfPermission(MainActivity.this, "android.permission.ACCESS_FINE_LOCATION") == 0 || ActivityCompat.checkSelfPermission(MainActivity.this, "android.permission.ACCESS_COARSE_LOCATION") == 0) {
                List<Address> list = null;
                try {
                    location = MainActivity.this.locationManager.getLastKnownLocation("gps");
                } catch (Exception unused) {
                    location = null;
                }
                if (location != null) {
                    try {
                        List<Address> fromLocation = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                        if (fromLocation != null && fromLocation.size() > 0 && !MainActivity.this.isSrollable) {
                            GeoPoint position = MainActivity.this.startMarker.getPosition();
                            String str = "";
                            if (MainActivity.this.sharedPreferences.getBoolean("econom", true)) {
                                str = str + "Эконом= " + strArr[0] + "\n";
                            }
                            if (MainActivity.this.sharedPreferences.getBoolean("comfort", true)) {
                                str = str + "Комфорт= " + strArr[1] + "\n";
                            }
                            if (MainActivity.this.sharedPreferences.getBoolean("children", true)) {
                                str = str + "Детский= " + strArr[2] + "\n";
                            }
                            if (MainActivity.this.sharedPreferences.getBoolean("express", true)) {
                                str = str + "Экспресс= " + strArr[6] + "\n";
                            }
                            if (MainActivity.this.sharedPreferences.getBoolean("comfortplus", true)) {
                                str = str + "Комфорт+= " + strArr[4] + "\n";
                            }
                            if (MainActivity.this.sharedPreferences.getBoolean("minivan", true)) {
                                str = str + "Минивэн= " + strArr[7] + "\n";
                            }
                            if (MainActivity.this.sharedPreferences.getBoolean("vip", true)) {
                                str = str + "Бизнес= " + strArr[3] + "\n";
                            }
                            if (MainActivity.this.sharedPreferences.getBoolean("cargo", true)) {
                                str = str + "Грузовой= " + strArr[8] + "\n";
                            }
                            MainActivity.this.startMarker.setTitle(str);
                            MainActivity.this.startMarker.setAnchor(0.5f, 0.5f);
                            MainActivity.this.startMarker.showInfoWindow();
                            MainActivity.this.startMarker.setPosition(position);
                            MainActivity.this.mapView.getOverlays().add(MainActivity.this.startMarker);
                            try {
                                list = geocoder.getFromLocation(position.getLatitude(), position.getLongitude(), 1);
                            } catch (IOException unused2) {
                            }
                            if (list != null) {
                                MainActivity.this.tv_location.setText(list.get(0).getAddressLine(0));
                            }
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    private void downloadTask() throws Exception {
        Thread thread = new Thread(new Runnable() {
            public void run() {
                Response response;
                FileOutputStream openFileOutput;
                try {
                    response = new OkHttpClient().newCall(new Request.Builder().url("https://github.com/vitg8/radar_update/releases/download/v1/tariffs.json").build()).execute();
                } catch (IOException unused) {
                    response = null;
                }
                try {
                    openFileOutput = MainActivity.this.openFileOutput("tariffs.json", 0);
                    if (response != null) {
                        openFileOutput.write(response.body().bytes());
                    }
                    if (openFileOutput != null) {
                        openFileOutput.close();
                        return;
                    }
                    return;
                } catch (FileNotFoundException | IOException unused2) {
                    return;
                } catch (Throwable th) {
                    th.addSuppressed(th);
                }
                throw th;
            }
        });
        thread.start();
        thread.join();
    }
}
