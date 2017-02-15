package com.bitlworks.intlib_music_base.common.source.ready;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.bitlworks.WeddingAlbum.DataNet.*;
import com.bitlworks.intlib_music_base.common.StaticValues;
import com.bitlworks.intlib_music_base.common.source.PagerMainActivity;
import com.bitlworks.music._common.data.DAOSqlite;
import com.bitlworks.music._common.data.DataNetUtils;
import com.bitlworks.music._common.data.VOUser;
import com.bitlworks.music._common.data.VOdisk;
import com.bitlworks.music._common.network.NETgetUserInfo;
import com.bitlworks.music._common.network.NETupdateRegid;
import com.bitlworks.music._common.network.Network;
import com.bitlworks.music.common.Static;
import com.bitlworks.music.gcm.GcmRegistration;
import com.bitlworks.music.utils.CommonUtils;
import com.bitlworks.wedding.resources.StudioValues;

import org.apache.http.client.ClientProtocolException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

public class AuthCheckActivity extends Activity {

  Handler handler;
  DAOSqlite sqlDAO;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    StaticValues.SERVICE_URL = StaticValues.getServiceUrl(this);

    Intent iin = getIntent();
    Bundle bundle = iin.getExtras();

    if (bundle != null) {
      Log.d("Main Activity hyuk", "check is true");
      finish();
      System.exit(0);
      Boolean j = (Boolean) bundle.get("exit");

      if (j) {
        finish();
        Log.d("Main Activity hyuk", "check is true");
      } else {
        Log.d("Main Activity hyuk", "check is false");
      }
    } else {
      Log.d("Main Activity hyuk", "check is true22222222222222");
    }

    if (StaticValues.nework_check == 100) {
      checkGCMRegID();
    }

    sqlDAO = DAOSqlite.getInstance(this);
    requestWindowFeature(Window.FEATURE_NO_TITLE);
    getWindow().setFlags(
        WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
    setContentView(R.layout.activity_startloading);
    super.onCreate(savedInstanceState);

    handler = new Handler();
    handler.postDelayed(versionCheckRunnable, 1000);
  }

  private void checkGCMRegID() {
    String saved_regid = StaticValues.myInfo.user_regid;//.getUser_regid();
    String saved_uuid = StaticValues.myInfo.user_uuid;//.getUser_uuid();
    String saved_appver = StaticValues.myInfo.user_appver;//.getUser_appver();

    String now_uuid = CommonUtils.getDevicesUUID(AuthCheckActivity.this);
    String now_appver = getAppVersion(AuthCheckActivity.this) + "";

    registerGCMid(now_uuid, now_appver);
  }

  public int getAppVersion(Context context) {
    try {
      PackageInfo packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
      return packageInfo.versionCode;
    } catch (PackageManager.NameNotFoundException e) {
      Log.d("RegisterActivity", "I never expected this! Going down, going down!" + e);
      throw new RuntimeException(e);
    }
  }

  private void registerGCMid(String uuid, String appver) {

    Log.w("T_T", "registerGCMid()");
    // GCM등록ID 구하기
    GcmRegistration gr = new GcmRegistration(AuthCheckActivity.this);
    String regId = gr.registerGCM(uuid, appver);

    new NETupdateRegid(StaticValues.myInfo.user_id, uuid, appver, regId, afterUpdateRegid);
  }

  @SuppressLint("HandlerLeak")
  private final Handler afterUpdateRegid = new Handler() {
    @Override
    public void handleMessage(Message msg) {
      if (msg.what == -1) {
        Toast.makeText(AuthCheckActivity.this, "Network Error", Toast.LENGTH_SHORT).show();
      } else {
        try {
          final JSONObject obj = new JSONObject(msg.obj.toString());

          final VOUser me = new VOUser(
              obj.getInt("user_id"),
              obj.getString("user_name"),
              obj.getInt("album_id"),
              obj.getString("user_phone_number"),
              obj.getString("user_appver"),
              obj.getString("user_uuid"),
              obj.getString("user_regid"), obj.getInt("user_level"));

          // parse JSON -> preference store & static obj
          StaticValues.myInfo = me;

        } catch (JSONException e) {
          Log.i(StaticValues.LOG_TAG, e.toString());
        }
      } // END if
    }
  };

