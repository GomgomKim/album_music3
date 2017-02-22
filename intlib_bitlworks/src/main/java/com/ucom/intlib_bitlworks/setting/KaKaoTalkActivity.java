package com.ucom.intlib_bitlworks.setting;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bitlworks.WeddingAlbum.DataNet.ImagesDownloader;
import com.bitlworks.WeddingAlbum.DataNet.StaticValues;
import com.bitlworks.WeddingAlbum.DataNet.VOCouple;
import com.bitlworks.music.R;
import com.bitlworks.wedding.resources.StudioValues;
import com.kakao.kakaolink.KakaoLink;
import com.kakao.kakaolink.KakaoTalkLinkMessageBuilder;
import com.kakao.util.KakaoParameterException;

public class KaKaoTalkActivity extends Activity {

  private KakaoLink kakaoLink;
  // private KakaoDialogSpinner text, link, image, button;
  // private CheckBox forwardable;
  private KakaoTalkLinkMessageBuilder kakaoTalkLinkMessageBuilder;

  private int mAlbumId;

  public String msg;
  public static final String EXTRA_ALBUM_ID = "album";


  String message;
  private Context CONTEXT;
  EditText edtName;

  ImageView imagePhoto;

  String image_file;
  /**
   * ATTENTION: This was auto-generated to implement the App Indexing API.
   * See https://g.co/AppIndexing/AndroidStudio for more information.
   */
  //private GoogleApiClient client;

  int menu_unit;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.katalk_input_layout);
    CONTEXT = this;

    int w = getResources().getDisplayMetrics().widthPixels;
    menu_unit = w / 5;
    findViewById(R.id.imageView1).setOnTouchListener(new View.OnTouchListener() {

      @Override
      public boolean onTouch(View v, MotionEvent event) {
        // TODO Auto-generated method stub

        if (event.getAction() == MotionEvent.ACTION_DOWN) {
          Log.d("TOUCH EVENT", "x>>" + event.getX() + "   y>>" + event.getY());
          Log.d("Touch unit,,,,", "menu unit>>" + menu_unit);
          if (event.getX() > 0 && event.getX() < menu_unit) {  // menu 1
            finish();
          }
        }
        return true;
      }
    });

    edtName = (EditText) findViewById(R.id.edt_new_hello);

    TextView inviteText = (TextView) findViewById(R.id.text_invite);
    inviteText.setText(StaticValues.album.album_invitemsg);

    imagePhoto = (ImageView) findViewById(R.id.iv_photo);
    String in_file = "";
    for (int i = 0; i < StaticValues.photoList.size(); i++) {
      if (StaticValues.photoList.get(i).type == 3 && StaticValues.photoList.get(i).photo_order == 100) {
        in_file = StaticValues.photoList.get(i).photo_file_name;
        break;
      }
    }
    image_file = in_file;

    String imageSrc = "http://music.bitlworks.co.kr/mobilemusic/image_home/" + StaticValues.album.album_id + "/disk0/" + image_file;
    ImagesDownloader.loadImageUseUIL(imageSrc, imagePhoto);
    findViewById(R.id.image_kakao_send).setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {

        final String nick = edtName.getText().toString().trim().replace("'", "");

        if (nick.length() < 3) {
          Toast.makeText(CONTEXT, "문자는 3자 이상이여야 합니다",
              Toast.LENGTH_SHORT).show();
          return;
        }

        message = "[" + StaticValues.album.album_name + "]\n\n" + nick + "\n\n" + message;
        try {
          kakaoLink = KakaoLink.getKakaoLink(getApplicationContext());
          kakaoTalkLinkMessageBuilder = kakaoLink.createKakaoTalkLinkMessageBuilder();

          kakaoTalkLinkMessageBuilder.addText(message);


          int ww = imagePhoto.getMeasuredWidth();
          int hh = imagePhoto.getMeasuredHeight();
          Log.d("hyuk-->ooo", "ww,hh>>" + ww + "??" + hh);


          Long tsLong = System.currentTimeMillis() / 1000;
          String ts = tsLong.toString();

          String imageSrc = "http://music.bitlworks.co.kr/mobilemusic/image_home/" + StaticValues.album.album_id + "/disk0/" + image_file + "?timestamp=" + ts;
          int width = 300;
          int height = width * hh / ww;//100;
          kakaoTalkLinkMessageBuilder.addImage(imageSrc, width, height);

          kakaoTalkLinkMessageBuilder.addWebButton("" + StaticValues.album.album_name, StudioValues.music_url);
          kakaoLink.sendMessage(kakaoTalkLinkMessageBuilder, getApplicationContext());
        } catch (KakaoParameterException e) {
          Log.d("hyuk-->", "error>>" + e.getMessage());
        }

        finish();
      }
    });
  }

  @Override
  public void onStart() {
    super.onStart();
/*
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "KaKaoTalkActivity Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://com.bitlworks.weddingalbum3.common/http/host/path")
        );
        AppIndex.AppIndexApi.start(client, viewAction);
        */
  }

  @Override
  public void onStop() {
    super.onStop();
/*
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "KaKaoTalkActivity Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://com.bitlworks.weddingalbum3.common/http/host/path")
        );
        AppIndex.AppIndexApi.end(client, viewAction);
        client.disconnect();
        */
  }
}
