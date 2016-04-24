package com.qst.fly.activity;

import com.qst.fly.R;
import com.qst.fly.entity.PhotoUpImageBucket;
import com.qst.fly.entity.PhotoUpImageItem;
import com.qst.fly.fragment.AlbumDetailFragment;
import com.qst.fly.fragment.AlbumFragment;
import com.qst.fly.fragment.CropFragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

public class AlbumActivity extends FragmentActivity {
	public static final String CROPED_PHOTO = "CROPED_PHOTO";
	Fragment mAlbumFragment;
	Fragment mAlbumDetailFragment;
	Fragment mCropFragment;

	public static final int ALBUM_FRAGMENT_POSITION  = 0;
	public static final int ALBUM_DETAIL_FRAGMENT_POSITION = 1;
	public static final int CROP_FRAGMENT_POSITION = 2;
	
	private int currentPosition = ALBUM_FRAGMENT_POSITION;
	
	private ImageView mBackImage;
	private TextView mTitleText;
	private PhotoUpImageBucket mPhotoUpImageBucket;
	private PhotoUpImageItem mPhotoUpImageItem;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_album);

		initView();
		initFragment();
	}

	private void initView() {
		mTitleText = (TextView) findViewById(R.id.text_album_title);
		mBackImage = (ImageView) findViewById(R.id.img_album_back);
		mBackImage.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				back();
			}
		});
		mTitleText.setText("选择相册");
	}

	private void initFragment() {
		mAlbumFragment = new AlbumFragment();
		mAlbumDetailFragment = new AlbumDetailFragment();
		mCropFragment = new CropFragment();
		jump2Fragment(ALBUM_FRAGMENT_POSITION);
	}

	public void jump2Fragment(int position){
		switch (position) {
		case ALBUM_FRAGMENT_POSITION:
			mTitleText.setText("选择相册");
			getSupportFragmentManager().beginTransaction().replace(R.id.frame_content, mAlbumFragment).commit();
			currentPosition = 0;
			break;
		case ALBUM_DETAIL_FRAGMENT_POSITION:
			mTitleText.setText("选择文件");
			getSupportFragmentManager().beginTransaction().replace(R.id.frame_content, mAlbumDetailFragment).commit();
			currentPosition = 1;
			break;
		case CROP_FRAGMENT_POSITION:
			mTitleText.setText("");
			getSupportFragmentManager().beginTransaction().replace(R.id.frame_content, mCropFragment).commit();
			currentPosition = 2;
			break;
		}
	}

	/**
	 * 完成选择
	 * 
	 * @param bitmap
	 */
	public void finishSelect(Bitmap bitmap) {
		Intent intent = new Intent(AlbumActivity.this, CameraPreviewActivity.class);
		intent.putExtra(CROPED_PHOTO, bitmap);
		setResult(RESULT_OK, intent);
		finish();
	}

	public void back() {
		if(currentPosition > 0){
			currentPosition -= 1;
			jump2Fragment(currentPosition);
		}else if(currentPosition == 0){
			finish();
			return;
		}
	}

	public void setPhotoUpImageBucket(PhotoUpImageBucket photoUpImageBucket) {
		this.mPhotoUpImageBucket = photoUpImageBucket;
	}

	public PhotoUpImageBucket getPhotoUpImageBucket() {
		return this.mPhotoUpImageBucket;
	}

	public void setPhotoUpImageItem(PhotoUpImageItem photoUpImageItem) {
		this.mPhotoUpImageItem = photoUpImageItem;
	}

	public PhotoUpImageItem getPhotoUpImageItem() {
		return this.mPhotoUpImageItem;
	}
}
