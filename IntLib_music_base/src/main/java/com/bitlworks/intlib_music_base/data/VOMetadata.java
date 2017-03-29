package com.bitlworks.intlib_music_base.data;

public class VOMetadata {

  public int album_id;
  public String album_cover;
  public String disk_bg;
  public String review_bg;
  public String setting_bg;
  public int color;
  public int text_color;
  public String title_image;
  public String main_image;
  public String music_player_bg;
  public String song_play_icon;
  public String song_pause_icon;
  public String song_list_icon;
  public String lyrics_icon;
  public String disk_icon;
  public String mini_icon;

  public VOMetadata(int album_id,
                    String album_cover, String disk_bg, String review_bg, String setting_bg, int color, int text_color,
                    String title_image, String main_image,
                    String music_player_bg, String song_play_icon, String song_pause_icon, String song_list_icon, String lyrics_icon,
                    String disk_icon, String mini_icon) {

    this.album_id = album_id;
    this.album_cover = album_cover;
    this.disk_bg = disk_bg;
    this.review_bg = review_bg;
    this.setting_bg = setting_bg;
    this.color = color;
    this.text_color = text_color;
    this.title_image = title_image;
    this.main_image = main_image;
    this.music_player_bg = music_player_bg;
    this.song_play_icon = song_play_icon;
    this.song_pause_icon = song_pause_icon;
    this.song_list_icon = song_list_icon;
    this.disk_icon = disk_icon;
    this.lyrics_icon = lyrics_icon;
    this.mini_icon = mini_icon;
  }
}