  @Override
  public void onBackPressed() {
    super.onBackPressed();
    handler.removeCallbacks(versionCheckRunnable);
  }

  Runnable versionCheckRunnable = new Runnable() {
    @Override
    public void run() {
      new VersionCheckTask().execute();
    }
  };

  class VersionCheckTask extends AsyncTask<Void, Void, Integer> {

    @Override
    protected Integer doInBackground(Void... params) {
      if (StudioValues.IS_SAMPLE_PROJECT) {
        AlbumUtils.createAuth(AuthCheckActivity.this, "Y", "0123456789");
      }
      return 1;
    }

    @Override
    protected void onPostExecute(Integer result) {
      if (StudioValues.IS_SAMPLE_PROJECT || result > 0) {

        final int MY_ID = CommonUtils.getMyID(AuthCheckActivity.this);
        // 인증 된 회원이 아니라면 가입으로 연결
        if (MY_ID < 1) {
          Intent intent = new Intent(AuthCheckActivity.this, Register.class);
          startActivity(intent);
          finish();
          return;
        }
        if (!DataNetUtils.isNetworkConnect(AuthCheckActivity.this)) {
          StaticValues.myInfo = sqlDAO.getUser();
          StaticValues.diskList = sqlDAO.getdiskList();
          VOdisk item2 = StaticValues.diskList.get(0);
          DataNetUtils.setSelectedCoupleId(AuthCheckActivity.this, item2.disk_id);
          Log.d("hyuk>>", "cid>>" + item2.disk_id);

          StudioValues.MOBILE_MUSIC_ID = item2.disk_id;
          StudioValues.disk_id = item2.disk_id;
          StaticValues.disk_id = item2.disk_id;
          StaticValues.album_id = StudioValues.album_id;
          StaticValues.disk_name = item2.disk_name;

          StaticValues.nework_check = 0;
          Intent i = new Intent(AuthCheckActivity.this, ActivityStartLoading.class);
          i.putExtra(ActivityStartLoading.PARAM_NEXT_ACTIVITY, PagerMainActivity.class);
          i.putExtra(ActivityStartLoading.PARAM_STUDIO_NO, StudioValues.STUDIO_ID);
          i.putExtra(ActivityStartLoading.PARAM_DEFAULT_ALBUM_NO, StudioValues.DEFAULT_ALBUM_ID);
          i.putExtra(ActivityStartLoading.PARAM_MUSIC_ID, StudioValues.MOBILE_MUSIC_ID);
          startActivity(i);
          finish();
        } else {
          StaticValues.nework_check = 100;
          new NETgetUserInfo(MY_ID, afterGetUserInfo);
        }
      } else {
        new AlertDialog.Builder(AuthCheckActivity.this)
            .setMessage("업데이트가 필요합니다")
            .setCancelable(false)
            .setNeutralButton("확인",
                new DialogInterface.OnClickListener() {
                  public void onClick(DialogInterface dialog, int id) {
//                    Intent intent = new Intent(
//                        Intent.ACTION_VIEW, Uri.parse(Value.getAlbumStoreUrl()));
//                    startActivity(intent);
//                    finish();
                  }
                }).show();
      }
    }

