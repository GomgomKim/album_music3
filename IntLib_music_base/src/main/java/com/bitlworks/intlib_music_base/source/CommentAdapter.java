package com.bitlworks.intlib_music_base.source;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bitlworks.intlib_music_base.R;
import com.bitlworks.intlib_music_base.StaticValues;
import com.bitlworks.intlib_music_base.data.VOComment;

import java.util.ArrayList;

public class CommentAdapter extends ArrayAdapter<VOComment> {

  public interface CommentAdapterListener {
    void onClickComment(int commentId);
  }

  private Context context;
  private LayoutInflater inflater = null;
  private ViewHolder viewHolder = null;
  private CommentAdapterListener listener;

  public CommentAdapter(Context context, ArrayList<VOComment> arrayList) {
    super(context, 0, arrayList);
    this.inflater = LayoutInflater.from(context);
    this.context = context;
  }

  public void setListener(CommentAdapterListener listener) {
    this.listener = listener;
  }

  @Override
  public View getView(final int position, View convertView, ViewGroup parent) {

    if (convertView == null) {
      convertView = inflater.inflate(R.layout.item_comment, null);
      viewHolder = new ViewHolder();
      viewHolder.comment_contents = (TextView) convertView.findViewById(R.id.comment_contents);
      viewHolder.user_name = (TextView) convertView.findViewById(R.id.user);
      viewHolder.del = (ImageView) convertView.findViewById(R.id.imageDel);
      convertView.setTag(viewHolder);
    } else {
      viewHolder = (ViewHolder) convertView.getTag();
    }

    final VOComment item = getItem(position);
//    viewHolder.comment_contents.setTypeface(ff);
    viewHolder.comment_contents.setText(item.comment_contents);
//    viewHolder.user_name.setTypeface(ff, Typeface.BOLD);
    viewHolder.user_name.setText("-" + item.user_name + "-");

    if (item.user_id == StaticValues.user.user_id || StaticValues.user.user_level == 9) {
      viewHolder.del.setVisibility(View.VISIBLE);
      viewHolder.del.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
          listener.onClickComment(item.comment_id);
        }
      });
    } else {
      viewHolder.del.setVisibility(View.INVISIBLE);
    }

    return convertView;
  }

  @Override
  public int getCount() {
    return super.getCount();
  }

  @Override
  public VOComment getItem(int position) {
    return super.getItem(position);
  }

  @Override
  public long getItemId(int position) {
    return super.getItemId(position);
  }

  class ViewHolder {
    public TextView comment_contents = null;
    public TextView user_name = null;
    public ImageView del = null;
  }
}
