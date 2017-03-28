package com.bitlworks.intlib_music_base.source;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;


import com.bitlworks.intlib_music_base.MusicUtils;
import com.bitlworks.intlib_music_base.R;
import com.bitlworks.intlib_music_base.StaticValues;
import com.bitlworks.intlib_music_base.data.VODisk;

import java.io.File;
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
      viewHolder.diskImage = (ImageView) convertView.findViewById(R.id.image_disk);
      viewHolder.diskNameText = (TextView) convertView.findViewById(R.id.text_disk_name);
      convertView.setTag(viewHolder);
    } else {
      viewHolder = (ViewHolder) convertView.getTag();
    }

    final VODisk item = getItem(position);
    int drawableId;
    if (imageType == IMAGE_TYPE.LARGE) {
    } else if (imageType == IMAGE_TYPE.SMALL) {
      File file = new File(MusicUtils.getAlbumPath(getContext()) + "metadata/" + StaticValues.metadata.disk_icon);
      viewHolder.diskImage.setImageURI(Uri.fromFile(file));
      viewHolder.diskNameText.setText(item.disk_name);
    } else {
      throw new RuntimeException("Unknown image type");
    }
    viewHolder.diskImage.setTag(item);
    viewHolder.diskImage.setOnClickListener(new View.OnClickListener() {
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
    public ImageView diskImage = null;
    public TextView diskNameText = null;
  }
}
