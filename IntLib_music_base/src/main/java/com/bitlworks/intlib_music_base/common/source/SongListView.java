package com.bitlworks.intlib_music_base.common.source;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.bitlworks.intlib_music_base.R;
import com.bitlworks.intlib_music_base.common.StaticValues;
import com.bitlworks.intlib_music_base.common.data.VOdisk;

import java.util.ArrayList;

public class SongListView extends LinearLayout {


  public SongListView(Context context,
                      DiskAdapter.AlbumListListener albumListListener,
                      SongAdapter.SongListListener songListListener) {
    super(context);
    initView(albumListListener, songListListener);
  }

  private void initView(DiskAdapter.AlbumListListener albumListListener,
                        SongAdapter.SongListListener songListListener) {
    String infService = Context.LAYOUT_INFLATER_SERVICE;
    LayoutInflater li = (LayoutInflater) getContext().getSystemService(infService);
    View v = li.inflate(R.layout.layout_song_list, this, false);
    addView(v);

    ImageView selectedDiskImage = (ImageView) v.findViewById(R.id.image_selected_disk);
    String diskName = StaticValues.selectedDisk.disk_name;
    if (diskName.equals("A")) {
      selectedDiskImage.setImageResource(R.drawable.playlist_button_disk1);
    } else if (diskName.equals("B")) {
      selectedDiskImage.setImageResource(R.drawable.playlist_button_disk2);
    } else if (diskName.equals("C")) {
      selectedDiskImage.setImageResource(R.drawable.playlist_button_disk3);
    }

    ListView diskListView = (ListView) findViewById(R.id.list_disk);

    ArrayList<VOdisk> otherDiskList = new ArrayList<>();
    for (VOdisk disk : StaticValues.diskList) {
      if (disk.disk_id == StaticValues.selectedDisk.disk_id) {
        continue;
      }
      otherDiskList.add(disk);
    }
    DiskAdapter diskAdapter =
        new DiskAdapter(getContext(), otherDiskList, DiskAdapter.IMAGE_TYPE.SMALL);
    diskAdapter.setListener(albumListListener);
    diskListView.setAdapter(diskAdapter);

    ListView songListView = (ListView) findViewById(R.id.list_song);
    SongAdapter songAdapter = new SongAdapter(getContext(), StaticValues.songList);
    songAdapter.setListener(songListListener);
    songListView.setAdapter(songAdapter);
  }
}
