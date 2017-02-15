package com.bitlworks.intlib_music_base.common.data;

public class VOCouple {
	public static final int STATE_MAKING = 2;
	public static final int STATE_SAMPLE = 3;
	public static final int STATE_COMPLETE = 5;
	
	private int couple_id;
	private int couple_state;	
	private String couple_mr;
	private String couple_mrs;
	private String couple_time;
	private String couple_path;
	private String couple_video;
	private int couple_image;
	private String couple_photo;
	private String couple_dirszie;
	private String couple_mr_en;
	private String couple_mrs_en;
	private String hall_name_kr;
	private String hall_name_en;
	private String hall_address;
	private String hall_way1;
	private String hall_way2;
	private String hall_mapx;
	private String hall_mapy;
	private String hall_tel;
	private int couple_update;
	private int couple_like;
	private String couple_music;
	private String music_inpath;
	private int visible;

	public String album_title;
	public String album_msg;

	public VOCouple() {

	}
	
	public VOCouple(int couple_id, int couple_state, String couple_mr,
									String couple_mrs, String couple_time, String couple_path,
									String couple_video, int couple_image, String couple_photo,
									String couple_dirszie, String couple_mr_en, String couple_mrs_en,
									String hall_name_kr, String hall_name_en, String hall_address,
									String hall_way1, String hall_way2, String hall_mapx,
									String hall_mapy, String hall_tel, int couple_update,
									int couple_like, String couple_music, String music_inpath,
									int visible, String album_title,
									String album_msg) {
		super();
		this.couple_id = couple_id;
		this.couple_state = couple_state;
		this.couple_mr = couple_mr;
		this.couple_mrs = couple_mrs;
		this.couple_time = couple_time;
		this.couple_path = couple_path;
		this.couple_video = couple_video;
		this.couple_image = couple_image;
		this.couple_photo = couple_photo;
		this.couple_dirszie = couple_dirszie;
		this.couple_mr_en = couple_mr_en;
		this.couple_mrs_en = couple_mrs_en;
		this.hall_name_kr = hall_name_kr;
		this.hall_name_en = hall_name_en;
		this.hall_address = hall_address;
		this.hall_way1 = hall_way1;
		this.hall_way2 = hall_way2;
		this.hall_mapx = hall_mapx;
		this.hall_mapy = hall_mapy;
		this.hall_tel = hall_tel;
		this.couple_update = couple_update;
		this.couple_like = couple_like;
		this.couple_music = couple_music;
		this.music_inpath = music_inpath;
		this.visible = visible;
		this.album_title = album_title;
		this.album_msg = album_msg;
	}
	
	public int getCouple_id() {
		return couple_id;
	}

	public void setCouple_id(int couple_id) {
		this.couple_id = couple_id;
	}

	public int getCouple_state() {
		return couple_state;
	}

	public void setCouple_state(int couple_state) {
		this.couple_state = couple_state;
	}

	public String getCouple_mr() {
		return couple_mr;
	}

	public void setCouple_mr(String couple_mr) {
		this.couple_mr = couple_mr;
	}

	public String getCouple_mrs() {
		return couple_mrs;
	}

	public void setCouple_mrs(String couple_mrs) {
		this.couple_mrs = couple_mrs;
	}

	public String getCouple_time() {
		return couple_time;
	}

	public void setCouple_time(String couple_time) {
		this.couple_time = couple_time;
	}

	public String getCouple_path() {
		return couple_path;
	}

	public void setCouple_path(String couple_path) {
		this.couple_path = couple_path;
	}

	public String getCouple_video() {
		return couple_video;
	}

	public void setCouple_video(String couple_video) {
		this.couple_video = couple_video;
	}

	public int getCouple_image() {
		return couple_image;
	}

	public void setCouple_image(int couple_image) {
		this.couple_image = couple_image;
	}

	public String getCouple_photo() {
		return couple_photo;
	}

	public void setCouple_photo(String couple_photo) {
		this.couple_photo = couple_photo;
	}

	public String getCouple_dirszie() {
		return couple_dirszie;
	}

	public void setCouple_dirszie(String couple_dirszie) {
		this.couple_dirszie = couple_dirszie;
	}

	public String getCouple_mr_en() {
		return couple_mr_en;
	}

	public void setCouple_mr_en(String couple_mr_en) {
		this.couple_mr_en = couple_mr_en;
	}

	public String getCouple_mrs_en() {
		return couple_mrs_en;
	}

	public void setCouple_mrs_en(String couple_mrs_en) {
		this.couple_mrs_en = couple_mrs_en;
	}

	public String getHall_name_kr() {
		return hall_name_kr;
	}

	public void setHall_name_kr(String hall_name_kr) {
		this.hall_name_kr = hall_name_kr;
	}

	public String getHall_name_en() {
		return hall_name_en;
	}

	public void setHall_name_en(String hall_name_en) {
		this.hall_name_en = hall_name_en;
	}

	public String getHall_address() {
		return hall_address;
	}

	public void setHall_address(String hall_address) {
		this.hall_address = hall_address;
	}

	public String getHall_way1() {
		return hall_way1;
	}

	public void setHall_way1(String hall_way1) {
		this.hall_way1 = hall_way1;
	}

	public String getHall_way2() {
		return hall_way2;
	}

	public void setHall_way2(String hall_way2) {
		this.hall_way2 = hall_way2;
	}

	public String getHall_mapx() {
		return hall_mapx;
	}

	public void setHall_mapx(String hall_mapx) {
		this.hall_mapx = hall_mapx;
	}

	public String getHall_mapy() {
		return hall_mapy;
	}

	public void setHall_mapy(String hall_mapy) {
		this.hall_mapy = hall_mapy;
	}

	public String getHall_tel() {
		return hall_tel;
	}

	public void setHall_tel(String hall_tel) {
		this.hall_tel = hall_tel;
	}

	public int getCouple_update() {
		return couple_update;
	}

	public void setCouple_update(int couple_update) {
		this.couple_update = couple_update;
	}

	public int getCouple_like() {
		return couple_like;
	}

	public void setCouple_like(int couple_like) {
		this.couple_like = couple_like;
	}

	public String getCouple_music() {
		return couple_music;
	}

	public void setCouple_music(String couple_music) {
		this.couple_music = couple_music;
	}

	public String getMusic_inpath() {
		return music_inpath;
	}

	public void setMusic_inpath(String music_inpath) {
		this.music_inpath = music_inpath;
	}

	public int getVisible() {
		return visible;
	}

	public void setVisible(int visible) {
		this.visible = visible;
	}

	public String getCouple_photoPath() {
		final String[] filenames = couple_photo.split("\\.");
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < filenames.length - 1; i++) {
			sb.append(filenames[i]);
		}
		sb.append(".ti");
		return sb.toString();
	}

}
