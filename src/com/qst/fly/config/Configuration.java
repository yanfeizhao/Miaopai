package com.qst.fly.config;

import java.util.HashMap;
import java.util.Map;

/**
 * @author NoahZu
 * @version
 * @date 2016年3月14日 上午10:51:55 类说明
 */
public class Configuration {
	public static int sPictureWidth = 1080;
	public static int sPictureHeight = 1920;

	public static String MIAOPAI_FILE_PATH = "qst_miaopai";
	public static String MIAOPAI_TEMP_PATH = "temp";
	public static String MIAOPAI_SAVED_PATH = "saved";
	public static String MIAOPAI_THEME_PATH = "theme";

	public static int APP_VERSION = 1;
	/**
	 * 示例图片接口
	 */
	public static String EXAMPLE_PIC_URL = "http://api.miaopai.com/m/maopaiImage.json";
	/**
	 * 装饰图片 base url 
	 */
	public static String BASE_THEME_PIC_URL = "http://api.miaopai.com/m/maopaiMaterial.json?cateid=";
	/**
	 * 判断是不是第一次进入分享页面。
	 */
	public static  boolean  FIRST_OPEN_SHARE=true;

	public static String DB_NAME = "miaopai.db";
	
	public static Map<String,String> animalTitleMaps = new HashMap<String,String>();
	public static Map<String,String> yanyiTitleMaps = new HashMap<String, String>();
	public static Map<String,String> cartoonTitleMaps = new HashMap<String, String>();
	public static Map<String,String> starTitleMaps = new HashMap<String, String>();
	
	static{
		animalTitleMaps.put("14001.jpg","@招财大法好");
		animalTitleMaps.put("14002.jpg","@韩寒的马达加斯加");
		animalTitleMaps.put("14003.jpg","@马达的拉斯维加斯");
		animalTitleMaps.put("14004.jpg","FLASH");
		animalTitleMaps.put("14005.jpg","不开心猫");
		animalTitleMaps.put("14006.jpg","兔子");
		animalTitleMaps.put("14007.jpg","哈士奇");
		animalTitleMaps.put("14008.jpg","尼克");
		animalTitleMaps.put("14009.jpg","布偶猫muffi");
		animalTitleMaps.put("14010.jpg","张崇子");
		animalTitleMaps.put("14011.jpg","张梓琳的狗");
		animalTitleMaps.put("14012.jpg","惊讶猫咪");
		animalTitleMaps.put("14013.jpg","松鼠");
		animalTitleMaps.put("14014.jpg","波斯猫Grafi");
		animalTitleMaps.put("14015.jpg","牛头梗");
		animalTitleMaps.put("14016.jpg","瓜皮");
		animalTitleMaps.put("14017.jpg","疯狂动物城");
		animalTitleMaps.put("14018.jpg","疯狂动物城市长");
		animalTitleMaps.put("14019.jpg","草泥马");
		animalTitleMaps.put("14020.jpg","长颈鹿");
		
		yanyiTitleMaps.put("11001.png","单身狗");
		yanyiTitleMaps.put("11002.png","爱你哦");
		yanyiTitleMaps.put("11003.png","猴赛雷");
		yanyiTitleMaps.put("11004.png","鬼脸");
		
		cartoonTitleMaps.put("12001.png","你TM在逗我");
		cartoonTitleMaps.put("12002.png","看我可爱吗");
		
		starTitleMaps.put("13001.png","KFC");
		starTitleMaps.put("13002.png","小李子");
	}
	
	
}
