package com.ucom.intlib_bitlworks.setting;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.ucom.intlib_bitlworks.R;


public class ProInviteActivity extends Activity {
    private int mAlbumId;
    public static final String EXTRA_ALBUM_ID = "album";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        getWindow().setAttributes(layoutParams);

        setContentView(R.layout.activity_pro_invite);

        mAlbumId = getIntent().getIntExtra(EXTRA_ALBUM_ID, -1);

        findViewById(R.id.image_exit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               finish();
            }
        });

//        findViewById(R.id.view_kakaotalk).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent i = new Intent(ProInviteActivity.this,	KaTalkMsgWrite.class);
//                startActivity(i);
//            }
//        });
//
//        findViewById(R.id.view_msg).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent i = new Intent(ProInviteActivity.this,	InviteFriend.class);
//                startActivity(i);

//                Intent i = new Intent(ProInviteActivity.this,	InviteActivity.class);
//                i.putExtra(InviteActivity.EXTRA_ALBUM_ID, mAlbumId);
//                startActivity(i);
//            }
//        });

        findViewById(R.id.view_sns).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
//                Intent share = new Intent(Intent.ACTION_SEND);
//                share.setType("text/plain");
//                share.putExtra(Intent.EXTRA_SUBJECT, "모바일 음반에 초대합니다.");
//                share.putExtra(Intent.EXTRA_TEXT, StaticValues.album.album_invitemsg + "\n" + StaticValues.album.album_inviteurl);
//                startActivity(Intent.createChooser(share, "문자 보내기"));
            }
        });
    }
}
