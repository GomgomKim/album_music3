package com.bitlworks.intlib_music_base.common.data;

/**
 * Created by 강혁 on 2016-08-18.
 */
public class VONewInfo {

    public int info_id;
    public int album_id;

    public String main_subject;
    public String time;
    public String image_data;
    public String contents;

    public String link_url;
/*
sql = "CREATE TABLE new_info("
				+"info_id INTEGER primary key,"
				+"album_id INTEGER ,"
				+"main_subject TEXT,"
				+"time TEXT,"
				+"image_data TEXT,"
				+"contents TEXT,"
				+"link_url TEXT );";
    */
    public VONewInfo(int info_id, int album_id, String main_subject, String time,
                     String image_data, String contents, String link_url) {
        //super();
        this.info_id = info_id;
        this.album_id = album_id;
        this.main_subject = main_subject;
        this.time = time;
        this.image_data = image_data;
        this.contents = contents;

        this.link_url = link_url;
    }

}
