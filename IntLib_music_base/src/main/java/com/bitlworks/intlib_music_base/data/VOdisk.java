package com.bitlworks.intlib_music_base.data;

import java.io.Serializable;

public class VOdisk implements Serializable {
    public int disk_id;
    public String disk_name;
    public int album_id;

    public VOdisk(int disk_id, String disk_name, int album_id){
        this.disk_id = disk_id;
        this.disk_name = disk_name;
        this.album_id = album_id;
    }
}
