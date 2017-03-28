package com.bitlworks.intlib_music_base.source;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.bitlworks.intlib_music_base.MusicClient;
import com.bitlworks.intlib_music_base.MusicUtils;
import com.bitlworks.intlib_music_base.R;
import com.bitlworks.intlib_music_base.StaticValues;
import com.bitlworks.intlib_music_base.data.VOComment;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;

import java.io.File;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CommentView extends LinearLayout implements
    CommentAdapter.CommentAdapterListener,
    PagerMainActivity.Listener {

  private View writeCommentView;
  private CommentAdapter commentAdapter;

  public CommentView(Context context) {
    super(context);
    LayoutInflater li = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    View v = li.inflate(R.layout.layout_comment, this, true);

    File file = new File(MusicUtils.getAlbumPath(context) + "metadata/" + StaticValues.metadata.review_bg);
    Drawable d = Drawable.createFromPath(file.getAbsolutePath());
    v.setBackground(d);

    findViewById(R.id.ll_root).setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View view) {
        if (writeCommentView.getVisibility() == VISIBLE) {
          writeCommentView.setVisibility(GONE);
        }
      }
    });

    writeCommentView = v.findViewById(R.id.view_write_comment);
    v.findViewById(R.id.button_write).setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View view) {
        writeCommentView.setVisibility(View.VISIBLE);
      }
    });
    v.findViewById(R.id.button_edit).setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View v) {
      }
    });

    final ListView commentListView = (ListView) findViewById(R.id.lv_comments);
    commentAdapter = new CommentAdapter(getContext(), StaticValues.comments);
    commentAdapter.setListener(this);
    commentListView.setAdapter(commentAdapter);

    final EditText nameEditText = (EditText) v.findViewById(R.id.edittext_name);
    nameEditText.setText("");
    final EditText commentEditText = (EditText) v.findViewById(R.id.edittext_comment);
    v.findViewById(R.id.button_send).setOnClickListener(new OnClickListener() {

      @Override
      public void onClick(View v) {
        String name = nameEditText.getText().toString();
        String comment = commentEditText.getText().toString();
        if (name.length() < 1) {
          Toast.makeText(getContext(), "작성자 이름을 입력하세요.", Toast.LENGTH_SHORT).show();
          return;
        }
        if (comment.length() < 5) {
          Toast.makeText(getContext(), "보낼 메세지가 너무 짧아요.", Toast.LENGTH_SHORT).show();
          return;
        }
        insertComment(name, comment);
        commentEditText.getText().clear();
        writeCommentView.setVisibility(View.GONE);

        InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getWindowToken(), 0);
      }
    });
  }

  @Override
  public void onClickComment(int commentId) {
    deleteComment(commentId);
  }

  @Override
  public void updateCommentList() {
    getComments();
  }

  @Override
  public void onBackPressed() {
    if (writeCommentView.getVisibility() == VISIBLE) {
      writeCommentView.setVisibility(GONE);
    }
  }

  private void getComments() {
    final ProgressDialog progressDialog = new ProgressDialog(getContext());
    progressDialog.setCancelable(false);
    progressDialog.show();

    Call<JsonArray> call = MusicClient.getInstance().getService().getComments(StaticValues.album.album_id);
    call.enqueue(
        new Callback<JsonArray>() {
          @Override
          public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
            progressDialog.dismiss();
            ArrayList<VOComment> comments = new ArrayList<>();
            JsonArray array = response.body().getAsJsonArray();
            for (JsonElement object : array) {
              VOComment comment = new VOComment(
                  object.getAsJsonObject().get("comment_id").getAsInt(),
                  object.getAsJsonObject().get("user_id").getAsInt(),
                  object.getAsJsonObject().get("user_name").getAsString(),
                  object.getAsJsonObject().get("comment_time").getAsString(),
                  object.getAsJsonObject().get("comment_contents").getAsString(),
                  object.getAsJsonObject().get("album_id").getAsInt());
              comments.add(comment);
            }

            StaticValues.comments.addAll(comments);
            commentAdapter.notifyDataSetChanged();
          }

          @Override
          public void onFailure(Call<JsonArray> call, Throwable t) {
            Log.e("onFailure", t.getMessage());
          }
        }
    );
  }

  private void insertComment(String name, String contents) {
    final ProgressDialog progressDialog = new ProgressDialog(getContext());
    progressDialog.setCancelable(false);
    progressDialog.show();

    Call<JsonArray> call = MusicClient.getInstance().getService().insertComment(StaticValues.album.album_id, StaticValues.user.user_id, name, contents);
    call.enqueue(
        new Callback<JsonArray>() {
          @Override
          public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
            progressDialog.dismiss();
            ArrayList<VOComment> comments = new ArrayList<>();
            JsonArray array = response.body().getAsJsonArray();
            for (JsonElement object : array) {
              VOComment comment = new VOComment(
                  object.getAsJsonObject().get("comment_id").getAsInt(),
                  object.getAsJsonObject().get("user_id").getAsInt(),
                  object.getAsJsonObject().get("user_name").getAsString(),
                  object.getAsJsonObject().get("comment_time").getAsString(),
                  object.getAsJsonObject().get("comment_contents").getAsString(),
                  object.getAsJsonObject().get("album_id").getAsInt());
              comments.add(comment);
            }

            StaticValues.comments.addAll(comments);
            commentAdapter.notifyDataSetChanged();
          }

          @Override
          public void onFailure(Call<JsonArray> call, Throwable t) {
            Log.e("onFailure", t.getMessage());
          }
        }
    );
  }

  private void deleteComment(int commentId) {
    final ProgressDialog progressDialog = new ProgressDialog(getContext());
    progressDialog.setCancelable(false);
    progressDialog.show();

    Call<JsonArray> call = MusicClient.getInstance().getService().deleteComment(StaticValues.album.album_id, commentId);
    call.enqueue(
        new Callback<JsonArray>() {
          @Override
          public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
            progressDialog.dismiss();
            ArrayList<VOComment> comments = new ArrayList<>();
            JsonArray array = response.body().getAsJsonArray();
            for (JsonElement object : array) {
              VOComment comment = new VOComment(
                  object.getAsJsonObject().get("comment_id").getAsInt(),
                  object.getAsJsonObject().get("user_id").getAsInt(),
                  object.getAsJsonObject().get("user_name").getAsString(),
                  object.getAsJsonObject().get("comment_time").getAsString(),
                  object.getAsJsonObject().get("comment_contents").getAsString(),
                  object.getAsJsonObject().get("album_id").getAsInt());
              comments.add(comment);
            }

            StaticValues.comments.addAll(comments);
            commentAdapter.notifyDataSetChanged();
          }

          @Override
          public void onFailure(Call<JsonArray> call, Throwable t) {
            Log.e("onFailure", t.getMessage());
          }
        }
    );
  }
}
