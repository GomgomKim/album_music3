package com.bitlworks.intlib_music_base.source;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bitlworks.intlib_bitlworks.setting.CSActivity;
import com.bitlworks.intlib_music_base.MusicUtils;
import com.bitlworks.intlib_music_base.R;
import com.bitlworks.intlib_music_base.StaticValues;
import com.bitlworks.intlib_music_base.source.setting.NewInfoActivity;
import com.bitlworks.intlib_music_base.source.setting.ProInviteDialog;

import java.io.File;

public class SettingView extends LinearLayout {

  public SettingView(Context context) {
    super(context);
    LayoutInflater li = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    View v = li.inflate(R.layout.layout_setting, this, true);

    File file = new File(MusicUtils.getAlbumPath(context) + "metadata/" + StaticValues.metadata.setting_bg);
    Drawable d = Drawable.createFromPath(file.getAbsolutePath());
    v.setBackground(d);

    ImageView titleImage = (ImageView) v.findViewById(R.id.image_title);
    File titleFile = new File(MusicUtils.getAlbumPath(context) + "metadata/" + StaticValues.metadata.title_image);
    titleImage.setImageURI(Uri.fromFile(titleFile));
    titleImage.setScaleType(ImageView.ScaleType.FIT_XY);

    ImageView mainImage = (ImageView) v.findViewById(R.id.image_main);
    File mainFile = new File(MusicUtils.getAlbumPath(context) + "metadata/" + StaticValues.metadata.main_image);
    mainImage.setImageURI(Uri.fromFile(mainFile));
    mainImage.setScaleType(ImageView.ScaleType.FIT_XY);

    TextView albumIntroText = (TextView) findViewById(R.id.text_album_intro);
    albumIntroText.setText(StaticValues.album.album_intro);

    TextView newInfoText = (TextView) v.findViewById(R.id.text_new_info);
    newInfoText.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View view) {
        Intent i = new Intent(getContext(), NewInfoActivity.class);
        getContext().startActivity(i);
      }
    });
    newInfoText.setBackgroundColor(StaticValues.metadata.color);

    TextView inviteText = (TextView) v.findViewById(R.id.text_invite);
    inviteText.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View view) {
        ProInviteDialog dialog = new ProInviteDialog(getContext());
        dialog.show();
      }
    });
    inviteText.setBackgroundColor(StaticValues.metadata.color);

    TextView customerCenterText = (TextView) v.findViewById(R.id.text_customer_center);
    customerCenterText.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View view) {
        Intent i = new Intent(getContext(), CSActivity.class);
        getContext().startActivity(i);
      }
    });
    customerCenterText.setBackgroundColor(StaticValues.metadata.color);
  }
}
