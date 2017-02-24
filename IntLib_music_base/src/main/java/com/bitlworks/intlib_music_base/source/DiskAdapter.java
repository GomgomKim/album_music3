package com.bitlworks.intlib_music_base.source;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;


import com.bitlworks.intlib_music_base.R;
import com.bitlworks.intlib_music_base.data.VODisk;

import java.util.ArrayList;


public class DiskAdapter extends ArrayAdapter<VODisk> {

  public interface AlbumListListener {
    void onClickDisk(VODisk disk);
  }

  public enum IMAGE_TYPE {
    LARGE,
    SMALL,
  }

  private LayoutInflater inflater;
  private ViewHolder viewHolder;
  private AlbumListListener listener;
  private IMAGE_TYPE imageType;

  public DiskAdapter(Context context, ArrayList<VODisk> arrayList, IMAGE_TYPE imageType) {
    super(context, 0, arrayList);
    this.inflater = LayoutInflater.from(context);
    this.imageType = imageType;
  }

  public void setListener(AlbumListListener listener) {
    this.listener = listener;
  }

  @Override
  public View getView(int position, View convertView, ViewGroup parent) {
    if (convertView == null) {
      if (imageType == IMAGE_TYPE.LARGE) {
        convertView = inflater.inflate(R.layout.item_large_disk, null);
      } else {
        convertView = inflater.inflate(R.layout.item_small_disk, null);
      }
      viewHolder = new ViewHolder();
      viewHolder.music_name = (ImageView) convertView.findViewById(R.id.song_name);
      convertView.setTag(viewHolder);
    } else {
      viewHolder = (ViewHolder) convertView.getTag();
    }

    final VODisk item = getItem(position);
    int drawableId;
    if (imageType == IMAGE_TYPE.LARGE) {
      switch (position) {
        case 0:
          drawableId = R.drawable.playlist_button_disk1;
          break;
        case 1:
          drawableId = R.drawable.playlist_button_disk2;
          break;
        case 2:
          drawableId = R.drawable.playlist_button_disk3;
          break;
        default:
          throw new RuntimeException("Unknown position");
      }
    } else if (imageType == IMAGE_TYPE.SMALL) {
      if (item.disk_name.equals("A")) {
        drawableId = R.drawable.playlist_b_button_disk1;
      } else if (item.disk_name.equals("B")) {
        drawableId = R.drawable.playlist_b_button_disk2;
      } else if (item.disk_name.equals("C")) {
        drawableId = R.drawable.playlist_b_button_disk3;
      } else {
        throw new RuntimeException("Unknown position");
      }
    } else {
      throw new RuntimeException("Unknown image type");
    }
    viewHolder.music_name.setImageResource(drawableId);
    viewHolder.music_name.setTag(item);
    viewHolder.music_name.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        if (listener == null) {
          return;
        }
        listener.onClickDisk(item);
      }
    });
    return convertView;
  }

  @Override
  public int getCount() {
    return super.getCount();
  }

  @Override
  public VODisk getItem(int position) {
    return super.getItem(position);
  }

  @Override
  public long getItemId(int position) {
    return super.getItemId(position);
  }

  class ViewHolder {
    public ImageView music_name = null;
  }
}
