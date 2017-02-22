package com.bitlworks.intlib_bitlworks.setting;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

import com.bitlworks.intlib_bitlworks.R;

public class TermFragment extends Fragment {

  public static Fragment newInstance() {
    TermFragment fragment = new TermFragment();
    return fragment;
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_term, container, false);
    WebView agreementWebView1 = (WebView) view.findViewById(R.id.webview_agreement1);
    agreementWebView1.loadUrl("http://wedding.bitlworks.co.kr/wedding/agreement1.html");
    WebView agreementWebView2 = (WebView) view.findViewById(R.id.webview_agreement2);
    agreementWebView2.loadUrl("http://wedding.bitlworks.co.kr/wedding/agreement2.html");
    return view;
  }
}
