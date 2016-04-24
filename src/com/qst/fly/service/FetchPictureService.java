package com.qst.fly.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.qst.fly.application.MiaoPaiApplication;
import com.qst.fly.config.Configuration;
import com.qst.fly.db.SqliteHelper;
import com.qst.fly.entity.Picture;
import com.qst.fly.entity.Theme;
import com.qst.fly.utils.BitmapUtil;
import com.qst.fly.utils.JsonUtils;
import com.qst.fly.utils.LogUtils;
import com.qst.fly.utils.NetUtil;
import com.qst.fly.utils.OkHttpUtils;
import com.qst.fly.utils.OkHttpUtils.ResultCallback;
import com.qst.fly.utils.SharedPreferenceUtil;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Picasso.LoadedFrom;
import com.squareup.picasso.Target;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Binder;
import android.os.Environment;
import android.os.IBinder;
import android.util.Log;
import android.widget.ShareActionProvider;

/**
* @author smallzoo
* @version
* @date 2016年3月17日 下午2:55:55
* 类说明
*/
public class FetchPictureService extends Service {
	
	private static final String TAG = "FetchPictureService";
	private SqliteHelper mSqliteHelper;
	
	private static final String IS_FIRST_ENTER = "isFristEnter";
	private static final String IS_FIRST_ENTER_TAG = "isFristEnterTag";
	public static final String SAVED_INSTANCE = "SAVED_INSTANCE";
	public static final String SAVED_DATE = "savedDate";
	public static final String SAVED_TYPE = "savedType";
	public static final String SAVED_TITLE = "savedTitle";
	
	private long mExitDate;
	private int mType;
	private String mTitle;


	@Override
	public void onCreate() {
		readSavedInstance();
		mSqliteHelper = new SqliteHelper(this,Configuration.DB_NAME, null, 1);
		super.onCreate();
	}

	/**
	 * 从sp读取配置
	 */
	private void readSavedInstance() {
		SharedPreferenceUtil.setSharedPreferenceName(SAVED_INSTANCE);
		
		mExitDate = SharedPreferenceUtil.getLong(this, SAVED_DATE);
		mType = SharedPreferenceUtil.getInt(this, SAVED_TYPE);
		mTitle = SharedPreferenceUtil.getString(this, SAVED_TITLE);
	}

