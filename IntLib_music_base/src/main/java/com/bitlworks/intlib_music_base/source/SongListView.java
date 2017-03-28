package com.bitlworks.intlib_music_base.source;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.bitlworks.intlib_music_base.MusicUtils;
import com.bitlworks.intlib_music_base.R;
import com.bitlworks.intlib_music_base.StaticValues;
import com.bitlworks.intlib_music_base.data.VODisk;

import java.io.File;
import java.util.ArrayList;

public class SongListView extends LinearLayout {


  public SongListView(Context context,
                      DiskAdapter.AlbumListListener albumListListener,
                      SongAdapter.SongListListener songListListener) {
    super(context);

    LayoutInflater li = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    View v = li.inflate(R.layout.layout_song_list, this, true);

    File file = new File(MusicUtils.getAlbumPath(context) + "metadata/" + StaticValues.metadata.disk_bg);
    Drawable d = Drawable.createFromPath(file.getAbsolutePath());
    v.setBackground(d);

    ImageView selectedDiskImage = (ImageView) v.findViewById(R.id.image_selected_disk);

    File selectedDiskFile = new File(MusicUtils.getAlbumPath(getContext()) + StaticValues.selectedDisk.disk_id + "/" + StaticValues.selectedDisk.disk_icon);
    selectedDiskImage.setImageURI(Uri.fromFile(selectedDiskFile));

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
    songListView.setBackgroundColor(StaticValues.metadata.color);

  }
}
