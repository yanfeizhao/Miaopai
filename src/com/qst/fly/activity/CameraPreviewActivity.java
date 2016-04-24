package com.qst.fly.activity;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.animation.Animator;
import android.animation.Animator.AnimatorListener;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.meetme.android.horizontallistview.HorizontalListView;
import com.qst.fly.R;
import com.qst.fly.adapter.PictureSelectListAdapter;
import com.qst.fly.application.MiaoPaiApplication;
import com.qst.fly.config.Configuration;
import com.qst.fly.db.SqliteHelper;
import com.qst.fly.entity.Picture;
import com.qst.fly.utils.BitmapUtil;
import com.qst.fly.utils.CameraOperationHelper;
import com.qst.fly.utils.CameraOperationHelper.CameraOverCallback;
import com.qst.fly.utils.FileUtils;
import com.qst.fly.utils.SharedPreferenceUtil;
import com.qst.fly.utils.StringUtils;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Picasso.LoadedFrom;
import com.squareup.picasso.Target;

/**
 * @author NoahZu
 * @version
 */
public class CameraPreviewActivity extends BaseActivity implements Callback, OnClickListener, CameraOverCallback {

	private static final String CAMERA_PATH = "cameraDemos";
	private static final String TAG = "MainActivity";
	public static final String PICPATH = "picturePath";
	public static final int STATUS_SELECT_THEME = 0;
	public static final int STATUS_TAKE_PHOTO = 1;
	public static final String DECORATION = "decoration";
	public static final String CAMERA_ID = "cameraId";
	private static final int REQUEST_PHOTO_ALBUM = 5;
//	private static final int REQUEST_CROP_PHOTO = 8;


	private static final String IS_FIRST_ENTER = "isFristEnter";
	private static final String IS_FIRST_ENTER_TAG = "isFristEnterTag";

	private int currentType;
	private int previousType;
	public static String FROM = "from";
	public static String FROM_LAUNCH = "FLAG_FROM_LAUNCH";
	public static String FROM_PREVIEW = "FROM_PREVIEW";

	private int currentStatus = STATUS_SELECT_THEME;
	private String currentTitle = "none";

	private HorizontalListView mHorizontalListView;

	private List<Picture> mCurrentPictures;// 当前显示的数据源
	private boolean mShoudShowToast = true;

	private PictureSelectListAdapter mPictureAdapter;

	private ImageButton switchCameraBtn;
	private ImageButton flashBtn;
	private SurfaceView disCameraSurface;
	private RelativeLayout mAlbumLayout;
	private RelativeLayout mYanyiLayout;
	private RelativeLayout mSuperStarLayout;
	private RelativeLayout mCartoonLayout;
	private RelativeLayout mAnimalLayout;

	private LinearLayout mSelectedThemeLayout;
	private LinearLayout mThemeAndMenuLayout;
	private ImageView mSelectedThemeImage;
	private TextView mSelectedThemeText;
	private ProgressBar mPbIsTaking;

	private SurfaceHolder disCameraSurfaceHolder;
	private CameraOperationHelper cameraOperationHelper;

	private SoundPool soundPool;
	private boolean isFlashing = false;
	private boolean isFront = true;

	private SqliteHelper mSqliteHelper;
	private int mCameraId = CameraOperationHelper.FRONT_CAMERA;

