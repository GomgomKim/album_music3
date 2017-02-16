package com.bitlworks.intlib_music_base.common.source;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.bitlworks.intlib_music_base.R;
import com.bitlworks.intlib_music_base.common.StaticValues;

public class DiskListView extends LinearLayout {

  public DiskListView(Context context, DiskAdapter.AlbumListListener listener) {
    super(context);
    initView(listener);
  }

  private void initView(DiskAdapter.AlbumListListener listener) {
    String infService = Context.LAYOUT_INFLATER_SERVICE;
    LayoutInflater li = (LayoutInflater) getContext().getSystemService(infService);
    View v = li.inflate(R.layout.layout_disk_list, this, false);
    addView(v);

    ListView diskListView = (ListView) findViewById(R.id.list_disk);
    DiskAdapter adapter =
        new DiskAdapter(getContext(), StaticValues.diskList, DiskAdapter.IMAGE_TYPE.LARGE);
    adapter.setListener(listener);
    diskListView.setAdapter(adapter);
  }
}
