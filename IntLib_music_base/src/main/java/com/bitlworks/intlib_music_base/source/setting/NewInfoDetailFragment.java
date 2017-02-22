package com.bitlworks.intlib_music_base.source.setting;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

import com.bitlworks.intlib_music_base.R;
import com.bitlworks.intlib_music_base.data.VONewInfo;


public class NewInfoDetailFragment extends Fragment {

  private VONewInfo newInfo;

  public static Fragment newInstance(VONewInfo item) {
    NewInfoFragment fragment = new NewInfoFragment();
    Bundle bundle = new Bundle();
    bundle.putSerializable("NEWINFO", item);
    fragment.setArguments(bundle);
    return fragment;
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    newInfo = (VONewInfo) getArguments().getSerializable("NEWINFO");
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_newinfo_detail, container, false);
    WebView webView = (WebView) view.findViewById(R.id.webview);
    webView.loadUrl(newInfo.link_url);
    return view;
  }
}
