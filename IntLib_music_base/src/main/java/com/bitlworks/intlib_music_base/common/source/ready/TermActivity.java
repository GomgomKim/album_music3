package com.bitlworks.intlib_music_base.common.source.ready;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.bitlworks.intlib_music_base.R;

public class TermActivity extends Activity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_term);
    WebView webView = (WebView) findViewById(R.id.webview);
    webView.setWebViewClient(new WebViewClient());
    webView.getSettings().setUseWideViewPort(true);
    webView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
    webView.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);
    webView.loadUrl("http://studiogallery.bitlworks.co.kr/term.htm");

    findViewById(R.id.btn_ok).setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View v) {
        finish();
      }
    });
  }

}