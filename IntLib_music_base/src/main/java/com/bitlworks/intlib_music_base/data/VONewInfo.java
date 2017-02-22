package com.bitlworks.intlib_music_base.data;

import java.io.Serializable;

public class VONewInfo implements Serializable {

  public int info_id;
  public int album_id;
  public String main_subject;
  public String time;
  public String image_data;
  public String contents;
  public String link_url;

  public VONewInfo(int info_id, int album_id, String main_subject, String time,
                   String image_data, String contents, String link_url) {
    this.info_id = info_id;
    this.album_id = album_id;
    this.main_subject = main_subject;
    this.time = time;
    this.image_data = image_data;
    this.contents = contents;
    this.link_url = link_url;
  }

}
