package com.ucom.intlib_bitlworks.setting;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.Window;

import com.ucom.intlib_bitlworks.R;

public class ProInviteDialog extends Dialog {
  public ProInviteDialog(Context context) {
    super(context);
    requestWindowFeature(Window.FEATURE_NO_TITLE);
    setContentView(R.layout.dialog_proinvite);

//    mAlbumId = getIntent().getIntExtra(EXTRA_ALBUM_ID, -1);

    findViewById(R.id.image_exit).setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        dismiss();
      }
    });

    findViewById(R.id.view_kakaotalk).setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        Intent i = new Intent(getContext(), KaKaoTalkActivity.class);
        getContext().startActivity(i);
        dismiss();
      }
    });

    findViewById(R.id.view_msg).setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        Intent i = new Intent(getContext(), InviteFriend.class);
        getContext().startActivity(i);
        dismiss();
      }
    });

    findViewById(R.id.view_sns).setOnClickListener(new View.OnClickListener() {

      @Override
      public void onClick(View v) {
        Intent share = new Intent(Intent.ACTION_SEND);
        share.setType("text/plain");
        share.putExtra(Intent.EXTRA_SUBJECT, "모바일 음반에 초대합니다.");
        share.putExtra(Intent.EXTRA_TEXT, StaticValues.album.album_invitemsg + "\n" + StaticValues.album.album_inviteurl);
        startActivity(Intent.createChooser(share, "문자 보내기"));
      }
    });
  }
}
