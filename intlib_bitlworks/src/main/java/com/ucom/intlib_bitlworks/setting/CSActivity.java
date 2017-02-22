package com.ucom.intlib_bitlworks.setting;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.TextView;

import com.ucom.intlib_bitlworks.CommonUtils;
import com.ucom.intlib_bitlworks.R;

public class CSActivity extends Activity {
  TextView currentVersionText, newVersionText;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_cs);

    findViewById(R.id.view_actionbar).setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        finish();
      }
    });

    currentVersionText = (TextView) findViewById(R.id.text_current_version);
    String version = CommonUtils.getAppVersionName(this);
    currentVersionText.setText(version);
    newVersionText = (TextView) findViewById(R.id.text_new_version);
    newVersionText.setText(CommonUtils.getLatestInstallableVersion(this));

    findViewById(R.id.view_term).setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        // TODO: TermsActivity 연결
//				startActivity(new Intent(CSActivity.this, TermsActivity.class));
      }
    });

    findViewById(R.id.view_notice).setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        // TODO: CSNoticeActivity 연결
//				if (ivNewNotice.getVisibility() == View.VISIBLE) {
//					ivNewNotice.setVisibility(View.INVISIBLE);
//				}
//				AlbumUtils.setLatestTouchedNoticeTime(CSActivity.this,
//						new Date().getTime());
//				Intent i = new Intent(CSActivity.this, CSNoticeActivity.class);
//				i.putExtra(BaseActivity.EXTRA_NAME_PLAY_MUSIC, isAlbum);
//				i.putExtra(CSNoticeActivity.PARAM_IS_ALBUM, isAlbum);
//				startActivity(i);
      }
    });

    findViewById(R.id.view_report).setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        // TODO: CSWriteActivity 연결
//				Intent i = new Intent(CSActivity.this, CSWriteActivity.class);
//				i.putExtra(BaseActivity.EXTRA_NAME_PLAY_MUSIC, isAlbum);
//				i.putExtra(CSWriteActivity.PARAM_IS_ALBUM, isAlbum);
//				i.putExtra(CSWriteActivity.PARAM_TYPE,
//						CSWriteActivity.TYPE_BUG_REPORT_AND_PARTNERSHIP);
//				startActivity(i);
      }
    });

    findViewById(R.id.text_call).setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        Intent i = new Intent(Intent.ACTION_DIAL);
        i.setData(Uri.parse("tel:" + getString(R.string.cs_call)));
        startActivity(i);
      }
    });

    findViewById(R.id.text_email).setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        new AlertDialog.Builder(CSActivity.this).setMessage("이메일을 전송하시겠습니까?")
            .setPositiveButton("예", new DialogInterface.OnClickListener() {
              @Override
              public void onClick(DialogInterface dialog, int which) {

              }
            }).setNegativeButton("아니오", new DialogInterface.OnClickListener() {
          @Override
          public void onClick(DialogInterface dialog, int which) {
            Intent email = new Intent(Intent.ACTION_SEND);
            email.putExtra(Intent.EXTRA_EMAIL, new String[]{getString(R.string.cs_email)});
            email.setType("message/rfc822");
            startActivity(Intent.createChooser(email, "Choose an Email client :"));
          }
        }).show();
      }
    });
  }
}