	private String selectedPicturePath = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_camera);

		mSqliteHelper = new SqliteHelper(this, Configuration.DB_NAME, null, 1);
		initView();
		initSound();
		initCamera();
		initData();
		launch();
		if (isFirstOpen()) {
			showViewFindToast();
		}
	}

	public static final String SAVED_INSTANCE = "SAVED_INSTANCE";
	public static final String SAVED_DATE = "savedDate";
	public static final String SAVED_TYPE = "savedType";
	public static final String SAVED_TITLE = "savedTitle";
	private static final String IS_FROM_ALBUM = "IS_FROM_ALBUM";
	private static final String SAVED_PATH = "savedPath";
	public static final int THEME_ALBUM = 5;
	public static final int THEME_OTHER = 6;

	/**
	 * 初始化，主要是根据读取的配置决定加载的数据
	 */
	private void launch() {
		SharedPreferenceUtil.setSharedPreferenceName(SAVED_INSTANCE);
		long savedDate = SharedPreferenceUtil.getLong(this, SAVED_DATE);
		long currentDate = new Date().getTime();

		if (currentDate - savedDate > 3620000) {
			loadData(currentType);
			selectMenuItemByType(previousType, false);
			selectMenuItemByType(currentType,true);
			previousType = currentType;
			return;
		}
		final int type = SharedPreferenceUtil.getInt(this, SAVED_TYPE);
		currentType = type;
		selectMenuItemByType(previousType, false);
		selectMenuItemByType(type,true);
		previousType = currentType;
		
		int savedType = SharedPreferenceUtil.getInt(this, IS_FROM_ALBUM);
		if (savedType == THEME_ALBUM) {
			final String path = SharedPreferenceUtil.getString(this, SAVED_PATH);
			mThemeAndMenuLayout.post(new Runnable() {
				@Override
				public void run() {
					loadData(type);
					mSelectedThemeLayout.setVisibility(View.VISIBLE);
					Picasso.with(CameraPreviewActivity.this).load(new File(path)).into(mSelectedThemeImage);
					mSelectedThemeText.setText("");
					floatingMoveDown();
				}
			});
		} else {
			final String title = SharedPreferenceUtil.getString(this, SAVED_TITLE);
			mThemeAndMenuLayout.post(new Runnable() {
				@Override
				public void run() {
					loadData(type, title);
				}
			});
		}
	}

	/**
	 * 加载数据的方法
	 * 
	 * @param themeType
	 *            主题
	 */
	private void loadData(final int themeType) {
		currentType = themeType;

		mCurrentPictures.clear();
		mCurrentPictures.addAll(mSqliteHelper.getPicturesByCategory(themeType, SqliteHelper.ONLINE));
		mCurrentPictures.addAll(mSqliteHelper.getPicturesByCategory(themeType, SqliteHelper.LOCAL));
		mPictureAdapter.notifyDataSetChanged();
	}

	/**
	 * 加载数据得类
	 * 
	 * @param type
	 *            主题
	 * @param title
	 *            标题
	 */
	private void loadData(int type, String title) {
		currentType = type;
		mCurrentPictures.clear();
		mCurrentPictures.addAll(mSqliteHelper.getPicturesByCategory(type, SqliteHelper.ONLINE));
		mCurrentPictures.addAll(mSqliteHelper.getPicturesByCategory(type, SqliteHelper.LOCAL));
		mPictureAdapter.notifyDataSetChanged();
		int position = getPositionByTitle(title);
		mHorizontalListView.setSelection(position);
		if (position > -1) {
			selectTheme(position);
			currentStatus = STATUS_TAKE_PHOTO;
		}
	}

	/**
	 * 在数据源中根据标题获取position
	 * 
	 * @param title
	 * @return
	 */
	private int getPositionByTitle(String title) {
		for (int i = 0; i < mCurrentPictures.size(); i++) {
			Picture p = mCurrentPictures.get(i);
			if (p.title.equals(title)) {
				return i;
			}
		}
		return -1;
	}

	/**
	 * 检查是否是第一次进入
	 * 
	 * @return
	 */
	private boolean isFirstOpen() {
		if (MiaoPaiApplication.getApplication().getIsFirstOpen() == -1) {
			// 为检查，去检查
			SharedPreferenceUtil.setSharedPreferenceName(IS_FIRST_ENTER);
			boolean first = SharedPreferenceUtil.getBoolean(this, IS_FIRST_ENTER_TAG, true);
			SharedPreferenceUtil.addInSharePreference(this, IS_FIRST_ENTER_TAG, false);
			MiaoPaiApplication.getApplication().setIsFirstEnter(first);
			return first;
		} else if (MiaoPaiApplication.getApplication().getIsFirstOpen() == 0) {
			return false;
		} else {
			return true;
		}
	}

	/**
	 * 底部菜单移上来
	 */
	private void floatingMoveUp() {
		ObjectAnimator moveUpAnimator = ObjectAnimator.ofFloat(mThemeAndMenuLayout, "translationY", 0);
		ObjectAnimator alphaAnimator = ObjectAnimator.ofFloat(mThemeAndMenuLayout, "alpha", 0.5f, 1f);
		AnimatorSet animatorSet = new AnimatorSet();
		animatorSet.play(moveUpAnimator).with(alphaAnimator);
		animatorSet.setDuration(750);
		animatorSet.start();
		currentStatus = STATUS_SELECT_THEME;
	}

	/**
	 * 底部菜单移下去
	 */
	private void floatingMoveDown() {
		ObjectAnimator moveDownAnimator = ObjectAnimator.ofFloat(mThemeAndMenuLayout, "translationY", 0f,
				(mThemeAndMenuLayout.getHeight() * 2) / 3);
		ObjectAnimator alphaAnimator = ObjectAnimator.ofFloat(mThemeAndMenuLayout, "alpha", 1f, 0.5f);
		AnimatorSet animatorSet = new AnimatorSet();
		animatorSet.play(moveDownAnimator).with(alphaAnimator);
		animatorSet.setDuration(750);
		animatorSet.start();
		currentStatus = STATUS_TAKE_PHOTO;
		if (isFirstOpen() && mShoudShowToast) {
			showPromoteToast();
			mShoudShowToast = false;
		}
	}

	/**
	 * 初始化声音
	 */
	private void initSound() {
		soundPool = new SoundPool(21, AudioManager.STREAM_SYSTEM, 10);
		soundPool.load(this, R.raw.take_photo, 1);
	}

	/**
	 * 初始化Camera
	 */
	private void initCamera() {
		cameraOperationHelper = CameraOperationHelper.getInstance(this);
	}

	/**
	 * 初始化View
	 */
	@SuppressWarnings("deprecation")
	private void initView() {
		/*
		NewHintDialog hintDialog = new NewHintDialog(this, R.style.Theme_AudioDialog);
		hintDialog.setDialogClickListener(null);
		//TODO 监听事件
		hintDialog.show();
		*/
		mPbIsTaking = (ProgressBar) findViewById(R.id.pb_istaking);
		mThemeAndMenuLayout = (LinearLayout) findViewById(R.id.ll_select_theme);
		mSelectedThemeLayout = (LinearLayout) findViewById(R.id.ll_float_select_item);
		mSelectedThemeImage = (ImageView) findViewById(R.id.img_float_item);
		mSelectedThemeText = (TextView) findViewById(R.id.text_float_item);
		Typeface selectedThemeTextType = Typeface.createFromAsset(getAssets(), "fonts/DFWaWaGB-W5.otf");
		mSelectedThemeText.setTypeface(selectedThemeTextType);

		mAlbumLayout = (RelativeLayout) findViewById(R.id.album);
		mYanyiLayout = (RelativeLayout) findViewById(R.id.yanyi);
		mSuperStarLayout = (RelativeLayout) findViewById(R.id.super_star);
		mCartoonLayout = (RelativeLayout) findViewById(R.id.cartoon);
		mAnimalLayout = (RelativeLayout) findViewById(R.id.animal);

		MenuItemListener menuItemListener = new MenuItemListener();
		mAlbumLayout.setOnClickListener(menuItemListener);
		mAnimalLayout.setOnClickListener(menuItemListener);
		mCartoonLayout.setOnClickListener(menuItemListener);
		mYanyiLayout.setOnClickListener(menuItemListener);
		mSuperStarLayout.setOnClickListener(menuItemListener);

		switchCameraBtn = (ImageButton) findViewById(R.id.btn_switch);
		flashBtn = (ImageButton) findViewById(R.id.btn_flash);
		flashBtn.setSelected(true);
		mHorizontalListView = (HorizontalListView) findViewById(R.id.h_lv_picture);

		switchCameraBtn.setOnClickListener(this);
		flashBtn.setOnClickListener(this);

		disCameraSurface = (SurfaceView) findViewById(R.id.sf_dis_camera);
		disCameraSurface.setFocusable(true);
		disCameraSurface.setBackgroundColor(TRIM_MEMORY_BACKGROUND);
		disCameraSurface.setOnClickListener(this);
		disCameraSurfaceHolder = disCameraSurface.getHolder();
		disCameraSurfaceHolder.addCallback(this);
		disCameraSurfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
	}

	class MenuItemListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			selectMenuItemByType(previousType,false);
			int checkId = v.getId();
			if (checkId == R.id.album) {
				openAlbum();
			}
			if (checkId == R.id.animal) {
				selectMenuItemByType(Picture.THEME_TYPE_ANIMAL,true);
				previousType = Picture.THEME_TYPE_ANIMAL;
				loadData(Picture.THEME_TYPE_ANIMAL);
			}
			if (checkId == R.id.yanyi) {
				selectMenuItemByType(Picture.THEME_TYPE_YANYI, true);
				previousType = Picture.THEME_TYPE_YANYI;
				loadData(Picture.THEME_TYPE_YANYI);
			}
			if (checkId == R.id.cartoon) {
				selectMenuItemByType(Picture.THEME_TYPE_CARTOON,true);
				previousType = Picture.THEME_TYPE_CARTOON;
				loadData(Picture.THEME_TYPE_CARTOON);
			}
			if (checkId == R.id.super_star) {
				selectMenuItemByType(Picture.THEME_TYPE_SUPER_STAR,true);
				previousType = Picture.THEME_TYPE_SUPER_STAR;
				loadData(Picture.THEME_TYPE_SUPER_STAR);
			}
		}
	}

	/**
	 * 根据type选中menu item
	 * 
	 * @param group
	 */
	private void selectMenuItemByType(int type,boolean select) {
		switch (type) {
		case Picture.THEME_TYPE_ANIMAL:
			if(select){
				mAnimalLayout.setSelected(true);				
			}else{
				mAnimalLayout.setSelected(false);
			}
			break;
		case Picture.THEME_TYPE_CARTOON:
			if(select){
				mCartoonLayout.setSelected(true);				
			}else{
				mCartoonLayout.setSelected(false);
			}
			break;
		case Picture.THEME_TYPE_YANYI:
			if(select){
				mYanyiLayout.setSelected(true);
			}else{
				mYanyiLayout.setSelected(false);
			}
			break;
		case Picture.THEME_TYPE_SUPER_STAR:
			if(select){
				mSuperStarLayout.setSelected(true);				
			}else{
				mSuperStarLayout.setSelected(false);
			}
			break;
		}
	}

	/**
	 * 初始化数据
	 */
	private void initData() {
		currentType = Picture.THEME_TYPE_YANYI;
		previousType = currentType;
		mCurrentPictures = new ArrayList<Picture>();
		mPictureAdapter = new PictureSelectListAdapter(this, mCurrentPictures, R.layout.item_select_picture);

		mHorizontalListView.setAdapter(mPictureAdapter);
		mHorizontalListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View view, int position, long arg3) {
				if (currentStatus == STATUS_TAKE_PHOTO) {
					floatingImageDissmiss();
					floatingMoveUp();
				} else {
					selectTheme(position);
				}
			}
		});
	}

	/**
	 * 选中某一position的theme
	 * 
	 * @param position
	 */
	private void selectTheme(int position) {
		floatingMoveDown();
		Picture picture = mCurrentPictures.get(position);
		currentTitle = picture.title;
		Log.d(TAG, "img:" + picture.img);
		if (picture.img.startsWith("http://")) {
			Picasso.with(this).load(picture.img).into(new Target() {

				@Override
				public void onPrepareLoad(Drawable arg0) {

				}

				@Override
				public void onBitmapLoaded(Bitmap bitmap, LoadedFrom arg1) {
					floatingImageShow(bitmap, currentTitle);
				}

				@Override
				public void onBitmapFailed(Drawable arg0) {

				}
			});
		} else {
			Picasso.with(this).load(new File(picture.img)).into(new Target() {
				@Override
				public void onPrepareLoad(Drawable arg0) {
				}

				@Override
				public void onBitmapLoaded(Bitmap arg0, LoadedFrom arg1) {
					floatingImageShow(arg0, currentTitle);
				}

				@Override
				public void onBitmapFailed(Drawable arg0) {
				}
			});
		}
		mSelectedThemeText.setText(picture.title);
		selectedPicturePath = "";
	}

	/**
	 * 打开相册
	 */
	private void openAlbum() {
		Intent intent = new Intent(CameraPreviewActivity.this, AlbumActivity.class);
		startActivityForResult(intent, REQUEST_PHOTO_ALBUM);
	}

	/**
	 * 截图的返回
	 * 
	 * @param requestCode
	 * @param resultCode
	 * @param data
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_OK && requestCode == REQUEST_PHOTO_ALBUM) {
			Bitmap bitmap = data.getParcelableExtra(AlbumActivity.CROPED_PHOTO);
			floatingImageShow(bitmap, "");
			floatingMoveDown();
		}

	}

	/**
	 * 主题大图显示的动画
	 * 
	 * @param bitmap
	 * @param text
	 */
	private void floatingImageShow(Bitmap bitmap, String text) {
		mSelectedThemeLayout.setVisibility(View.VISIBLE);
		mSelectedThemeImage.setImageBitmap(bitmap);
		mSelectedThemeText.setText(text);
		ObjectAnimator alphaAnimator = ObjectAnimator.ofFloat(mSelectedThemeLayout, "alpha", 0f, 1f);
		ObjectAnimator xScaleAnimator = ObjectAnimator.ofFloat(mSelectedThemeLayout, "scaleX", 0f, 1f);
		ObjectAnimator yScaleAnimator = ObjectAnimator.ofFloat(mSelectedThemeLayout, "scaleY", 0f, 1f);
		AnimatorSet animatorSet = new AnimatorSet();
		animatorSet.setDuration(1000);
		animatorSet.play(alphaAnimator).with(xScaleAnimator).with(yScaleAnimator);
		animatorSet.start();
	}

	/**
	 * 主题大图消失的动画
	 */
	private void floatingImageDissmiss() {
		ObjectAnimator alphaAnimator = ObjectAnimator.ofFloat(mSelectedThemeLayout, "alpha", 1f, 0f);
		ObjectAnimator xScaleAnimator = ObjectAnimator.ofFloat(mSelectedThemeLayout, "scaleX", 1f, 0f);
		ObjectAnimator yScaleAnimator = ObjectAnimator.ofFloat(mSelectedThemeLayout, "scaleY", 1f, 0f);
		AnimatorSet animatorSet = new AnimatorSet();
		animatorSet.play(alphaAnimator).with(xScaleAnimator).with(yScaleAnimator);
		animatorSet.setDuration(1000);
		animatorSet.addListener(new AnimatorListener() {

			@Override
			public void onAnimationStart(Animator animation) {

			}

			@Override
			public void onAnimationRepeat(Animator animation) {

			}

			@Override
			public void onAnimationEnd(Animator animation) {
				mSelectedThemeLayout.setVisibility(View.INVISIBLE);
			}

			@Override
			public void onAnimationCancel(Animator animation) {

			}
		});
		animatorSet.start();

	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
		cameraOperationHelper.surfaceChanged();
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		cameraOperationHelper.doOpenCamera(mCameraId, this, holder);
		cameraOperationHelper.doStartPreview();
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		cameraOperationHelper.destroyCamera();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	@Override
	protected void onRestart() {
		super.onRestart();
	}

	@Override
	protected void onResume() {
		super.onResume();
		isPhotoing = false;
		boolean isNeedResetData = MiaoPaiApplication.getApplication().isNeedResetData;
		if(isNeedResetData){
			floatingImageDissmiss();
			floatingMoveUp();
			loadData(Picture.THEME_TYPE_YANYI);
			currentType = Picture.THEME_TYPE_YANYI;
			currentStatus = STATUS_SELECT_THEME;
			currentTitle = "none";
			selectMenuItemByType(previousType, false);
			selectMenuItemByType(currentType, true);
			previousType = currentType;
		}
	}

	@Override
	protected void onStart() {
		super.onStart();
	}

	private boolean isPhotoing = false;

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.btn_switch) {
			ObjectAnimator objectAnimator;
			if (isFront) {
				objectAnimator = ObjectAnimator.ofFloat(switchCameraBtn, "rotationY", 0f, 180f);
			} else {
				objectAnimator = ObjectAnimator.ofFloat(switchCameraBtn, "rotationY", 180f, 0f);
			}
			objectAnimator.setDuration(700);
			objectAnimator.addListener(new AnimatorListener() {
				@Override
				public void onAnimationStart(Animator animation) {

				}

				@Override
				public void onAnimationRepeat(Animator animation) {

				}

				@Override
				public void onAnimationEnd(Animator animation) {
					if (isFront) {
						switchCameraBtn.setSelected(true);
						cameraOperationHelper.changeCamera(CameraOperationHelper.BACK_CAMERA);
						isFront = false;
					} else {
						switchCameraBtn.setSelected(false);
						cameraOperationHelper.changeCamera(CameraOperationHelper.FRONT_CAMERA);
						isFront = true;
					}

				}

				@Override
				public void onAnimationCancel(Animator animation) {

				}
			});
			objectAnimator.start();

		} else if (v.getId() == R.id.sf_dis_camera) {

			if (currentStatus == STATUS_TAKE_PHOTO) {
				Log.d(TAG, "is Photoing" + isPhotoing);
				if (isPhotoing) {
					return;
				}
				isPhotoing = true;
				File picFile = FileUtils.createFile(Environment.getExternalStorageDirectory() + CAMERA_PATH,
						StringUtils.formatDate(new Date(), "yyyyMMddHHmmss") + ".jpg");
				if (isFlashing) {
					cameraOperationHelper.turnFlashOn();
				}
				cameraOperationHelper.takePicture(picFile.getAbsolutePath());
			} else {
				showViewFindToast();
			}
		} else if (v.getId() == R.id.btn_flash) {
			ObjectAnimator objectAnimator;
			if (isFlashing) {
				objectAnimator = ObjectAnimator.ofFloat(flashBtn, "rotationY", 0f, 180f);
			} else {
				objectAnimator = ObjectAnimator.ofFloat(flashBtn, "rotationY", 180f, 0f);
			}
			objectAnimator.setDuration(700);
			objectAnimator.addListener(new AnimatorListener() {
				@Override
				public void onAnimationStart(Animator animation) {
				}

				@Override
				public void onAnimationRepeat(Animator animation) {
				}

				@Override
				public void onAnimationEnd(Animator animation) {
					if (!isFlashing) {
						flashBtn.setSelected(false);
						isFlashing = true;
					} else {
						flashBtn.setSelected(true);
						isFlashing = false;
					}
				}

				@Override
				public void onAnimationCancel(Animator animation) {

				}
			});
			objectAnimator.start();
		}
	}

	@Override
	public void cameraFlashModeChanged(int flashMode) {

	}

	@Override
	public void cameraFacingChanged(boolean hasFaceCamera, int cameraId) {

	}

	/**
	 * 在onStop需要保存当前状态
	 */
	@Override
	protected void onStop() {
		super.onStop();
		SharedPreferenceUtil.setSharedPreferenceName(SAVED_INSTANCE);
		SharedPreferenceUtil.addInSharePreference(this, SAVED_DATE, new Date().getTime());
		SharedPreferenceUtil.addInSharePreference(this, SAVED_TYPE, currentType);
		if (selectedPicturePath == null || selectedPicturePath.equals("")) {
			SharedPreferenceUtil.addInSharePreference(this, IS_FROM_ALBUM, THEME_OTHER);
			SharedPreferenceUtil.addInSharePreference(this, SAVED_TITLE, currentTitle);
		} else {
			SharedPreferenceUtil.addInSharePreference(this, IS_FROM_ALBUM, THEME_ALBUM);
			SharedPreferenceUtil.addInSharePreference(this, SAVED_PATH, selectedPicturePath);
		}
	}

	/**
	 * 照相机的回调事件
	 */
	@Override
	public void cameraPhotoTaken(byte[] data, int cameraId) {
		this.mCameraId = cameraId;
		soundPool.play(1, 1, 1, 0, 0, 1f);
		cameraOperationHelper.doStopPreview();
		mPbIsTaking.setVisibility(View.VISIBLE);

		Bitmap decorationBitmap = BitmapUtil.convertViewToBitmap(mSelectedThemeLayout);
		File picSaveFile = FileUtils.createFile(
				Environment.getExternalStorageDirectory() + Configuration.MIAOPAI_FILE_PATH
						+ Configuration.MIAOPAI_TEMP_PATH,
				StringUtils.formatDate(new Date(), "yyyyMMddhhmmss") + ".jpg");
		BitmapUtil.saveBitmap(decorationBitmap, picSaveFile.getAbsolutePath());

		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeByteArray(data, 0, data.length, options);
		int width = options.outWidth;
		options.inSampleSize = width / 750;
		options.inJustDecodeBounds = false;
		Bitmap photoBitmap = BitmapFactory.decodeByteArray(data, 0, data.length, options);
		String path = FileUtils
				.createFile(
						Environment.getExternalStorageDirectory() + Configuration.MIAOPAI_FILE_PATH
								+ Configuration.MIAOPAI_TEMP_PATH,
						StringUtils.formatDate(new Date(), "yyyyMMdd") + ".jpg")
				.getAbsolutePath();
		BitmapUtil.saveBitmap(photoBitmap, path);

		Intent intent = new Intent(CameraPreviewActivity.this, PicturePreviewActivity.class);
		intent.putExtra(PICPATH, path);
		intent.putExtra(DECORATION, picSaveFile.getAbsolutePath());
		intent.putExtra(CAMERA_ID, cameraId);
		mPbIsTaking.setVisibility(View.GONE);
		startActivity(intent);
	}

}
