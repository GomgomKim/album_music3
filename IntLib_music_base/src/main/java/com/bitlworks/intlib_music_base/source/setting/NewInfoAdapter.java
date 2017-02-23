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
import com.squareup.picasso.Picasso;

import java.util.ArrayList;


public class NewInfoAdapter extends ArrayAdapter<VONewInfo> {

  private LayoutInflater inflater;

  public NewInfoAdapter(Context context, ArrayList<VONewInfo> arrayList) {
    super(context, 0, arrayList);
    this.inflater = LayoutInflater.from(context);
  }

  @Override
  public View getView(final int position, View convertView, ViewGroup parent) {
    ViewHolder viewHolder;
    if (convertView == null) {
      convertView = inflater.inflate(R.layout.item_newinfo, null);
      viewHolder = new ViewHolder();
      viewHolder.mainSubjectText = (TextView) convertView.findViewById(R.id.text_main_subject);
      viewHolder.timeText = (TextView) convertView.findViewById(R.id.text_time);
      viewHolder.deleteButton = convertView.findViewById(R.id.button_delete);
      viewHolder.subjectImage = (ImageView) convertView.findViewById(R.id.image_subject);
      viewHolder.contentText = (TextView) convertView.findViewById(R.id.text_content);
      convertView.setTag(viewHolder);
    } else {
      viewHolder = (ViewHolder) convertView.getTag();
    }
    final VONewInfo item = getItem(position);
    viewHolder.mainSubjectText.setText(item.main_subject);
    viewHolder.timeText.setText(item.time);
    Picasso.with(convertView.getContext())
        .load(item.link_url)
        .into(viewHolder.subjectImage);
    viewHolder.contentText.setText(item.contents);
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

  private class ViewHolder {
    TextView mainSubjectText = null;
    TextView timeText = null;
    ImageView subjectImage = null;
    TextView contentText = null;
    View deleteButton = null;
  }
}
