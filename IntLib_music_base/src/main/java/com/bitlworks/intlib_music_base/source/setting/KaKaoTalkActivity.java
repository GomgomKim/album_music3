package com.bitlworks.intlib_music_base.source.setting;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bitlworks.intlib_music_base.R;
import com.bitlworks.intlib_music_base.StaticValues;
import com.bitlworks.music_resource.AlbumValue;
import com.kakao.kakaolink.KakaoLink;
import com.kakao.kakaolink.KakaoTalkLinkMessageBuilder;
import com.kakao.util.KakaoParameterException;
import com.squareup.picasso.Picasso;

public class KaKaoTalkActivity extends Activity {

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.katalk_input_layout);

    findViewById(R.id.view_back).setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        finish();
      }
    });

    final EditText edtName = (EditText) findViewById(R.id.edt_new_hello);
    TextView inviteText = (TextView) findViewById(R.id.text_invite);
    inviteText.setText(StaticValues.album.album_invitemsg);

    final ImageView imagePhoto = (ImageView) findViewById(R.id.iv_photo);
    String imageSrc = "http://music.bitlworks.co.kr/mobilemusic/image_home/"
        + StaticValues.album.album_id
        + "/metadata/"
        + StaticValues.metadata.main_image;
    Picasso.with(this)
        .load(imageSrc)
        .into(imagePhoto);

    findViewById(R.id.image_kakao_send).setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        final String nick = edtName.getText().toString().trim().replace("'", "");
        if (nick.length() < 3) {
          Toast.makeText(KaKaoTalkActivity.this, "문자는 3자 이상이여야 합니다",
              Toast.LENGTH_SHORT).show();
          return;
        }

        try {
          KakaoLink kakaoLink = KakaoLink.getKakaoLink(KaKaoTalkActivity.this);
          KakaoTalkLinkMessageBuilder kakaoTalkLinkMessageBuilder = kakaoLink.createKakaoTalkLinkMessageBuilder();
          kakaoTalkLinkMessageBuilder.addText("[" + StaticValues.album.album_name + "]\n\n" + nick + "\n\n");

          int ww = imagePhoto.getMeasuredWidth();
          int hh = imagePhoto.getMeasuredHeight();

          Long tsLong = System.currentTimeMillis() / 1000;
          String ts = tsLong.toString();

          String imageSrc = "http://music.bitlworks.co.kr/mobilemusic/image_home/"
              + StaticValues.album.album_id
              + "/metadata/"
              + StaticValues.metadata.main_image
              + "?timestamp=" + ts;
          int width = 300;
          int height = width * hh / ww;
          kakaoTalkLinkMessageBuilder.addImage(imageSrc, width, height);
          kakaoTalkLinkMessageBuilder.addWebButton("" + StaticValues.album.album_name, AlbumValue.music_url);
          kakaoLink.sendMessage(kakaoTalkLinkMessageBuilder, KaKaoTalkActivity.this);
          finish();
        } catch (KakaoParameterException e) {
          Log.d("hyuk-->", "error>>" + e.getMessage());
        }
      }
    });
  }
}
