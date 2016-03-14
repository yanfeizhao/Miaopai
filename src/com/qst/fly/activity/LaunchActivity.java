package com.qst.fly.activity;

import java.io.File;
import java.util.concurrent.ExecutionException;

import com.bumptech.glide.Glide;
import com.qst.fly.R;
import com.qst.fly.config.Configuration;
import com.qst.fly.utils.BitmapUtil;
import com.qst.fly.utils.ImageCreator;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.widget.ImageView;

/**
* @author NoahZu
* @version
* @date 2016年3月14日 下午3:09:27
* 类说明
*/
public class LaunchActivity extends BaseActivity {
	
	
	private ImageView mLaunchImage;
	private ImageView mLaunchImageBack;
	
	private File currentFile;
	private File themeFile;
	private File tempFile;
	private File saveFile;

	//TODO 启动动画
	//TODO 播放音频
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_launch);
		
		
		
		initView();
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				try {
					Thread.sleep(3000);
					runOnUiThread(new Runnable() {
						
						@Override
						public void run() {
							enterCameraPreview();
						}
					});
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				
			}
		}).start();
		initFile();
		new MyLoadPicture(getApplicationContext(),themeFile).start();//开始下载图片
	}

	

	private void initFile() {
		currentFile = new File(Environment.getExternalStorageDirectory(),Configuration.MIAOPAI_FILE_PATH);
		if(!currentFile.exists()){
			currentFile.mkdir();
			tempFile = new File(currentFile,"temp");
			saveFile = new File(currentFile,"saved");
			themeFile = new File(currentFile,"theme");
			tempFile.mkdir();
			saveFile.mkdir();
			themeFile.mkdir();
		}
	}

	private void enterCameraPreview() {
		//TODO 进入照相机界面
		Intent intent = new Intent(LaunchActivity.this,CameraPreviewActivity.class);
		startActivity(intent);
		finish();
	}


	private void initView() {
		mLaunchImage = (ImageView) findViewById(R.id.img_launch);
		mLaunchImageBack = (ImageView) findViewById(R.id.img_launch_back);
	}
	
	
	static class MyLoadPicture extends Thread{
		private Context context;
		private File themeFile;
		public MyLoadPicture(Context context,File themeFile){
			this.context = context;
			this.themeFile = themeFile;
		}
		
		@Override
		public void run() {
			super.run();
			loadPicture();
		}
		
		
		
		private void loadPicture() {
			for(int i = 0;i<ImageCreator.mPictures.size();i++){
				try {
					Bitmap myBitmap = Glide
							.with(this.context)
						    .load(ImageCreator.mPictures.get(i).picturePath) 
						    .asBitmap() 
						    .centerCrop() 
						    .into(300,300) 
						    .get();
				
					File picFile = new File(themeFile,"theme_test"+i+".jpg");
					BitmapUtil.saveBitmap(myBitmap, picFile.getAbsolutePath());
				} catch (InterruptedException e) {
					e.printStackTrace();
				} catch (ExecutionException e) {
					e.printStackTrace();
				} 
			}
		}
		
	}
	
	
}
