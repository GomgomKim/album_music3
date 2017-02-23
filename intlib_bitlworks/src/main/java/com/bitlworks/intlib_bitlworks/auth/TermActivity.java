package com.bitlworks.intlib_bitlworks.auth;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.bitlworks.intlib_bitlworks.R;


public class TermActivity extends Activity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_term);
    WebView agreementWebView1 = (WebView) findViewById(R.id.webview_agreement1);
    agreementWebView1.loadUrl("http://wedding.bitlworks.co.kr/wedding/agreement1.html");
    WebView agreementWebView2 = (WebView) findViewById(R.id.webview_agreement2);
    agreementWebView2.loadUrl("http://wedding.bitlworks.co.kr/wedding/agreement2.html");

    findViewById(R.id.btn_ok).setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View v) {
        finish();
      }
    });
  }

}