package com.qst.fly.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import uk.co.senab.photoview.PhotoViewAttacher;
import uk.co.senab.photoview.PhotoViewAttacher.OnPhotoTapListener;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.Toast;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.Platform.ShareParams;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.tencent.qq.QQ;
import cn.sharesdk.tencent.qzone.QZone;
import cn.sharesdk.wechat.friends.Wechat;
import cn.sharesdk.wechat.moments.WechatMoments;

import com.qst.fly.R;
import com.qst.fly.adapter.ImageShareListAdapter;
import com.qst.fly.adapter.ImageShareListAdapter.OnLoadNetPicFinish;
import com.qst.fly.application.MiaoPaiApplication;
import com.qst.fly.entity.Picture;
import com.qst.fly.entity.SharePicture;
import com.qst.fly.utils.CheckAppExistUtils;
import com.qst.fly.utils.FileUtils;
import com.qst.fly.utils.OkHttpUtils;
import com.qst.fly.utils.OkHttpUtils.ResultCallback;
import com.qst.fly.widget.WaterfallListView;
import com.qst.fly.widget.WaterfallListView.OnHeaderVisibilityChangeListener;
import com.qst.fly.widget.WaterfallListView.OnLoadDataListener;
import com.qst.fly.widget.view.PLA_AdapterView;
import com.qst.fly.widget.view.PLA_AdapterView.OnItemClickListener;
import com.umeng.analytics.MobclickAgent;
import com.umeng.analytics.social.UMPlatformData;
import com.umeng.analytics.social.UMPlatformData.GENDER;
import com.umeng.analytics.social.UMPlatformData.UMedia;

/**
 * @Title: ShareActivity.java
 * @Package com.qst.fly.activity
 * @author wzg0186 466959515@qq.com
 * @author yanfeizhao 417470640@qq.com
 * @date 2016-3-14 下午4:29:05
 * @version V1.0
 */
