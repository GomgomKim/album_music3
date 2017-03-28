package com.bitlworks.intlib_music_base.data;

public class VOComment {
    public int comment_id;
    public int user_id;
    public String user_name;
    public String comment_time;
    public String comment_contents;
    public int album_id;

    public VOComment(int comment_id, int user_id,
                     String user_name, String comment_time, String comment_contents, int album_id) {
        this.comment_id = comment_id;
        this.user_id = user_id;
        this.user_name = user_name;
        this.comment_time = comment_time;
        this.comment_contents = comment_contents;
        this.album_id =album_id;
    }
}
