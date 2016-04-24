package com.qst.fly.entity;
/**
* @author NoahZu
* @version
* @date 2016年3月14日 下午5:18:44
* 类说明
*/
public class Picture {
	public static final int THEME_TYPE_ALBUM = 0;
	public static final int THEME_TYPE_YANYI = 1;
	public static final int THEME_TYPE_CARTOON = 2;
	public static final int THEME_TYPE_SUPER_STAR = 3;
	public static final int THEME_TYPE_ANIMAL = 4;
	
	
	public int category;
	public String img;//路径
	public String title;
	
	public Picture(String path){
		this.img = path;
	}
	public Picture(String path,String title){
		this.img = path;
		this.title = title;
	}
	public Picture(int category, String img, String title) {
		super();
		this.category = category;
		this.img = img;
		this.title = title;
	}
}
