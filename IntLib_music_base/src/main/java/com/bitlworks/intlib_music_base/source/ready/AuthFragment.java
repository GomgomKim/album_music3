package com.bitlworks.intlib_music_base.source.ready;

import android.app.Activity;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bitlworks.intlib_music_base.R;
import com.bitlworks.intlib_music_base.MusicClient;
import com.bitlworks.intlib_music_base.StaticValues;
import com.bitlworks.intlib_music_base.data.DAOSqlite;
import com.bitlworks.intlib_music_base.data.DataNetUtils;
import com.bitlworks.intlib_music_base.data.VOUser;
import com.google.gson.JsonObject;
import com.ucom.intlib_bitlworks.CommonUtils;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AuthFragment extends Fragment {

  private DAOSqlite sqlDAO;
  private AuthListener listener;

  public static Fragment newInstance() {
    AuthFragment fragment = new AuthFragment();
    return fragment;
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    sqlDAO = DAOSqlite.getInstance(getActivity());
    if (!DataNetUtils.isNetworkConnect(getActivity())) {
      startOfflineMode();
      return;
    }

    String uuid = CommonUtils.getDevicesUUID(getActivity());
    String appver = String.valueOf(CommonUtils.getAppVersion(getActivity()));
    String regID = CommonUtils.registerGCMid(getActivity(), uuid, appver);
    updateRegid(uuid, appver, regID);
    // TODO: AppVersion 확인 후 다운로드받게 하기
//    new AlertDialog.Builder(AuthCheckActivity.this)
//        .setMessage("업데이트가 필요합니다")
//        .setCancelable(false)
//        .setNeutralButton("확인",
//            new DialogInterface.OnClickListener() {
//              public void onClick(DialogInterface dialog, int id) {
//                    Intent intent = new Intent(
//                        Intent.ACTION_VIEW, Uri.parse(Value.getAlbumStoreUrl()));
//                    startActivity(intent);
//                    finish();
//              }
//            }).show();
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
    return inflater.inflate(R.layout.fragment_authcheck, container, false);
  }

  private void startOfflineMode() {
    StaticValues.user = sqlDAO.getUser();
    StaticValues.album = sqlDAO.getalbum();
    StaticValues.diskList = sqlDAO.getdiskList();
    StaticValues.selectedDisk = StaticValues.diskList.get(0);
    DataNetUtils.setSelectedDiskId(getActivity(), StaticValues.selectedDisk.disk_id);

    listener.finishAuth();
  }

  private void updateRegid(String uuid, String appver, String regId) {
    final ProgressDialog progressDialog = new ProgressDialog(getActivity());
    progressDialog.setCancelable(false);
    progressDialog.show();

    Call<JsonObject> call = MusicClient.getInstance().getService().updateRegid(CommonUtils.getMyID(getActivity()), uuid, appver, regId);
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

        CommonUtils.setMyID(getActivity(), user.user_id);
        StaticValues.user = user;
        sqlDAO.insertUser(StaticValues.user);

        listener.finishAuth();
      }

      @Override
      public void onFailure(Call<JsonObject> call, Throwable t) {
        Log.e("onFailure", t.getMessage());
      }
    });
  }

  @Override
  public void onAttach(Activity activity) {
    super.onAttach(activity);
    if (activity instanceof AuthListener) {
      listener = (AuthListener) activity;
    } else {
      throw new RuntimeException(activity.toString()
          + " must implement OnFragmentInteractionListener");
    }
  }

  @Override
  public void onAttach(Context context) {
    super.onAttach(context);
    if (context instanceof AuthListener) {
      listener = (AuthListener) context;
    } else {
      throw new RuntimeException(context.toString()
          + " must implement OnFragmentInteractionListener");
    }
  }

  @Override
  public void onDetach() {
    super.onDetach();
    listener = null;
  }

  public interface AuthListener {
    void finishAuth();
  }
}
