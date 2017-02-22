package com.bitlworks.intlib_bitlworks.gcm;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.util.Log;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.bitlworks.intlib_bitlworks.CommonUtils;

import java.io.IOException;


/**
 * 기기 등록 아이디 가져오거나 등록하는 클래스
 */
public class GcmRegistration {
  private static final String TAG = "GCMLog";
  private static final String APP_VERSION = "1";
  private static final String GOOGLE_PROJECT_ID = "1043490216464";
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

  private String getRegistrationId(Context context) {
    final SharedPreferences prefs = context.getSharedPreferences("GCM", Context.MODE_PRIVATE);
    String registrationId = prefs.getString("regId", "");

    if (registrationId.isEmpty()) {
      Log.i(TAG, "Registration not found.");
      return "";
    }
    int registeredVersion = prefs.getInt(APP_VERSION, Integer.MIN_VALUE);
    int currentVersion = CommonUtils.getAppVersion(context);

    if (registeredVersion != currentVersion) {
      Log.i(TAG, "App version changed.");
      return "";
    }
    return registrationId;
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


      }
    }.execute(null, null, null);
  }

  private void storeRegistrationId(Context context, String regId) {

    final SharedPreferences prefs = context.getSharedPreferences("GCM", Context.MODE_PRIVATE);
    int appVersion = CommonUtils.getAppVersion(context);
    Log.i(TAG, "Saving regId on app version " + appVersion);
    SharedPreferences.Editor editor = prefs.edit();
    editor.putString("regId", regId);
    editor.putInt(APP_VERSION, appVersion);
    editor.commit();
  }

}