public class ShareActivity extends BaseActivity
		implements OnClickListener, OnHeaderVisibilityChangeListener, OnLoadDataListener, OnLoadNetPicFinish {

	ListView mListView;
	private String mPackageQQ = "com.tencent.mobileqq";
	private String mPackageWeibo = "com.sina.weibo";
	private String mPackageWeixin = "com.tencent.mm";
	private String mPackageQQZone = "com.qzone";

	private boolean mShareState = false;//false:分享不成功。

	public static final String URL_EXAMPLE = "http://api.miaopai.com/m/maopaiImage.json";// 获取示例图片的接口
	private String mFileAbsolutePath = Environment.getExternalStorageDirectory().getPath() + "/qst_miaopai/saved";// 存储示例图片的文件夹路径。
	private int mNumOfNetPic = 0;// 存储网络图片的张数

	private ImageView mImgBack;
	private ImageView mImgTakePhoto;
	private ImageView mImgWeChat;
	private ImageView mImgFrinds;
	private ImageView mImgWeibo;
	private ImageView mImgQQ;
	private ImageView mImgQQZone;

	private ImageView mImgWeChatSmall;
	private ImageView mImgFrindsSmall;
	private ImageView mImgWeiboSmall;
	private ImageView mImgQQSmall;
	private ImageView mImgQQZoneSmall;

	private String mShareFail;
	private String mShareCancel;

	private LinearLayout mLayoutWeChat;
	private LinearLayout mLayoutFriends;
	private LinearLayout mLayoutWeibo;
	private LinearLayout mLayoutQQ;
	private LinearLayout mLayoutQQZone;

	private List<Picture> mListPictures;
	private SharePicture mShareSimple = null;

	private Bitmap mBitmap;
	private String mPath;
	private WaterfallListView mMultiColumnListView;
	private ImageShareListAdapter mImageShareListAdapter;
	private UMPlatformData platform;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		MobclickAgent.setDebugMode(true);
		requestWindowFeature(Window.FEATURE_NO_TITLE);// 去掉标题栏
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_share);

		ShareSDK.initSDK(this);
		getBitmapFromIntent();
		initData();
		initView();
	}

	
	/**
	 * 得到要分享出去的照片。（bitmap对象）
	 */
	private void getBitmapFromIntent() {
		mPath = getIntent().getStringExtra(PicturePreviewActivity.SAVED_PATH);
		if (mPath != null) {
			mBitmap = BitmapFactory.decodeFile(mPath);
		}
	}

	/**
	 * 初始化数据
	 */
	private void initData() {
		mListPictures = new ArrayList<Picture>();
		mImageShareListAdapter = new ImageShareListAdapter(this, mListPictures);

		OkHttpUtils.get(URL_EXAMPLE, new ResultCallback<SharePicture>() {
			@Override
			public void onSuccess(SharePicture response) {
				// 顺利请求到jsonString之后，就先加载网络图片。
				mShareSimple=response;
				List<String> picpath = mShareSimple.getResult();
				for (int i = 0; i < picpath.size(); i++) {
					mListPictures.add(new Picture(picpath.get(i)));
				}
				mNumOfNetPic = mListPictures.size();// 网络图片的张数
				// 加载本地图片。
				List<String> listPicturePath = FileUtils.GetPictureFileName(mFileAbsolutePath);
				int lengthOfPic = listPicturePath.size();
				
//				lengthOfPic=lengthOfPic > 10 ?10:lengthOfPic;
				if (lengthOfPic > 10) {
					lengthOfPic = 10;
				}
				for (int i = 0; i < lengthOfPic; i++) {
					mListPictures.add(new Picture(listPicturePath.get(i)));
				}
				mImageShareListAdapter.notifyDataSetChanged();
			}

			@Override
			public void onFailure(Exception e) {
				List<String> listPicturePath = FileUtils.GetPictureFileName(mFileAbsolutePath);
				int lengthOfPic = listPicturePath.size();
				if (lengthOfPic > 10) {
					lengthOfPic = 10;
				}
				for (int i = 0; i < lengthOfPic; i++) {
					mListPictures.add(new Picture(listPicturePath.get(i)));
				}
				mImageShareListAdapter.notifyDataSetChanged();
			}
		});
		mImageShareListAdapter.setOnLoadNetPicFinish(this);

	}

	@Override
	public int getNumOfNetPic() {
		return mNumOfNetPic;
	}

	/**
	 * 显示全屏图片
	 */
	private void showFullScreenPicture(View view) {
		View root = getLayoutInflater().inflate(R.layout.activity_share, null);
		ImageView image = (ImageView) view.findViewById(R.id.img_sharelist);
		BitmapDrawable bd = (BitmapDrawable) image.getDrawable();
		Bitmap bitmap = bd.getBitmap();

		View contentView = LayoutInflater.from(ShareActivity.this).inflate(R.layout.layout_show_pic_full_screen, null);
		ImageView imageView = (ImageView) contentView.findViewById(R.id.img_dis_screen);
		imageView.setImageBitmap(bitmap);
		PhotoViewAttacher attacher = new PhotoViewAttacher(imageView);
		attacher.update();
		final PopupWindow pop = new PopupWindow(contentView, WindowManager.LayoutParams.MATCH_PARENT,
				WindowManager.LayoutParams.MATCH_PARENT, true);
		
		attacher.setOnPhotoTapListener(new OnPhotoTapListener() {
			
			@Override
			public void onPhotoTap(View view, float x, float y) {
				pop.dismiss();
			}
		});
		pop.setBackgroundDrawable(new ColorDrawable(0xffffff));// 支持点击Back虚拟键退出
		pop.showAtLocation(root, Gravity.NO_GRAVITY, 0, 0);
	}

	/**
	 * 初始化控件
	 */
	private void initView() {

		mMultiColumnListView = (WaterfallListView) findViewById(R.id.lv_act_share);
		View view = View.inflate(this, R.layout.sharelist_header, null);
		mMultiColumnListView.addHeaderView(view);
		mMultiColumnListView.setOnLoadDataListener(this);
		mMultiColumnListView.setOnHeaderVisibilityChangeListener(this);
		mMultiColumnListView.setAdapter(mImageShareListAdapter);
		mMultiColumnListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(PLA_AdapterView<?> parent, View view, int position, long id) {
				if(position != 0){
					showFullScreenPicture(view);					
				}
			}

		});
		mImgBack = (ImageView) findViewById(R.id.img_back);
		mImgTakePhoto = (ImageView) findViewById(R.id.img_take_photo);
		mImgWeChat = (ImageView) view.findViewById(R.id.img_wechat);
		mImgFrinds = (ImageView) view.findViewById(R.id.img_friends);
		mImgWeibo = (ImageView) view.findViewById(R.id.img_weibo);
		mImgQQ = (ImageView) view.findViewById(R.id.img_qq);
		mImgQQZone = (ImageView) view.findViewById(R.id.img_qq_zone);

		mImgWeChatSmall = (ImageView) findViewById(R.id.img_weichat_small);
		mImgFrindsSmall = (ImageView) findViewById(R.id.img_friends_small);
		mImgWeiboSmall = (ImageView) findViewById(R.id.img_weibo_small);
		mImgQQSmall = (ImageView) findViewById(R.id.img_qq_small);
		mImgQQZoneSmall = (ImageView) findViewById(R.id.img_qq_zone_small);

		mLayoutWeChat = (LinearLayout) view.findViewById(R.id.ll_wechat);
		mLayoutFriends = (LinearLayout) view.findViewById(R.id.ll_friends);
		mLayoutWeibo = (LinearLayout) view.findViewById(R.id.ll_weibo);
		mLayoutQQ = (LinearLayout) view.findViewById(R.id.ll_qq);
		mLayoutQQZone = (LinearLayout) view.findViewById(R.id.ll_qq_zone);

		mImgBack.setOnClickListener(this);
		mImgTakePhoto.setOnClickListener(this);
		mImgWeChat.setOnClickListener(this);
		mImgFrinds.setOnClickListener(this);
		mImgWeibo.setOnClickListener(this);
		mImgQQ.setOnClickListener(this);
		mImgQQZone.setOnClickListener(this);

		mImgWeChatSmall.setOnClickListener(this);
		mImgFrindsSmall.setOnClickListener(this);
		mImgWeiboSmall.setOnClickListener(this);
		mImgQQSmall.setOnClickListener(this);
		mImgQQZoneSmall.setOnClickListener(this);

		bigAppIconVisible();

	}

	/**
	 * 让大图标显示
	 */
	private void bigAppIconVisible() {
		if (CheckAppExistUtils.checkAppExist(this, mPackageWeixin)) {

			mLayoutWeChat.setVisibility(View.VISIBLE);
			mLayoutFriends.setVisibility(View.VISIBLE);
		}
		if (CheckAppExistUtils.checkAppExist(this, mPackageWeibo)) {
			mLayoutWeibo.setVisibility(View.VISIBLE);
		}

		if (CheckAppExistUtils.checkAppExist(this, mPackageQQ)) {
			mLayoutQQ.setVisibility(View.VISIBLE);
		}
		if (CheckAppExistUtils.checkAppExist(this, mPackageQQZone)) {
			mLayoutQQZone.setVisibility(View.VISIBLE);
		}
	}

	/**
	 * 处理点击事件
	 */
	@Override
	public void onClick(View v) {// TODO 点击事件要一一实现
		switch (v.getId()) {
		case R.id.img_back:
			finish();
			break;
		case R.id.img_take_photo:
			Intent intent = new Intent(ShareActivity.this, CameraPreviewActivity.class);
			startActivity(intent);
			MiaoPaiApplication.getApplication().isNeedResetData = true;
			finish();
			break;

		case R.id.img_wechat:
			shareToDifferentPlatform(1);
			if (mShareState) {
				shareUmeng(1);
			}
			break;

		case R.id.img_friends:
			shareToDifferentPlatform(2);
			if (mShareState) {
				shareUmeng(2);
			}
			break;

		case R.id.img_weibo:
			shareToDifferentPlatform(3);
			if (mShareState) {
				shareUmeng(3);
			}

			break;

		case R.id.img_qq:
			shareToDifferentPlatform(4);
			if (mShareState) {
				shareUmeng(4);
			}
			break;

		case R.id.img_qq_zone:
			shareToDifferentPlatform(5);
			if (mShareState) {
				shareUmeng(5);
			}
			break;

		case R.id.img_weichat_small:
		case R.id.img_friends_small:
		case R.id.img_weibo_small:
		case R.id.img_qq_small:
		case R.id.img_qq_zone_small:
			// mMultiColumnListView.setSelectionFromTop(0, 0);// position y
			mMultiColumnListView.setAdapter(mImageShareListAdapter);
			break;
		default:
			break;
		}
	}

	/**
	 * 分享照片到不同平台
	 * 
	 * @param whitch
	 */
	private void shareToDifferentPlatform(int whitch) {
		Platform pf = null;
		ShareParams sp = null;

		switch (whitch) {
		case 1:
			pf = ShareSDK.getPlatform(Wechat.NAME);
			sp = new ShareParams();
			sp.setImagePath(mPath);

			mShareFail = getString(R.string.wechatfail);
			mShareCancel = getString(R.string.wechatcancel);
			break;

		case 2:
			pf = ShareSDK.getPlatform(WechatMoments.NAME);
			sp = new ShareParams();
			sp.setImagePath(mPath);

			mShareFail = getString(R.string.wechatmomentfail);
			mShareCancel = getString(R.string.wechatmomentcancel);
			break;

		case 3:
			pf = ShareSDK.getPlatform(SinaWeibo.NAME);
			sp = new ShareParams();
			// 分享微博只能是text和image
			sp.setText(getString(R.string.content));
			sp.setImagePath(mPath);

			mShareFail = getString(R.string.weibofail);
			mShareCancel = getString(R.string.weibocancel);
			break;

		case 4:
			pf = ShareSDK.getPlatform(QQ.NAME);
			sp = new ShareParams();
			// QQ分享时这四个参数需要都具备
//			sp.setTitle(getString(R.string.sharetitle));
			sp.setImagePath(mPath);

			mShareFail = getString(R.string.qqfail);
			mShareCancel = getString(R.string.qqcancel);
			break;

		case 5:
			pf = ShareSDK.getPlatform(QZone.NAME);
			sp = new ShareParams();
			sp.setText("喵拍");
			sp.setImagePath(mPath);

			mShareFail = getString(R.string.qqzonefail);
			mShareCancel = getString(R.string.qqzonecancel);
			break;
		}
		// 设置分享事件的反馈
		pf.setPlatformActionListener(new PlatformActionListener() {

			@Override
			public void onError(Platform arg0, int arg1, Throwable arg2) {
				Toast.makeText(ShareActivity.this, mShareFail, Toast.LENGTH_SHORT).show();
				mShareState = false;
			}

			@Override
			public void onComplete(Platform arg0, int arg1, HashMap<String, Object> arg2) {
				mShareState = true;
			}

			@Override
			public void onCancel(Platform arg0, int arg1) {
				Toast.makeText(ShareActivity.this, mShareCancel, Toast.LENGTH_SHORT).show();
				mShareState = false;
			}
		});
		// 执行图文分享
		pf.share(sp);
	}

	private void shareUmeng(int type) {
		switch (type) {
		case 1:
			platform = new UMPlatformData(UMedia.WEIXIN_CIRCLE, "user_id");
			platform.setGender(GENDER.MALE); // optional
			platform.setWeiboId("1"); // optional
			MobclickAgent.onSocialEvent(this, platform);
			break;

		case 2:
			platform = new UMPlatformData(UMedia.WEIXIN_FRIENDS, "user_id");
			platform.setGender(GENDER.MALE); // optional
			platform.setWeiboId("2"); // optional
			MobclickAgent.onSocialEvent(this, platform);
			break;

		case 3:
			platform = new UMPlatformData(UMedia.SINA_WEIBO, "user_id");
			platform.setGender(GENDER.MALE); // optional
			platform.setWeiboId("weiboId"); // optional
			MobclickAgent.onSocialEvent(this, platform);
			break;

		case 4:
			platform = new UMPlatformData(UMedia.TENCENT_QQ, "user_id");
			platform.setGender(GENDER.MALE); // optional
			platform.setWeiboId("4"); // optional
			MobclickAgent.onSocialEvent(this, platform);
			break;

		case 5:
			platform = new UMPlatformData(UMedia.TENCENT_QZONE, "user_id");
			platform.setGender(GENDER.MALE); // optional
			platform.setWeiboId("5"); // optional
			MobclickAgent.onSocialEvent(this, platform);
			break;

		default:
			break;
		}

	}
	
	/**
	 * 加载数据-先从接口中加载数据，然后再从本地加载10张图片。
	 */
	@Override
	public void onLoadDataListener() {
		// TODO 通过接口获取照片数据。
		new Thread() {
			public void run() {
				try {
					Thread.sleep(3000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}.start();

	}

	/**
	 * 监听瀑布流的头部状态，显示状态是大图标，隐藏状态下：更改标题，显示小图标
	 */
	@SuppressLint("NewApi")
	@Override
	public void OnHeaderVisibilityChange(int headerVisibility) {
		if (headerVisibility == OnHeaderVisibilityChangeListener.HEADER_VISIBLE) {
			smallAppIconGone();
			bigAppIconVisible();
		}

		if (headerVisibility == OnHeaderVisibilityChangeListener.HEADER_INVISIBLE) {
			smallAppIconVisible();

		}

	}

	private void smallAppIconGone() {
		mImgWeChatSmall.setVisibility(View.GONE);
		mImgFrindsSmall.setVisibility(View.GONE);
		mImgWeiboSmall.setVisibility(View.GONE);
		mImgQQSmall.setVisibility(View.GONE);
		mImgQQZoneSmall.setVisibility(View.GONE);
	}

	private void smallAppIconVisible() {
		if (CheckAppExistUtils.checkAppExist(this, mPackageWeixin)) {
			mImgWeChatSmall.setVisibility(View.VISIBLE);
			mImgFrindsSmall.setVisibility(View.VISIBLE);
		}
		if (CheckAppExistUtils.checkAppExist(this, mPackageWeibo)) {
			mImgWeiboSmall.setVisibility(View.VISIBLE);
		}

		if (CheckAppExistUtils.checkAppExist(this, mPackageQQ)) {
			mImgQQSmall.setVisibility(View.VISIBLE);
		}
		if (CheckAppExistUtils.checkAppExist(this, mPackageQQZone)) {
			mImgQQZoneSmall.setVisibility(View.VISIBLE);
		}
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		MobclickAgent.onResume(this);
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		MobclickAgent.onPause(this);
	}

}
