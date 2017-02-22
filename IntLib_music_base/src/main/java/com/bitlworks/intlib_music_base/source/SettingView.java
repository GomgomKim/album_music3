package com.bitlworks.intlib_music_base.source;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bitlworks.intlib_music_base.R;
import com.bitlworks.intlib_music_base.StaticValues;
import com.ucom.intlib_bitlworks.setting.CSActivity;
import com.ucom.intlib_bitlworks.setting.ProInviteDialog;

public class SettingView extends LinearLayout {

  public SettingView(Context context) {
    super(context);
    LayoutInflater li = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    View v = li.inflate(R.layout.layout_setting, this, true);

    TextView newInfoText = (TextView) v.findViewById(R.id.text_new_info);
    newInfoText.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View view) {
        Intent i = new Intent(getContext(), NewInfoActivity.class);
        i.putExtra("NEW_INFO_LIST", StaticValues.newinfoList);
        getContext().startActivity(i);
      }
    });

    TextView inviteText = (TextView) v.findViewById(R.id.text_invite);
    inviteText.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View view) {
        ProInviteDialog dialog = new ProInviteDialog(getContext());
        dialog.show();
      }
    });

    TextView customerCenterText = (TextView) v.findViewById(R.id.text_customer_center);
    customerCenterText.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View view) {
        Intent i = new Intent(getContext(), CSActivity.class);
        getContext().startActivity(i);
      }
    });
  }

  private void initView() {
//    TextView album_time = (TextView) findViewById(R.id.album_time);
////    album_time.setTypeface(tf);
//    album_time.setText(StaticValues.album.album_time);
//
//    TextView album_type_c = (TextView) findViewById(R.id.album_type_c);
////    album_type_c.setTypeface(tf);
//    album_type_c.setText(StaticValues.album.album_type);
//
//    TextView album_type = (TextView) findViewById(R.id.album_type);
////    album_type.setTypeface(tf);
//    album_type.setText("앨범유형");
//
//    TextView album_song_type_c = (TextView) findViewById(R.id.album_song_type_c);
////    album_song_type_c.setTypeface(tf);
//    album_song_type_c.setText(StaticValues.album.album_genre);
//
//    TextView album_song_type = (TextView) findViewById(R.id.album_song_type);
////    album_song_type.setTypeface(tf);
//    album_song_type.setText("장르");
//
//    TextView album_company_type_c = (TextView) findViewById(R.id.album_company_type_c);
////    album_company_type_c.setTypeface(tf);
//    album_company_type_c.setText(StaticValues.album.album_company);
//
//    TextView album_company_type = (TextView) findViewById(R.id.album_company_type);
////    album_company_type.setTypeface(tf);
//    album_company_type.setText("발매사");
//
//    TextView album_info_type = (TextView) findViewById(R.id.album_info_type);
////    album_info_type.setTypeface(tf);
//    album_info_type.setText("앨범소개");
//
//    // 앨범 소개 글
//    TextView albumIntroText = (TextView) findViewById(R.id.text_album_intro);
////    albumIntroText.setTypeface(ff);
//    albumIntroText.setText(StaticValues.album.album_intro);

  }
}
