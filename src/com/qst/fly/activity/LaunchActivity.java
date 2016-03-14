package com.qst.fly.activity;

import com.qst.fly.R;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
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
	}

	private void enterCameraPreview() {
		//TODO 进入照相机界面
		
	}


	private void initView() {
		mLaunchImage = (ImageView) findViewById(R.id.img_launch);
		mLaunchImageBack = (ImageView) findViewById(R.id.img_launch_back);
	}
	
	
}
