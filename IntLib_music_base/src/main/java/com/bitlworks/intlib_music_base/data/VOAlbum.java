package com.bitlworks.intlib_music_base.data;

import android.graphics.Color;

public class VOAlbum {

    public int album_id;
    public String album_name;
    public String singer_id_list;
    public String album_type;
    public String album_genre;
    public String album_company;
    public String album_intro;
    public String album_time;
    public String album_invitemsg;
    public String album_inviteurl;
    public String album_cover_name;
    public String album_disk_bg_name;
    public String album_title_image;
    public String album_main_image;
    public int album_primary_color;

    public VOAlbum(int album_id, String album_name, String singer_id_list,
                   String album_type, String album_genre, String album_company,
                   String album_intro, String album_time,
                   String album_invitemsg, String album_inviteurl,
                   String album_cover_name, String album_disk_bg_name,
                   String album_title_image, String album_main_image,
                   int album_primary_color) {

        this.album_id = album_id;
        this.album_name = album_name;
        this.singer_id_list = singer_id_list;

        this.album_type = album_type;
        this.album_genre = album_genre;
        this.album_company = album_company;
        this.album_intro = album_intro;
        this.album_time = album_time;
        this.album_invitemsg = album_invitemsg;
        this.album_inviteurl = album_inviteurl;

        this.album_cover_name = album_cover_name;
        this.album_disk_bg_name = album_disk_bg_name;
        this.album_title_image = album_title_image;
        this.album_main_image = album_main_image;
        this.album_primary_color = album_primary_color;
    }
}
