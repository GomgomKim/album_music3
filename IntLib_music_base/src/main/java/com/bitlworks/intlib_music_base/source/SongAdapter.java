package com.bitlworks.intlib_music_base.source;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.bitlworks.intlib_music_base.R;
import com.bitlworks.intlib_music_base.StaticValues;
import com.bitlworks.intlib_music_base.data.VOSong;

import java.util.ArrayList;

public class SongAdapter extends ArrayAdapter<VOSong> {

  public interface SongListListener {
    void onClickSong(int position);
  }

  private LayoutInflater inflater;
  private ViewHolder viewHolder;
  private SongListListener listener;

  public SongAdapter(Context context, ArrayList<VOSong> arrayList) {
    super(context, 0, arrayList);
    this.inflater = LayoutInflater.from(context);
  }

  public void setListener(SongListListener listener) {
    this.listener = listener;
  }

  @Override
  public View getView(final int position, View convertView, ViewGroup parent) {
    if (convertView == null) {
      convertView = inflater.inflate(R.layout.item_song, null);
      viewHolder = new ViewHolder();
      viewHolder.songNameText = (TextView) convertView.findViewById(R.id.text_song_name);
      convertView.setTag(viewHolder);
    } else {
      viewHolder = (ViewHolder) convertView.getTag();
    }
    final VOSong item = getItem(position);
    viewHolder.songNameText.setTextColor(StaticValues.metadata.text_color);
    viewHolder.songNameText.setText((position+1) + "  " + item.msg1 + " - " + item.song_name);
    viewHolder.songNameText.setOnClickListener(new View.OnClickListener() {

      @Override
      public void onClick(View v) {
        listener.onClickSong(position);
      }
    });

    return convertView;
  }

  @Override
  public int getCount() {
    return super.getCount();
  }

  @Override
  public VOSong getItem(int position) {
    return super.getItem(position);
  }

  @Override
  public long getItemId(int position) {
    return super.getItemId(position);
  }

  class ViewHolder {
    public TextView songNameText;
  }
}