    @SuppressLint("HandlerLeak")
    private final Handler afterGetUserInfo = new Handler() {
      @Override
      public void handleMessage(Message msg) {
        if (msg.what == -1) {
          Toast.makeText(AuthCheckActivity.this, "Network Error", Toast.LENGTH_SHORT).show();
        } else {
          try {
            final JSONObject obj = new JSONObject(msg.obj.toString());
            final VOUser me = new VOUser(
                obj.getInt("user_id"),
                obj.getString("user_name"),
                obj.getInt("album_id"),
                obj.getString("user_phone_number"),
                obj.getString("user_appver"),
                obj.getString("user_uuid"),
                obj.getString("user_regid"),
                obj.getInt("user_level")
            );

            CommonUtils.setMyID(AuthCheckActivity.this, me.user_id);
            StaticValues.myInfo = me;
            sqlDAO.insertUser(StaticValues.myInfo);
          } catch (JSONException e) {
            Log.i(Static.LOG_TAG, e.toString());
          }

          new SearchAlbumTask(AuthCheckActivity.this, StudioValues.album_id).execute();
        }
      }
    };
  }

  class SearchAlbumTask extends AsyncTask<Void, Void, Integer> {
    Context mContext;
    String url;
    ProgressDialog pDlg;
    ArrayList<VOdisk> mCoupleResultArray;

    public SearchAlbumTask(Context context, int albumId) {
      mContext = context;
      mCoupleResultArray = new ArrayList<VOdisk>();
      pDlg = new ProgressDialog(mContext);
      pDlg.setCancelable(false);
      url = AlbumUtils.getServiceUrl(mContext) + "getMusicAlbumList.php?album_id=" + albumId;
    }

    @Override
    protected void onPreExecute() {
      pDlg.show();
    }

    @Override
    protected Integer doInBackground(Void... params) {
      try {
        JSONArray jResultArray = Network.getInstance().getJsonArrayFromUrl(mContext, url);
        for (int i = 0; i < jResultArray.length(); i++) {
          JSONObject jCouple = jResultArray.getJSONObject(i);
          VOdisk couple = new VOdisk(jCouple.getInt("disk_id"), jCouple.getString("disk_name"),
              jCouple.getInt("album_id")
          );
          mCoupleResultArray.add(couple);
        }
      } catch (ClientProtocolException e) {
        e.printStackTrace();
      } catch (IllegalStateException e) {
        e.printStackTrace();
      } catch (IOException e) {
        e.printStackTrace();
      } catch (JSONException e) {
        e.printStackTrace();
      }
      return null;
    }

    @Override
    protected void onPostExecute(Integer result) {
      pDlg.dismiss();
      if (mCoupleResultArray.size() > 0) {
        StaticValues.diskList = mCoupleResultArray;
        /////////

        DAOSqlite.getInstance(AuthCheckActivity.this).insertDiskList(StaticValues.diskList);
        VOdisk item2 = StaticValues.diskList.get(0);
        DataNetUtils.setSelectedCoupleId(AuthCheckActivity.this, item2.disk_id);
        Log.d("hyuk>>", "cid>>" + item2.disk_id);
        StudioValues.MOBILE_MUSIC_ID = item2.disk_id;
        StudioValues.disk_id = item2.disk_id;

        StaticValues.disk_id = item2.disk_id;
        StaticValues.album_id = StudioValues.album_id;

        StaticValues.disk_name = item2.disk_name;

        Intent i = new Intent(AuthCheckActivity.this, ActivityStartLoading.class);
        i.putExtra(ActivityStartLoading.PARAM_NEXT_ACTIVITY, PagerMainActivity.class);
        i.putExtra(ActivityStartLoading.PARAM_STUDIO_NO, StudioValues.STUDIO_ID);
        i.putExtra(ActivityStartLoading.PARAM_DEFAULT_ALBUM_NO, StudioValues.DEFAULT_ALBUM_ID);
        i.putExtra(ActivityStartLoading.PARAM_MUSIC_ID, StudioValues.MOBILE_MUSIC_ID);
        startActivity(i);
        finish();
      } else {
        Toast.makeText(getApplicationContext(), "해당 앨범을 찾을 수 없습니다", Toast.LENGTH_LONG).show();
      }
      Log.i("bitlworks", "search result : " + mCoupleResultArray.size());
    }
  }
}
