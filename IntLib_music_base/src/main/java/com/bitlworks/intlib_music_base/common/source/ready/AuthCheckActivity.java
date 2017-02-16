package com.bitlworks.intlib_music_base.common.source.ready;

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
import android.util.Log;
import android.widget.Toast;

import com.bitlworks.intlib_music_base.R;
import com.bitlworks.intlib_music_base.common.AlbumUtils;
import com.bitlworks.intlib_music_base.common.CommonUtils;
import com.bitlworks.intlib_music_base.common.MusicClient;
import com.bitlworks.intlib_music_base.common.StaticValues;
import com.bitlworks.intlib_music_base.common.data.DAOSqlite;
import com.bitlworks.intlib_music_base.common.data.DataNetUtils;
import com.bitlworks.intlib_music_base.common.data.VOAlbum;
import com.bitlworks.intlib_music_base.common.data.VOUser;
import com.bitlworks.intlib_music_base.common.data.VOdisk;
import com.bitlworks.intlib_music_base.common.gcm.GcmRegistration;
import com.bitlworks.intlib_music_base.common.source.PagerMainActivity;
import com.bitlworks.music_resource_hanyang.AlbumValue;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AuthCheckActivity extends Activity {

  Handler handler;
  DAOSqlite sqlDAO;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_authcheck);

    Intent intent = getIntent();
    Bundle bundle = intent.getExtras();
    if (bundle != null) {
      finish();
      System.exit(0);
      Boolean j = (Boolean) bundle.get("exit");
      if (j) {
        finish();
        Log.d("Main Activity hyuk", "check is true");
      } else {
        Log.d("Main Activity hyuk", "check is false");
      }
    }

    if (StaticValues.nework_check == 100) {
      checkGCMRegID();
    }

    sqlDAO = DAOSqlite.getInstance(this);

    handler = new Handler();
    handler.postDelayed(versionCheckRunnable, 1000);
  }

  private void checkGCMRegID() {
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
    GcmRegistration gr = new GcmRegistration(AuthCheckActivity.this);
    String regId = gr.registerGCM(uuid, appver);
    updateRegid(uuid, appver, regId);
  }

  private void updateRegid(String uuid, String appver, String regId) {
    final ProgressDialog progressDialog = new ProgressDialog(AuthCheckActivity.this);
    progressDialog.setCancelable(false);
    progressDialog.show();

    Call<JsonObject> call = MusicClient.getInstance().getService().updateRegid(StaticValues.user.user_id, uuid, appver, regId);
    call.enqueue(new Callback<JsonObject>() {
      @Override
      public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
        progressDialog.dismiss();
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

        CommonUtils.setMyID(AuthCheckActivity.this, user.user_id);
        StaticValues.user = user;
        sqlDAO.insertUser(StaticValues.user);
      }

      @Override
      public void onFailure(Call<JsonObject> call, Throwable t) {
        Log.e("onFailure", t.getMessage());
      }
    });
  }

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
      if (AlbumValue.IS_SAMPLE_PROJECT) {
        AlbumUtils.createAuth(AuthCheckActivity.this, "Y", "0123456789");
      }
      return 1;
    }

    @Override
    protected void onPostExecute(Integer result) {
      if (AlbumValue.IS_SAMPLE_PROJECT || result > 0) {

        final int userId = CommonUtils.getMyID(AuthCheckActivity.this);
        if (userId < 1) {
          Intent intent = new Intent(AuthCheckActivity.this, RegisterActivity.class);
          startActivity(intent);
          finish();
          return;
        }

        if (DataNetUtils.isNetworkConnect(AuthCheckActivity.this)) {
          StaticValues.nework_check = 100;
          getUser(userId);
          return;
        }

        StaticValues.user = sqlDAO.getUser();
        StaticValues.album = sqlDAO.getalbum();
        StaticValues.diskList = sqlDAO.getdiskList();
        StaticValues.selectedDisk = StaticValues.diskList.get(0);
        DataNetUtils.setSelectedDiskId(AuthCheckActivity.this, StaticValues.selectedDisk.disk_id);
        StaticValues.nework_check = 0;
        Intent i = new Intent(AuthCheckActivity.this, LoadingActivity.class);
        i.putExtra(LoadingActivity.PARAM_NEXT_ACTIVITY, PagerMainActivity.class);
        i.putExtra(LoadingActivity.PARAM_STUDIO_NO, AlbumValue.STUDIO_ID);
        i.putExtra(LoadingActivity.PARAM_DEFAULT_ALBUM_NO, AlbumValue.DEFAULT_ALBUM_ID);
        i.putExtra(LoadingActivity.PARAM_MUSIC_ID, AlbumValue.disk_id);
        startActivity(i);
        finish();

        return;

      }
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

    private void getUser(int userId) {
      final ProgressDialog progressDialog = new ProgressDialog(AuthCheckActivity.this);
      progressDialog.setCancelable(false);
      progressDialog.show();

      Call<JsonObject> call = MusicClient.getInstance().getService().getUser(userId);
      call.enqueue(new Callback<JsonObject>() {
        @Override
        public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
          progressDialog.dismiss();
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

          CommonUtils.setMyID(AuthCheckActivity.this, user.user_id);
          StaticValues.user = user;
          sqlDAO.insertUser(StaticValues.user);
          getAlbum();
        }

        @Override
        public void onFailure(Call<JsonObject> call, Throwable t) {
          Log.e("onFailure", t.getMessage());
        }
      });
    }

    private void getAlbum() {
      final ProgressDialog progressDialog = new ProgressDialog(AuthCheckActivity.this);
      progressDialog.setCancelable(false);
      progressDialog.show();

      Call<JsonObject> call = MusicClient.getInstance().getService().getAlbum(AlbumValue.album_id);
      call.enqueue(
          new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
              progressDialog.dismiss();
              JsonObject object = response.body().getAsJsonObject();
              VOAlbum album = new VOAlbum(
                  object.get("album_id").getAsInt(),
                  object.get("album_name").getAsString(),
                  object.get("singer_id_list").getAsString(),
                  object.get("album_type").getAsString(),
                  object.get("album_genre").getAsString(),
                  object.get("album_company").getAsString(),
                  object.get("album_intro").getAsString(),
                  object.get("album_time").getAsString(),
                  object.get("album_invitemsg").getAsString(),
                  object.get("album_inviteurl").getAsString());
              StaticValues.album = album;
              sqlDAO.insertalbum(StaticValues.album);
              getDisks();
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
              Log.e("onFailure", t.getMessage());
            }
          }
      );
    }


    private void getDisks() {
      final ProgressDialog progressDialog = new ProgressDialog(AuthCheckActivity.this);
      progressDialog.setCancelable(false);
      progressDialog.show();

      Call<JsonArray> call = MusicClient.getInstance().getService().getDisks(AlbumValue.album_id);
      call.enqueue(
          new Callback<JsonArray>() {
            @Override
            public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
              progressDialog.dismiss();
              ArrayList<VOdisk> disks = new ArrayList<>();
              JsonArray array = response.body().getAsJsonArray();
              for (JsonElement object : array) {
                VOdisk disk = new VOdisk(
                    object.getAsJsonObject().get("disk_id").getAsInt(),
                    object.getAsJsonObject().get("disk_name").getAsString(),
                    object.getAsJsonObject().get("album_id").getAsInt());
                disks.add(disk);
              }

              if (array.size() == 0) {
                Toast.makeText(
                    getApplicationContext(), "해당 앨범을 찾을 수 없습니다", Toast.LENGTH_LONG).show();
                return;
              }

              StaticValues.diskList.addAll(disks);
              StaticValues.selectedDisk = disks.get(0);
              sqlDAO.insertDiskList(StaticValues.diskList);
              DataNetUtils.setSelectedDiskId(AuthCheckActivity.this, StaticValues.selectedDisk.disk_id);

              Intent intent = new Intent(AuthCheckActivity.this, LoadingActivity.class);
              startActivity(intent);
              finish();
            }

            @Override
            public void onFailure(Call<JsonArray> call, Throwable t) {
              Log.e("onFailure", t.getMessage());
            }
          }
      );
    }
  }
}
