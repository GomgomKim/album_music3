package com.bitlworks.intlib_music_base.common.data;

import java.io.File;
import java.io.Serializable;

public class VOPhoto implements Serializable {
	private static final long serialVersionUID = -1287871989980040685L;
	private int photo_id;
	private int theme_id;
	private int dtheme_id;
	private int couple_id;
	private String photo_path;
	private int photo_isPublic;
	private int photo_isSelect;
	private int photo_isHeightrLarge;
	private String photo_time;
	private int photo_sequence;
	private String photo_url;
	private String photo_inpath;

	public VOPhoto() { }

	public VOPhoto(int photo_id, int theme_id, int dtheme_id, int couple_id,
								 String photo_path, int photo_isPublic, int photo_isSelect,
								 int photo_isHeightrLarge, String photo_time, int photo_sequence) {
		super();
		this.photo_id = photo_id;
		this.theme_id = theme_id;
		this.dtheme_id = dtheme_id;
		this.couple_id = couple_id;
		this.photo_path = photo_path;
		this.photo_isPublic = photo_isPublic;
		this.photo_isSelect = photo_isSelect;
		this.photo_isHeightrLarge = photo_isHeightrLarge;
		this.photo_time = photo_time;
		this.photo_sequence = photo_sequence;
	}

	public int getPhoto_id() {
		return photo_id;
	}

	public void setPhoto_id(int photo_id) {
		this.photo_id = photo_id;
	}

	public int getTheme_id() {
		return theme_id;
	}

	public void setTheme_id(int theme_id) {
		this.theme_id = theme_id;
	}
	
	public int getDtheme_id() {
		return dtheme_id;
	}

	public void setDtheme_id(int dtheme_id) {
		this.dtheme_id = dtheme_id;
	}

	public int getCouple_id() {
		return couple_id;
	}

	public void setCouple_id(int couple_id) {
		this.couple_id = couple_id;
	}

	public String getPhoto_path() {
		return photo_path;
	}

	public void setPhoto_path(String photo_path) {
		this.photo_path = photo_path;
	}

	public int getPhoto_isPublic() {
		return photo_isPublic;
	}

	public void setPhoto_isPublic(int photo_isPublic) {
		this.photo_isPublic = photo_isPublic;
	}

	public int getPhoto_isHeightrLarge() {
		return photo_isHeightrLarge;
	}

	public void setPhoto_isHeightrLarge(int photo_isHeightrLarge) {
		this.photo_isHeightrLarge = photo_isHeightrLarge;
	}

	public String getPhoto_time() {
		return photo_time;
	}

	public void setPhoto_time(String photo_time) {
		this.photo_time = photo_time;
	}

	public int getPhoto_sequence() {
		return photo_sequence;
	}

	public void setPhoto_sequence(int photo_sequence) {
		this.photo_sequence = photo_sequence;
	}

	public String getPhoto_url() {
		return photo_url;
	}

	public void setPhoto_url(String photo_url) {
		this.photo_url = photo_url;
	}

	public void setPhoto_inpath(String photo_inpath) {
		this.photo_inpath = photo_inpath;
	}
	public int getPhoto_isSelect() {
		return photo_isSelect;
	}
	public void setPhoto_isSelect(int photo_isSelect) {
		this.photo_isSelect = photo_isSelect;
	}
	
	public String getPhoto_inpath() {
		if (photo_inpath == null) {
			photo_inpath = DataNetUtils.getInternalStorePath() + getTheme_id()
					+ "_" + getPhoto_id();
		}
		if (photo_inpath.length() < 3) {
			photo_inpath = DataNetUtils.getInternalStorePath() + getTheme_id()
					+ "_" + getPhoto_id();
		}
		if (new File(photo_inpath).exists() == false)
			photo_inpath = "";

		return photo_inpath;
	}

}