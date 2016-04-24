package com.qst.fly.activity;

import java.io.File;

import com.qst.fly.R;
import com.qst.fly.config.Configuration;
import com.qst.fly.service.FetchPictureService;
import com.qst.fly.widget.GifView;
import com.qst.fly.widget.GifView.OnGifFinishListener;
import com.qst.fly.widget.GifView.OnGifStartListener;

import android.content.Intent;
import android.media.AudioManager;
import android.media.SoundPool;
import android.media.SoundPool.OnLoadCompleteListener;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

/**
 * @author NoahZu
 * @version
 * @date 2016年3月14日 下午3:09:27 类说明
 */
public class LaunchActivity extends BaseActivity {

	private GifView mGifView;
	private File currentFile;
	private File themeFile;
	private File tempFile;
	private File saveFile;
	private SoundPool launchSound;
	private boolean mIsSupportAnimation = false;
	
	private ImageView mBackImage;
	private ImageView mIconImage;

	Handler handler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			if(!mIsSupportAnimation){
				enterCameraPreview();
				finish();
			}
		};
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		int flag = WindowManager.LayoutParams.FLAG_FULLSCREEN;
		Window window = LaunchActivity.this.getWindow();
		window.setFlags(flag, flag);
		setContentView(R.layout.activity_launch);

		initAnimation();
		initLaunchSound();
		initFile();
		launch();
		handler.sendEmptyMessageDelayed(0x000,3000);
	}

	private void initAnimation() {
		mBackImage = (ImageView) findViewById(R.id.launch_back);
		mIconImage = (ImageView) findViewById(R.id.launch_icon);
		mGifView = (GifView) findViewById(R.id.gifview);
		mGifView.setMovieResource(R.drawable.beginmovie);
		mGifView.setOnGifFinishListener(new OnGifFinishListener() {			
			@Override
			public void onFinish() {
				enterCameraPreview();
				finish();
			}
		});
		mGifView.setOnGifStartListener(new OnGifStartListener() {
			
			@Override
			public void onStart() {
				mIsSupportAnimation = true;
				mBackImage.setVisibility(View.GONE);
				mIconImage.setVisibility(View.GONE);
			}
		});
		
	}

	private void launch() {
		// 开启后台服务来去获取图片
		Intent intent = new Intent(LaunchActivity.this, FetchPictureService.class);
		startService(intent);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		mGifView = null;
		launchSound.release();
		System.gc();
	}

	/**
	 * 初始化声音
	 */
	private void initLaunchSound() {
		launchSound = new SoundPool(21, AudioManager.STREAM_SYSTEM, 10);
		launchSound.load(this, R.raw.launch_sound, 1);
		launchSound.setOnLoadCompleteListener(new OnLoadCompleteListener() {

			@Override
			public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {
				launchSound.play(1, 1, 1, 0, 0, 1f);
			}
		});
	}

	/**
	 * 创建所需要的文件
	 */
	private void initFile() {
		currentFile = new File(Environment.getExternalStorageDirectory(), Configuration.MIAOPAI_FILE_PATH);
		if (!currentFile.exists()) {
			currentFile.mkdir();
			tempFile = new File(currentFile, Configuration.MIAOPAI_TEMP_PATH);
			saveFile = new File(currentFile, Configuration.MIAOPAI_SAVED_PATH);
			themeFile = new File(currentFile, Configuration.MIAOPAI_THEME_PATH);
			tempFile.mkdir();
			saveFile.mkdir();
			themeFile.mkdir();
		}
	}

	private void enterCameraPreview() {
		Intent intent = new Intent(LaunchActivity.this, CameraPreviewActivity.class);
		startActivity(intent);
		finish();
	}
}
