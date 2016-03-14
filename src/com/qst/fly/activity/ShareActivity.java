/** 
 * @Title: ShareActivity.java 
 * @Package com.qst.fly.activity 
 * @Description: TODO 
 * @author yanfeizhao 417470640@qq.com 
 * @date 2016-3-14 下午4:29:05 
 * @version V1.0 
 */  
package com.qst.fly.activity;

import com.qst.fly.R;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.ImageView;

/** 
 * @Title: ShareActivity.java 
 * @Package com.qst.fly.activity 
 * @Description: TODO 
 * @author yanfeizhao 417470640@qq.com 
 * @date 2016-3-14 下午4:29:05 
 * @version V1.0 
 */
public class ShareActivity extends Activity implements OnClickListener{
	
	private ImageView mImgBack;
	private ImageView mImgTakePhoto;
	private ImageView mImgWeChat;
	private ImageView mImgFrinds;
	private ImageView mImgWeibo;
	private ImageView mImgQQ;
	private ImageView mImgQQZone;
	
	
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);// 去掉标题栏
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_share);
		
		initView();
	}




	private void initView() {
		// TODO Auto-generated method stub
		mImgBack=(ImageView) findViewById(R.id.img_back);
		mImgTakePhoto=(ImageView) findViewById(R.id.img_take_photo);
		mImgWeChat=(ImageView) findViewById(R.id.img_wechat);
		mImgFrinds=(ImageView) findViewById(R.id.img_friends);
		mImgWeibo=(ImageView) findViewById(R.id.img_weibo);
		mImgQQ=(ImageView) findViewById(R.id.img_qq);
		mImgQQZone=(ImageView) findViewById(R.id.img_qq_zone);
		
		mImgBack.setOnClickListener(this);
		mImgTakePhoto.setOnClickListener(this);
		mImgWeChat.setOnClickListener(this);
		mImgFrinds.setOnClickListener(this);
		mImgWeibo.setOnClickListener(this);
		mImgQQ.setOnClickListener(this);
		mImgQQZone.setOnClickListener(this);
		
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.img_back:
			
			break;
		case R.id.img_take_photo:
			
			break;
		case R.id.img_wechat:
			
			break;
		case R.id.img_friends:
			
			break;
		case R.id.img_weibo:
			
			break;
		case R.id.img_qq:
			
			break;
		case R.id.img_qq_zone:
				
				break;
				

		default:
			break;
		}
	}

}
