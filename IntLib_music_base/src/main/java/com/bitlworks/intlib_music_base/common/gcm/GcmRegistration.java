package com.bitlworks.intlib_music_base.common.gcm;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.util.Log;

import com.bitlworks.intlib_music_base.common.CommonUtils;
import com.bitlworks.intlib_music_base.common.MusicClient;
import com.bitlworks.intlib_music_base.common.StaticValues;
import com.bitlworks.intlib_music_base.common.data.VOUser;
import com.bitlworks.intlib_music_base.common.source.ready.AuthCheckActivity;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.gson.JsonObject;


import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 기기 등록 아이디 가져오거나 등록하는 클래스
 */
public class GcmRegistration {
  private static final String TAG = "GCMLog";
  private static final String APP_VERSION = "1";
  //private static final String GOOGLE_PROJECT_ID = "435529839484";
  private static final String GOOGLE_PROJECT_ID = "1043490216464";//"95471565103";//95471565103
  // server api key = AIzaSyBm8uJapffq_Yu8M3ypjWqNpxWpStiHC4E
  private Context CONTEXT;
  private GoogleCloudMessaging gcm;
  private String regId, uuid, appver;

  public GcmRegistration(Context c) {
    CONTEXT = c;
  }

  public String registerGCM(String _uuid, String _appver) {
    this.uuid = _uuid;
    this.appver = _appver;

    gcm = GoogleCloudMessaging.getInstance(CONTEXT);
    regId = getRegistrationId(CONTEXT);

    if (TextUtils.isEmpty(regId)) {
      synchronized (regId) {
        registerInBackground();
      }
      Log.d(TAG, "registerGCM - successfully registered with GCM server - regId: " + regId);
    } else {
      Log.d(TAG, "RegId already available. RegId: " + regId);
    }
    return regId;
  }

  @SuppressLint("NewApi")
  private String getRegistrationId(Context context) {
    final SharedPreferences prefs = context.getSharedPreferences(AuthCheckActivity.class.getSimpleName(),
        Context.MODE_PRIVATE);
    String registrationId = prefs.getString("regId", "");

    if (registrationId.isEmpty()) {
      Log.i(TAG, "Registration not found.");
      return "";
    }
    int registeredVersion = prefs.getInt(APP_VERSION, Integer.MIN_VALUE);
    int currentVersion = getAppVersion(context);

    if (registeredVersion != currentVersion) {
      Log.i(TAG, "App version changed.");
      return "";
    }
    return registrationId;
  }

  private static int getAppVersion(Context context) {
    try {
      PackageInfo packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
      return packageInfo.versionCode;
    } catch (NameNotFoundException e) {
      Log.d("RegisterActivity", "I never expected this! Going down, going down!" + e);
      throw new RuntimeException(e);
    }
  }

  private void registerInBackground() {
    new AsyncTask<Void, Void, String>() {
      @Override
      protected String doInBackground(Void... params) {
        String msg = "";
        try {
          if (gcm == null) {
            gcm = GoogleCloudMessaging.getInstance(CONTEXT);
          }

          regId = gcm.register(GOOGLE_PROJECT_ID);
          Log.d(TAG, "registerInBackground - regId: " + regId);
          msg = "Device registered, registration ID=" + regId;

          storeRegistrationId(CONTEXT, regId);
        } catch (IOException ex) {
          msg = "Error :" + ex.getMessage();
          Log.d(TAG, "Error: " + msg);
        }
        Log.d(TAG, "AsyncTask completed: " + msg);
        return msg;
      }

      @Override
      protected void onPostExecute(String msg) {
        Log.d(TAG, "Registered with GCM Server." + msg);


        updateRegid(uuid, appver, regId);
      }
    }.execute(null, null, null);
  }

  private void storeRegistrationId(Context context, String regId) {

    final SharedPreferences prefs = context.getSharedPreferences(AuthCheckActivity.class.getSimpleName(), Context.MODE_PRIVATE);
    int appVersion = getAppVersion(context);
    Log.i(TAG, "Saving regId on app version " + appVersion);
    SharedPreferences.Editor editor = prefs.edit();
    editor.putString("regId", regId);
    editor.putInt(APP_VERSION, appVersion);
    editor.commit();

  }

  private void updateRegid(String uuid, String appver, String regId) {
    Call<JsonObject> call = MusicClient.getInstance().getService().updateRegid(StaticValues.user.user_id, uuid, appver, regId);
    call.enqueue(new Callback<JsonObject>() {
      @Override
      public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
        JsonObject object = response.body().getAsJsonObject();
        VOUser user = new VOUser(
            object.get("user_id").getAsInt(),
            object.get("user_name").getAsString(),
            object.get("album_id").getAsInt(),
            object.get("user_phone_number").getAsString(),
            object.get("user_appver").getAsString(),
            object.get("user_uuid").getAsString(),
            object.get("user_regid").getAsString(),
            object.get("user_level").getAsInt());

        StaticValues.user = user;
      }

      @Override
      public void onFailure(Call<JsonObject> call, Throwable t) {
        Log.e("onFailure", t.getMessage());
      }
    });
  }
}
