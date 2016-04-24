package com.qst.fly.db;

import java.util.ArrayList;
import java.util.List;

import com.qst.fly.entity.Picture;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
* @author smallzoo
* @version
* @date 2016年3月17日 下午1:49:47
* 类说明
*/
public class SqliteHelper extends SQLiteOpenHelper {

	private static final String CREATE_THEME_TABLE = "create table theme_table ("
			+ "_id integer primary key autoincrement,"
			+ "category integer not null,"
			+ "title varchar(128) not null UNIQUE ON CONFLICT REPLACE,"//添加这句以后如果title重复的话会替换
			+ "path varchar(512) not null,"
			+ "isonline integer not null);";
	private static final String CREATE_PICTURE_TABLE = "create table picture_table("
			+ "_id integer primary key autoincrement,"
			+ "path varchar(512));";
	private static final String TAG = "SqliteHelper";
	
	public SqliteHelper(Context context, String name, CursorFactory factory, int version) {
		super(context, name, factory, version);
		
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(CREATE_THEME_TABLE);
		db.execSQL(CREATE_PICTURE_TABLE);
	}

	public static final int ONLINE = 0;
	public static final int LOCAL = 1;
	/**
	 * 查询图片
	 * @param category
	 * @param onLine 
	 * @return
	 */
	public List<Picture> getPicturesByCategory(int category,int onLine){
		List<Picture> pictures = new ArrayList<Picture>();
		String sql = "select * from theme_table where category = " + category +" and isonline = "+onLine+";";
		Cursor cursor = getWritableDatabase().rawQuery(sql, new String[]{});
		while(cursor.moveToNext()){
			Picture picture = new Picture(cursor.getString(3), cursor.getString(2));
			pictures.add(picture);
		}
		cursor.close();
		return pictures;
	}
	/**
	 * 查询所有图片
	 * @return
	 */
	public List<Picture> getAllPictures(){
		List<Picture> pictures = new ArrayList<Picture>();
		String sql = "select * from theme_table;";
		Cursor cursor = getWritableDatabase().rawQuery(sql, new String[]{});
		while(cursor.moveToNext()){
			Picture picture = new Picture(cursor.getInt(1),cursor.getString(3), cursor.getString(2));
			pictures.add(picture);
		}
		cursor.close();
		return pictures;
	}
	/**
	 * 查询所有在线图片
	 * @return
	 */
	public List<Picture> getAllOnLinePictures(int isOnline){
		List<Picture> pictures = new ArrayList<Picture>();
		String sql = "select * from theme_table where isonline = "+isOnline+";";
		Cursor cursor = getWritableDatabase().rawQuery(sql, new String[]{});
		while(cursor.moveToNext()){
			Picture picture = new Picture(cursor.getInt(1),cursor.getString(3), cursor.getString(2));
			pictures.add(picture);
		}
		cursor.close();
		return pictures;
	}
	/**
	 * 添加图片
	 * @param category
	 * @param picture
	 */
	public void addPicture(Picture picture){
		String sql;
		if(picture.img.contains("http://")){
			sql = "insert into theme_table values(null,"+picture.category+",'"+picture.title+"','"+picture.img+"',"+ONLINE+");";			
		}else{
			sql = "insert into theme_table values(null,"+picture.category+",'"+picture.title+"','"+picture.img+"',"+LOCAL+");";
		}
		getWritableDatabase().execSQL(sql);
	}
	/**
	 * 判断图片是否存在
	 * @param picture
	 */
	public boolean isPictureExits(Picture picture){
		String sql = "select * from theme_table where title = " + picture.title;
		Cursor cursor = getWritableDatabase().rawQuery(sql, new String[]{});
		while(cursor.moveToNext()){
			return true;
		}
		cursor.close();
		return false;
	}
	/**
	 * 清除掉非本地的图片，慎用
	 */
	public void clearDownloadPic(){
		
	}
	
	//TODO delete
	public void deletePicture(){
		
	}
	
	/**
	 * 
	 * @param picture
	 */
	public void modifyPicture(Picture picture){
		
	}
	@Override
	public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {
		
	}

	/**
	 * 添加数据
	 */
	public void addJSOn(String json) {
		ContentValues values = new ContentValues();
		values.put("path", json);
		getWritableDatabase().insert("picture_table", null, values);
		values.clear();
	}

	/**
	 * 更新数据
	 */
	public void updateJSon(
			String json) {
		ContentValues values = new ContentValues();
		values.put("path", json);
		getWritableDatabase().update("picture_table", values, null, null);
		values.clear();
	}

	/**
	 * 查询json字符串
	 */
	public String queryJson() {
		
		List<String> list = new ArrayList<String>();
		Cursor mCursor = getWritableDatabase().rawQuery(
				"select * from picture_table;", new String[]{});
		if (mCursor != null) {
			while (mCursor.moveToNext()) {
				String json = mCursor.getString(mCursor.getColumnIndex("path"));

				list.add(json);
			}
			mCursor.close();
		}
		if(list.size() > 0){
			return list.get(0);			
		}
		else{
			return null;
		}
	}
	

}
