package com.qst.fly.activity;

import java.io.File;
import java.util.Date;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.qst.fly.R;
import com.qst.fly.application.MiaoPaiApplication;
import com.qst.fly.config.Configuration;
import com.qst.fly.utils.BitmapUtil;
import com.qst.fly.utils.CameraOperationHelper;
import com.qst.fly.utils.FileUtils;
import com.qst.fly.utils.StringUtils;
import com.umeng.analytics.MobclickAgent;

/**
 * @author NoahZu
 * @version
 */
public class PicturePreviewActivity extends BaseActivity implements OnClickListener {

	public static final String SAVED_PATH = "savedPath";

	private ImageView imageView;

	private ImageView retakeBtn;
	private ImageView shareBtn;

	private Bitmap bitmap;
	private Bitmap mDecorationBitmap;

	private String path;
	private String decorationPath;
	private String savedPath;
	
	private ProgressBar mSavingPb;

	Intent intent ;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		MobclickAgent.setDebugMode(true);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_preview);
		initView();
		intent = getIntent();
		processBitmap();
	}

	/**
	 * 对图片做处理
	 */
	private void processBitmap() {
		final int cameraId = intent.getIntExtra(CameraPreviewActivity.CAMERA_ID, CameraOperationHelper.FRONT_CAMERA);
		path = intent.getStringExtra(CameraPreviewActivity.PICPATH);
		decorationPath = intent.getStringExtra(CameraPreviewActivity.DECORATION);
		
		bitmap = BitmapFactory.decodeFile(path);
		mDecorationBitmap = BitmapFactory.decodeFile(decorationPath);
		
		if (cameraId == CameraOperationHelper.FRONT_CAMERA) {
			bitmap = BitmapUtil.roateImage(bitmap, -90f);// 翻转
			bitmap = BitmapUtil.mirrorRoate(bitmap);// 对图片做镜面翻转
		} else {
			bitmap = BitmapUtil.roateImage(bitmap, 90f);// 翻转
		}
		imageView.post(new Runnable() {
			
			@Override
			public void run() {
				bitmap = BitmapUtil.scaleImage(bitmap, imageView.getWidth(), imageView.getHeight());// 缩放图片
				int xPosition = bitmap.getWidth() - mDecorationBitmap.getWidth() - 15;
				int yPosition = bitmap.getHeight() - mDecorationBitmap.getHeight() - 10;

				bitmap = BitmapUtil.mergeBitmap(bitmap, mDecorationBitmap, xPosition, yPosition);// 合并图片
				bitmap = BitmapUtil.compressImage(bitmap, 300);
				imageView.setImageBitmap(bitmap);
			}
		});
		
	}

	private void initView() {
		imageView = (ImageView) findViewById(R.id.img_preview);
		retakeBtn = (ImageView) findViewById(R.id.btn_retake);
		shareBtn = (ImageView) findViewById(R.id.btn_share);
		mSavingPb = (ProgressBar) findViewById(R.id.pb_saving);
		
		retakeBtn.setOnClickListener(this);
		shareBtn.setOnClickListener(this);
	}	

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_retake:
			new File(path).delete();
			new File(decorationPath).delete();//删掉创建的临时文件
			finish();
			MiaoPaiApplication.getApplication().isNeedResetData = false;
			break;
		case R.id.btn_share:
			jump2Share();
			break;
		default:
			break;
		}
	}

	/**
	 * 跳至分享
	 */
	private void jump2Share() {
		MobclickAgent.onEvent(this, "save_picture");
		new SaveBitmapTask().execute();
	}

	/**
	 * 保存图片
	 */
	private void saveBitmap() {
		if (bitmap != null) {
			File picFile = FileUtils.createFile(
					Configuration.MIAOPAI_FILE_PATH+"/"+Configuration.MIAOPAI_SAVED_PATH,
					StringUtils.formatDate(new Date(), "yyyyMMddHHmmss") + ".png");
			BitmapDrawable drawable = (BitmapDrawable) getResources().getDrawable(R.drawable.label);
			Bitmap labelBitmap = drawable.getBitmap();
			bitmap = BitmapUtil.mergeBitmap(bitmap, labelBitmap, 20, bitmap.getHeight() - labelBitmap.getHeight() - 20);
			int height = (750 * bitmap.getHeight()) / bitmap.getWidth();
			bitmap = BitmapUtil.scaleImage(bitmap, 750, height);
			BitmapUtil.saveBitmap(bitmap, picFile.getAbsolutePath());
			savedPath = picFile.getAbsolutePath();
		}
	}

	/**
	 * 保存图片比较耗时，在异步类中执行
	 * @author Administrator
	 *
	 */
	public class SaveBitmapTask extends AsyncTask<Void, Integer, Void> {
		
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			mSavingPb.setVisibility(View.VISIBLE);
		}
		@Override
		protected Void doInBackground(Void... params) {
			saveBitmap();
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			mSavingPb.setVisibility(View.GONE);
			Intent intent = new Intent(PicturePreviewActivity.this, ShareActivity.class);
			intent.putExtra(SAVED_PATH, savedPath);
			startActivity(intent);
		}
	}
}
