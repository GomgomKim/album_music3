package com.bitlworks.intlib_music_base.source;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.bitlworks.intlib_music_base.MusicUtils;
import com.bitlworks.intlib_music_base.R;
import com.bitlworks.intlib_music_base.StaticValues;

import java.io.File;

public class DiskListView extends LinearLayout {

  public DiskListView(Context context, DiskAdapter.AlbumListListener listener) {
    super(context);
    LayoutInflater li = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    View v = li.inflate(R.layout.layout_disk_list, this, true);

    ListView diskListView = (ListView) findViewById(R.id.list_disk);
    DiskAdapter adapter =
        new DiskAdapter(getContext(), StaticValues.disks, DiskAdapter.IMAGE_TYPE.LARGE);
    adapter.setListener(listener);
    diskListView.setAdapter(adapter);
  }
}
