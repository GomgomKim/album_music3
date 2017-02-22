package com.bitlworks.intlib_music_base.source.setting;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bitlworks.intlib_music_base.R;
import com.bitlworks.intlib_music_base.data.VONewInfo;
import com.bitlworks.intlib_music_base.data.VOSong;

import java.util.ArrayList;


public class NewInfoAdapter extends ArrayAdapter<VONewInfo> {

  public interface SongListListener {
    void onClickSong(int position);
  }

  private LayoutInflater inflater;
  private ViewHolder viewHolder;
  private SongListListener listener;

  public NewInfoAdapter(Context context, ArrayList<VONewInfo> arrayList) {
    super(context, 0, arrayList);
    this.inflater = LayoutInflater.from(context);
  }

  public void setListener(SongListListener listener) {
    this.listener = listener;
  }

  @Override
  public View getView(final int position, View convertView, ViewGroup parent) {
    if (convertView == null) {
      convertView = inflater.inflate(R.layout.item_newinfo, null);
      viewHolder = new ViewHolder();
      viewHolder.contents = (TextView) convertView.findViewById(R.id.contents);
      viewHolder.main_subject = (TextView) convertView.findViewById(R.id.main_subject);
      viewHolder.time = (TextView) convertView.findViewById(R.id.time);
      viewHolder.image_data = (ImageView) convertView.findViewById(R.id.imageView1);
      viewHolder.image_new = (ImageView) convertView.findViewById(R.id.imageNEW);
      viewHolder.del = (ImageView) convertView.findViewById(R.id.del);
      convertView.setTag(viewHolder);
    } else {
      viewHolder = (ViewHolder) convertView.getTag();
    }
    final VONewInfo item = getItem(position);
    viewHolder.contents.setText(item.contents);
    viewHolder.main_subject.setText(item.main_subject);
    viewHolder.time.setText(item.time);
    return convertView;
  }

  @Override
  public int getCount() {
    return super.getCount();
  }

  @Override
  public VONewInfo getItem(int position) {
    return super.getItem(position);
  }

  @Override
  public long getItemId(int position) {
    return super.getItemId(position);
  }

  class ViewHolder {
    public TextView main_subject = null;
    public TextView time = null;
    public ImageView image_data = null;
    public TextView contents = null;
    public ImageView image_new = null;

    public ImageView del = null;
  }
}