	@Override
	public void onRebind(Intent intent) {
		LogUtils.d(TAG, "onReBind");
		super.onRebind(intent);
	}

	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		new DownLoadPictureTask().execute();
		return super.onStartCommand(intent, flags, startId);
	}
	
	
	/**
	 * 判断是否是第一次进入app
	 * @return
	 */
	private boolean isFirstOpen() {
		if(MiaoPaiApplication.getApplication().getIsFirstOpen() == -1){
			//为检查，去检查
			SharedPreferenceUtil.setSharedPreferenceName(IS_FIRST_ENTER);
			boolean first = SharedPreferenceUtil.getBoolean(this, IS_FIRST_ENTER_TAG, true);
			SharedPreferenceUtil.addInSharePreference(this, IS_FIRST_ENTER_TAG, false);
			MiaoPaiApplication.getApplication().setIsFirstEnter(first);
			return first;
		}else if(MiaoPaiApplication.getApplication().getIsFirstOpen() == 0){
			return false;
		}else{
			return true;
		}
	}
	
	
	public class DownLoadPictureTask extends AsyncTask<Void, Integer, Void>{
		
		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			stopSelf();
		}
		@Override
		protected Void doInBackground(Void... params) {
			if(isFirstOpen()){
				File yanyiFile = new File(Environment.getExternalStorageDirectory(),Configuration.MIAOPAI_FILE_PATH+"/"+Configuration.MIAOPAI_THEME_PATH+"/01");
				copyFilesFassets(FetchPictureService.this, "01", yanyiFile.getAbsolutePath());
				
				File starFile = new File(Environment.getExternalStorageDirectory(),Configuration.MIAOPAI_FILE_PATH+"/"+Configuration.MIAOPAI_THEME_PATH+"/02");
				copyFilesFassets(FetchPictureService.this, "02", starFile.getAbsolutePath());
				
				File cartoonFile = new File(Environment.getExternalStorageDirectory(),Configuration.MIAOPAI_FILE_PATH+"/"+Configuration.MIAOPAI_THEME_PATH+"/03");
				copyFilesFassets(FetchPictureService.this, "03", cartoonFile.getAbsolutePath());
				
				File animalFile = new File(Environment.getExternalStorageDirectory(),Configuration.MIAOPAI_FILE_PATH+"/"+Configuration.MIAOPAI_THEME_PATH+"/04");
				copyFilesFassets(FetchPictureService.this, "04", animalFile.getAbsolutePath());
				System.gc();
				//将本地的图片添加到数据库
				addLocalPictureToDb();	
			}
			if(NetUtil.isNetConnected(FetchPictureService.this)){	
				loadNetPicture();
			}		
			return null;
		}
	}
	
	/**
	 * 加载网络图片到数据库，只是具体图片的信息，何时加载图片呢？？
	 */
	private void loadNetPicture() {
		OkHttpUtils.get(Configuration.BASE_THEME_PIC_URL+"01", new ResultCallback<String>() {
			
			@Override
			public void onSuccess(String response) {
				Theme theme = JsonUtils.deserialize(response, Theme.class);
				for(final Picture pic : theme.result){
					pic.category = Picture.THEME_TYPE_YANYI;
					Log.d(TAG, "category"+pic.category+",title:"+pic.title+" start");
					mSqliteHelper.addPicture(pic);
				}
			}

			@Override
			public void onFailure(Exception e) {
				
			}
		});
		OkHttpUtils.get(Configuration.BASE_THEME_PIC_URL+"02", new ResultCallback<String>() {
			@Override
			public void onSuccess(String response) {
				Theme theme = JsonUtils.deserialize(response, Theme.class);
				for(final Picture pic : theme.result){
					pic.category = Picture.THEME_TYPE_CARTOON;
					mSqliteHelper.addPicture(pic);
				}
			}

			@Override
			public void onFailure(Exception e) {
				
			}
		});
		OkHttpUtils.get(Configuration.BASE_THEME_PIC_URL+"03", new ResultCallback<String>() {

			@Override
			public void onSuccess(String response) {
				Theme theme = JsonUtils.deserialize(response, Theme.class);
				for(final Picture pic : theme.result){
					pic.category = Picture.THEME_TYPE_SUPER_STAR;
					Log.d(TAG, "category"+pic.category+",title:"+pic.title+" add");
					mSqliteHelper.addPicture(pic);
				}
			}

			@Override
			public void onFailure(Exception e) {
				
			}
		});
		OkHttpUtils.get(Configuration.BASE_THEME_PIC_URL+"04", new ResultCallback<String>() {

			@Override
			public void onSuccess(String response) {
				Theme theme = JsonUtils.deserialize(response, Theme.class);
				for(final Picture pic : theme.result){
					pic.category = Picture.THEME_TYPE_ANIMAL;
					Log.d(TAG, "category"+pic.category+",title:"+pic.title+" add");
					mSqliteHelper.addPicture(pic);
				}
			}

			@Override
			public void onFailure(Exception e) {
				
			}
		});
	}
	
	/**
	 * 将本地图片的信息录入数据库
	 */
	private void addLocalPictureToDb() {
		File yanyiFiles = new File(Environment.getExternalStorageDirectory(),Configuration.MIAOPAI_FILE_PATH+"/"+Configuration.MIAOPAI_THEME_PATH+"/0"+Picture.THEME_TYPE_YANYI);
		File[] yanyis = yanyiFiles.listFiles();
		for(int i = 0;i<yanyis.length;i++){
			File singleFile = yanyis[i];
			String title = Configuration.yanyiTitleMaps.get(singleFile.getName());
			Picture picture = new Picture(1,singleFile.getAbsolutePath(),title);
			mSqliteHelper.addPicture(picture);
		}
		File cartoonFiles = new File(Environment.getExternalStorageDirectory(),Configuration.MIAOPAI_FILE_PATH+"/"+Configuration.MIAOPAI_THEME_PATH+"/0"+Picture.THEME_TYPE_CARTOON);
		File[] cartoons = cartoonFiles.listFiles();
		for(int i = 0;i<cartoons.length;i++){
			File singleFile = cartoons[i];
			String title = Configuration.cartoonTitleMaps.get(singleFile.getName());
			Picture picture = new Picture(2,singleFile.getAbsolutePath(),title);
			mSqliteHelper.addPicture(picture);
		}
		
		File starFiles = new File(Environment.getExternalStorageDirectory(),Configuration.MIAOPAI_FILE_PATH+"/"+Configuration.MIAOPAI_THEME_PATH+"/0"+Picture.THEME_TYPE_SUPER_STAR);
		File[] stars = starFiles.listFiles();
		for(int i = 0;i<stars.length;i++){
			File singleFile = stars[i];
			String title = Configuration.starTitleMaps.get(singleFile.getName());
			Picture picture = new Picture(3,singleFile.getAbsolutePath(),title);
			mSqliteHelper.addPicture(picture);
		}
		
		File animalFiles = new File(Environment.getExternalStorageDirectory(),Configuration.MIAOPAI_FILE_PATH+"/"+Configuration.MIAOPAI_THEME_PATH+"/0"+Picture.THEME_TYPE_ANIMAL);
		File[] animals = animalFiles.listFiles();
		for(int i = 0;i<animals.length;i++){
			File singleFile = animals[i];
			String title = Configuration.animalTitleMaps.get(singleFile.getName());
			Picture picture = new Picture(4,singleFile.getAbsolutePath(),title);
			mSqliteHelper.addPicture(picture);
		}
	}
	
	/**  
	 *  从assets目录中复制整个文件夹内容  
	 *  @param  context  Context 使用CopyFiles类的Activity 
	 *  @param  oldPath  String  原文件路径  如：/aa  
	 *  @param  newPath  String  复制后路径  如：xx:/bb/cc  
	 */ 
	public void copyFilesFassets(Context context,String oldPath,String newPath) {                      
        try {  
       String fileNames[] = context.getAssets().list(oldPath);//获取assets目录下的所有文件及目录名  
       if (fileNames.length > 0) {//如果是目录  
           File file = new File(newPath);  
           file.mkdirs();//如果文件夹不存在，则递归  
           for (String fileName : fileNames) {  
              copyFilesFassets(context,oldPath + "/" + fileName,newPath+"/"+fileName);  
           }  
       } else {//如果是文件  
           InputStream is = context.getAssets().open(oldPath);  
           FileOutputStream fos = new FileOutputStream(new File(newPath));  
           byte[] buffer = new byte[1024];  
           int byteCount=0;                 
           while((byteCount=is.read(buffer))!=-1) {//循环从输入流读取 buffer字节          
               fos.write(buffer, 0, byteCount);//将读取的输入流写入到输出流  
           }  
           fos.flush();//刷新缓冲区  
           is.close();  
           fos.close();  
       }  
   } catch (Exception e) {  
       e.printStackTrace();  
     
   }                             
}

	
	
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	} 
}
