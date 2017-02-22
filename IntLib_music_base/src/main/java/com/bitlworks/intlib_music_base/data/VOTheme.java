package com.bitlworks.intlib_music_base.data;

import java.io.Serializable;

public class VOTheme implements Serializable {
	public static final int THEME_TYPE_DEFAULT = 0;
	public static final int THEME_TYPE_DYNAMIC = 1;
	
	private static final long serialVersionUID = 5463618673556531282L;

	private int theme_id;
	private int couple_id;
	private String theme_path;
	private String theme_name;
	private String theme_description;
	private int theme_effect;
	private int theme_sequence;
	private int theme_isHide;
	private int theme_type;
	
	public VOTheme(int theme_id, int couple_id, String theme_path,
								 String theme_name, String theme_description, int theme_effect,
								 int theme_sequence, int theme_isHide, int theme_type) {
		super();
		this.theme_id = theme_id;
		this.couple_id = couple_id;
		this.theme_path = theme_path;
		this.theme_name = theme_name;
		this.theme_description = theme_description;
		this.theme_effect = theme_effect;
		this.theme_sequence = theme_sequence;
		this.theme_isHide = theme_isHide;
		this.theme_type = theme_type;
	}
	public VOTheme() {

	}

	public int getTheme_id() {
		return theme_id;
	}
	public void setTheme_id(int theme_id) {
		this.theme_id = theme_id;
	}
	public int getCouple_id() {
		return couple_id;
	}
	public void setCouple_id(int couple_id) {
		this.couple_id = couple_id;
	}
	public String getTheme_path() {
		return theme_path;
	}
	public void setTheme_path(String theme_path) {
		this.theme_path = theme_path;
	}
	public String getTheme_name() {
		return theme_name;
	}
	public void setTheme_name(String theme_name) {
		this.theme_name = theme_name;
	}
	public String getTheme_description() {
		return theme_description;
	}
	public void setTheme_description(String theme_description) {
		this.theme_description = theme_description;
	}
	public int getTheme_effect() {
		return theme_effect;
	}
	public void setTheme_effect(int theme_effect) {
		this.theme_effect = theme_effect;
	}
	public int getTheme_sequence() {
		return theme_sequence;
	}
	public void setTheme_sequence(int theme_sequence) {
		this.theme_sequence = theme_sequence;
	}
	public int getTheme_isHide() {
		return theme_isHide;
	}
	public void setTheme_isHide(int theme_isHide) {
		this.theme_isHide = theme_isHide;
	}
	public int getTheme_type() {
		return theme_type;
	}
	public void setTheme_type(int theme_type) {
		this.theme_type = theme_type;
	}

}