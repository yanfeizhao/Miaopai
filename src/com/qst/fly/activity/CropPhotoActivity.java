package com.qst.fly.activity;

import java.io.FileNotFoundException;
import java.io.IOException;

import com.qst.fly.R;
import com.qst.fly.widget.CropImageView;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.Toast;

public class CropPhotoActivity extends Activity implements OnClickListener{
	private static final String TAG = "CropPhotoActivity";

	private CropImageView mCropImage;
	private Button mBtnConfirm;
	
	private Bitmap mBitmap;
	
	protected void onCreate(android.os.Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_cropphoto);
		
		getBitmap();
		initView();
	}
	
	public void initView() {
		mCropImage = (CropImageView)findViewById(R.id.img_tobe_crop);
		BitmapDrawable drawable = new BitmapDrawable(mBitmap);
		mCropImage.setDrawable(drawable, 300, 300);
		mBtnConfirm = (Button)findViewById(R.id.btn_confirm);
		mBtnConfirm.setOnClickListener(this);
	}

	public void getBitmap() {
		
		Intent intent = getIntent();
		// �õ����ü�ͼƬ��uri
		Uri uri = intent.getParcelableExtra("uri");
		if (uri == null) {
			Toast.makeText(getApplication(), "��ȡͼƬʧ�ܣ�", Toast.LENGTH_SHORT).show();
			return;
		}
		try {
			// �õ����ü�ͼƬ��bitmap����
			mBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void onClick(View v) {
		Intent intent = new Intent();
		// �õ��ü����ͼƬ
		Bitmap bitmap = mCropImage.getCropImage();
		intent.putExtra("cropedPhoto", bitmap);
		setResult(RESULT_OK, intent);
		finish();
	}
}
