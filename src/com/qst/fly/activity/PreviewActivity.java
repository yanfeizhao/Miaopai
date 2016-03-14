package com.qst.fly.activity;

import com.qst.fly.R;
import com.qst.fly.utils.BitmapUtil;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

/**
* @author NoahZu
* @version
*/
public class PreviewActivity extends Activity implements OnClickListener{
	
	private String picPath;
	
	private ImageView imageView;

	private Button retakeBtn;
	private Button shareBtn;
	private Button saveBtn;
	
	private Bitmap bitmap;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_preview);
		initView();
		getPicPath(getIntent());
	}

	private void initView() {
		imageView = (ImageView) findViewById(R.id.img_preview);
		retakeBtn = (Button) findViewById(R.id.btn_retake);
		shareBtn = (Button) findViewById(R.id.btn_share);
		saveBtn = (Button) findViewById(R.id.btn_save);
		
		retakeBtn.setOnClickListener(this);
	}

	private void getPicPath(Intent intent) {
		String path = intent.getStringExtra(CameraPreviewActivity.PICPATH);
		bitmap = BitmapFactory.decodeFile(path);
		bitmap = BitmapUtil.roateImage(bitmap, -90f);//翻转
		bitmap = BitmapUtil.mirrorRoate(bitmap);//对图片做镜面翻转
		imageView.post(new Runnable() {
			@Override
			public void run() {
				bitmap = BitmapUtil.scaleImage(bitmap, imageView.getWidth(), imageView.getHeight());//����ͼƬ
				imageView.setImageBitmap(bitmap);
			}
		});
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_retake:
			//TODO ɾ�����ص���Ƭ
			finish();
			break;
		case R.id.btn_save:
			Toast.makeText(this, "��Ƭ�Ѿ�����" + picPath, Toast.LENGTH_LONG).show();
			break;
		case R.id.btn_share:
			//TODO ����
			break;
		default:
			break;
		}
	}
}
