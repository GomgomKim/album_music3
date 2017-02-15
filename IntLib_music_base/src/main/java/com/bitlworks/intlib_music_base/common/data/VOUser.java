package com.bitlworks.intlib_music_base.common.data;

/**
 * Created by 강혁 on 2016-07-27.
 */
public class VOUser {
    public int user_id;
    public String user_name;
    public int album_id;
    public String user_phone_number;
    public String user_appver;
    public String user_uuid;
    public String user_regid;

    public int user_level;

    public VOUser(int user_id, String user_name, int album_id, String user_phone_number, String user_appver,
                  String user_uuid, String user_regid, int user_level) {
        //super();
        this.user_id = user_id;
        this.user_name = user_name;
        this.album_id = album_id;
        this.user_phone_number = user_phone_number;

        this.user_appver = user_appver;
        this.user_uuid = user_uuid;
        this.user_regid = user_regid;

        this.user_level=user_level;


    }
}
