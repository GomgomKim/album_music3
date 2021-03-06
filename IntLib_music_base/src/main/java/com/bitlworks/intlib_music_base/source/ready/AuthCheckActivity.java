package com.bitlworks.intlib_music_base.source.ready;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.bitlworks.intlib_music_base.R;
import com.bitlworks.intlib_music_base.MusicClient;
import com.bitlworks.intlib_music_base.StaticValues;
import com.bitlworks.intlib_music_base.data.DAOSqlite;
import com.bitlworks.intlib_music_base.data.VOUser;
import com.bitlworks.music_resource.AlbumValue;
import com.google.gson.JsonObject;
import com.bitlworks.intlib_bitlworks.CommonUtils;
import com.bitlworks.intlib_bitlworks.auth.RegisterFragment;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AuthCheckActivity extends Activity implements
    RegisterFragment.Listener,
    AuthFragment.AuthListener {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_authcheck);
    new TedPermission(this)
        .setPermissionListener(new PermissionListener() {
          @Override
          public void onPermissionGranted() {
          }

          @Override
          public void onPermissionDenied(ArrayList<String> deniedPermissions) {
            new AlertDialog.Builder(AuthCheckActivity.this)
                .setTitle("앱 종료")
                .setMessage("권한이 설정되지 않아 앱을 종료합니다.")
                .setPositiveButton("네", new DialogInterface.OnClickListener() {
                  @Override
                  public void onClick(DialogInterface dialog, int which) {
                    AuthCheckActivity.this.finish();
                  }
                })
                .create().show();
          }
        })
        .setDeniedMessage("권한 거부 시 앱을 사용할 수 없습니다.\n\n권한을 변경해주세요 [Setting] > [Permission]")
        .setPermissions(Manifest.permission.RECEIVE_SMS, Manifest.permission.SEND_SMS, Manifest.permission.READ_PHONE_STATE)
        .check();

    int userId = CommonUtils.getMyID(AuthCheckActivity.this);
    if (userId < 1) {
      FragmentManager fragmentManager = getFragmentManager();
      FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
      fragmentTransaction.replace(R.id.view_frame, RegisterFragment.newInstance());
      fragmentTransaction.commit();
      return;
    }

    FragmentManager fragmentManager = getFragmentManager();
    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
    fragmentTransaction.replace(R.id.view_frame, AuthFragment.newInstance());
    fragmentTransaction.commit();
  }


  @Override
  public void insertUser(String mobile) {
    final ProgressDialog progressDialog = new ProgressDialog(this);
    progressDialog.setCancelable(false);
    progressDialog.show();

    Call<JsonObject> call = MusicClient.getInstance().getService().insertUser(AlbumValue.album_id, mobile);
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
        DAOSqlite.getInstance(AuthCheckActivity.this).insertUser(StaticValues.user);
      }

      @Override
      public void onFailure(Call<JsonObject> call, Throwable t) {
        Log.e("onFailure", t.getMessage());
      }
    });
  }

  @Override
  public void updateName(final String name) {
    final ProgressDialog progressDialog = new ProgressDialog(this);
    progressDialog.setCancelable(false);
    progressDialog.show();

    Call<JsonObject> call = MusicClient.getInstance().getService().updateNick(AlbumValue.album_id, StaticValues.user.user_id, name);
    call.enqueue(new Callback<JsonObject>() {
      @Override
      public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
        progressDialog.dismiss();
        StaticValues.user.user_name = name;
        Intent i = new Intent(AuthCheckActivity.this, AuthCheckActivity.class);
        startActivity(i);
        finish();
      }

      @Override
      public void onFailure(Call<JsonObject> call, Throwable t) {
        Log.e("onFailure", t.getMessage());
      }
    });
  }

  @Override
  public void finishAuth() {
    Intent intent = new Intent(this, LoadingActivity.class);
    startActivity(intent);
    finish();
  }
}
