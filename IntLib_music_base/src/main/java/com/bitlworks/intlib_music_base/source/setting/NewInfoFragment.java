package com.bitlworks.intlib_music_base.source.setting;

import android.app.Activity;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.bitlworks.intlib_music_base.MusicClient;
import com.bitlworks.intlib_music_base.R;
import com.bitlworks.intlib_music_base.StaticValues;
import com.bitlworks.intlib_music_base.data.DAOSqlite;
import com.bitlworks.intlib_music_base.data.VONewInfo;
import com.bitlworks.intlib_music_base.source.ready.LoadingActivity;
import com.bitlworks.music_resource_hanyang.AlbumValue;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class NewInfoFragment extends Fragment {

  private Listener listener;
  private ArrayList<VONewInfo> newInfos = new ArrayList<>();
  private NewInfoAdapter adapter;

  public static Fragment newInstance() {
    NewInfoFragment fragment = new NewInfoFragment();
    return fragment;
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_new_info, container, false);

    // TODO: NewInfo 추가기능 만들기

    ListView newInfoListView = (ListView) view.findViewById(com.bitlworks.intlib_music_base.R.id.list_new_info);
    adapter = new NewInfoAdapter(getActivity(), newInfos);
    newInfoListView.setAdapter(adapter);
    newInfoListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
      @Override
      public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        listener.clickNewInfo(newInfos.get(i));
      }
    });

    getNewInfos();
    return view;
  }

  @Override
  public void onAttach(Activity activity) {
    super.onAttach(activity);
    if (activity instanceof Listener) {
      listener = (Listener) activity;
    } else {
      throw new RuntimeException(activity.toString()
          + " must implement OnFragmentInteractionListener");
    }
  }

  @Override
  public void onAttach(Context context) {
    super.onAttach(context);
    if (context instanceof Listener) {
      listener = (Listener) context;
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

  public interface Listener {
    void clickNewInfo(VONewInfo item);
  }

  private void getNewInfos() {
    final ProgressDialog progressDialog = new ProgressDialog(getActivity());
    progressDialog.setCancelable(false);
    progressDialog.show();

    Call<JsonArray> call = MusicClient.getInstance().getService().getNewInfos(AlbumValue.album_id);
    call.enqueue(
        new Callback<JsonArray>() {
          @Override
          public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
            progressDialog.dismiss();
            ArrayList<VONewInfo> newInfos = new ArrayList<>();
            JsonArray array = response.body().getAsJsonArray();
            for (JsonElement object : array) {
              VONewInfo info = new VONewInfo(
                  object.getAsJsonObject().get("info_id").getAsInt(),
                  object.getAsJsonObject().get("album_id").getAsInt(),
                  object.getAsJsonObject().get("main_subject").getAsString(),
                  object.getAsJsonObject().get("time").getAsString(),
                  object.getAsJsonObject().get("image_data").getAsString(),
                  object.getAsJsonObject().get("contents").getAsString(),
                  object.getAsJsonObject().get("link_url").getAsString());
              newInfos.add(info);
            }

            newInfos.addAll(newInfos);
            DAOSqlite.getInstance(getActivity()).insertnewInfoList(newInfos);
            adapter.notifyDataSetChanged();
          }

          @Override
          public void onFailure(Call<JsonArray> call, Throwable t) {
            progressDialog.dismiss();
            Log.e("onFailure", t.getMessage());
          }
        }
    );
  }
}
