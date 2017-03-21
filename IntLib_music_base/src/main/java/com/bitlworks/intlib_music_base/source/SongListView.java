package com.bitlworks.intlib_music_base.source;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.bitlworks.intlib_music_base.R;
import com.bitlworks.intlib_music_base.StaticValues;
import com.bitlworks.intlib_music_base.data.VODisk;

import java.util.ArrayList;

public class SongListView extends LinearLayout {


  public SongListView(Context context,
                      DiskAdapter.AlbumListListener albumListListener,
                      SongAdapter.SongListListener songListListener) {
    super(context);

    LayoutInflater li = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    View v = li.inflate(R.layout.layout_song_list, this, true);

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

    ArrayList<VODisk> otherDiskList = new ArrayList<>();
    for (VODisk disk : StaticValues.disks) {
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
    SongAdapter songAdapter = new SongAdapter(getContext(), StaticValues.songs);
    songAdapter.setListener(songListListener);
    songListView.setAdapter(songAdapter);
  }
}
