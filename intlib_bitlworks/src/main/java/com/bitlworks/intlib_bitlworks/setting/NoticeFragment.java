package com.bitlworks.intlib_bitlworks.setting;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

import com.bitlworks.intlib_bitlworks.R;

public class NoticeFragment extends Fragment {

  public static Fragment newInstance() {
    NoticeFragment fragment = new NoticeFragment();
    return fragment;
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_notice, container, false);
    // TODO: 비틀웍스만의 notice 만들기.
    WebView noticeWebView = (WebView) view.findViewById(R.id.webview_notice);
//    agreementWebView1.loadUrl("http://wedding.bitlworks.co.kr/wedding/agreement1.html");
    return view;
  }
}
