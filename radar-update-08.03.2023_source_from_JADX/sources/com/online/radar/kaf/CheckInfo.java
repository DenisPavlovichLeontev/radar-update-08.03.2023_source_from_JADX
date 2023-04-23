package com.online.radar.kaf;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.widget.TextView;
import androidx.appcompat.app.AlertDialog;
import androidx.work.WorkRequest;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class CheckInfo {
    /* access modifiers changed from: private */
    public Activity activity;
    private Context context;
    /* access modifiers changed from: private */
    public SharedPreferences sharedPreferences;

    public CheckInfo(Context context2, Activity activity2, SharedPreferences sharedPreferences2) {
        this.activity = activity2;
        this.context = context2;
        this.sharedPreferences = sharedPreferences2;
    }

    /* access modifiers changed from: package-private */
    public void checkInfo() {
        final OkHttpClient okHttpClient = new OkHttpClient();
        final Request build = new Request.Builder().url("http://185.251.90.210:8080/radar-info").get().build();
        new Timer().schedule(new TimerTask() {
            public void run() {
                new Thread(new Runnable() {
                    public void run() {
                        Response execute;
                        Set<String> stringSet = CheckInfo.this.sharedPreferences.getStringSet("message", new HashSet());
                        try {
                            execute = okHttpClient.newCall(build).execute();
                            if (execute.code() == 200) {
                                JSONArray jSONArray = new JSONArray(execute.body().string());
                                new JSONObject();
                                for (int i = 0; i < jSONArray.length(); i++) {
                                    String string = jSONArray.getJSONObject(i).getString("id");
                                    if (!stringSet.contains(string)) {
                                        stringSet.add(string);
                                        CheckInfo.this.sharedPreferences.edit().putStringSet("message", stringSet).commit();
                                        CheckInfo.this.showInfoWindow(jSONArray.getJSONObject(i).getString("title"), jSONArray.getJSONObject(i).getString("message"));
                                    }
                                }
                            }
                            if (execute != null) {
                                execute.close();
                                return;
                            }
                            return;
                        } catch (IOException unused) {
                            CheckInfo.this.showInfoWindow("info", "отсутствует интернет");
                            return;
                        } catch (JSONException unused2) {
                            return;
                        } catch (Throwable th) {
                            th.addSuppressed(th);
                        }
                        throw th;
                    }
                }).start();
            }
        }, WorkRequest.MIN_BACKOFF_MILLIS, 15000);
    }

    public void showInfoWindow(final String str, final String str2) {
        this.activity.runOnUiThread(new Runnable() {
            public void run() {
                AlertDialog create = new AlertDialog.Builder(CheckInfo.this.activity).create();
                create.setTitle(str);
                create.setCancelable(false);
                create.setMessage(Html.fromHtml(str2));
                create.setButton(-3, (CharSequence) "ok", (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                });
                create.show();
                ((TextView) create.findViewById(16908299)).setMovementMethod(LinkMovementMethod.getInstance());
            }
        });
    }
}
