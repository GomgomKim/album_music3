package com.bitlworks.intlib_music_base.data;

public class VOPhoto {
    public int photo_id;
    public int disk_id;
    public String photo_file_name;
    public int photo_order;

    public VOPhoto(int photo_id, int disk_id, String photo_file_name,int photo_order) {
        this.photo_id = photo_id;
        this.disk_id = disk_id;
        this.photo_file_name = photo_file_name;
        this.photo_order = photo_order;
    }
}
