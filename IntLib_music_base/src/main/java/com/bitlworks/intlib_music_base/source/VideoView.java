package com.bitlworks.intlib_music_base.source;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bitlworks.intlib_music_base.MusicUtils;
import com.bitlworks.intlib_music_base.R;
import com.bitlworks.intlib_music_base.StaticValues;

import java.io.File;

public class VideoView extends LinearLayout {

  public VideoView(Context context, int index) {
    super(context);
    LayoutInflater li = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    View v = li.inflate(R.layout.layout_video, this, true);

    File file = new File(MusicUtils.getAlbumPath(context)
        + "/video/"
        + StaticValues.videos.get(index).photoPath);
    Uri uri = Uri.fromFile(file);

    ImageView homeImage = (ImageView) v.findViewById(R.id.image_music);
    homeImage.setImageURI(uri);
    homeImage.setScaleType(ImageView.ScaleType.FIT_XY);
  }
}
