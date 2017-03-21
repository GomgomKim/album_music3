package com.bitlworks.intlib_music_base.source.setting;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.bitlworks.intlib_music_base.R;
import com.bitlworks.intlib_music_base.StaticValues;
import com.bitlworks.intlib_music_base.data.VONewInfo;


public class NewInfoFragment extends Fragment {

  private Listener listener;
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

    // TODO: NewInfo 추가/삭제 기능 만들기

    ListView newInfoListView = (ListView) view.findViewById(com.bitlworks.intlib_music_base.R.id.list_new_info);
    adapter = new NewInfoAdapter(getActivity(), StaticValues.newInfos);
    newInfoListView.setAdapter(adapter);
    newInfoListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
      @Override
      public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        listener.clickNewInfo(StaticValues.newInfos.get(i));
      }
    });
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

}
