/** 
 * @Title: ShareActivity.java 
 * @Package com.qst.fly.activity 
 * @Description: TODO 
 * @author yanfeizhao 417470640@qq.com 
 * @date 2016-3-14 下午4:29:05 
 * @version V1.0 
 */  
package com.qst.fly.activity;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.qst.fly.R;
import com.qst.fly.adapter.ImageShareListAdapter;
import com.qst.fly.entity.ImageInfo;
import com.qst.fly.widget.WaterfallListView;
import com.qst.fly.widget.WaterfallListView.OnHeaderVisibilityChangeListener;
import com.qst.fly.widget.WaterfallListView.OnLoadDataListener;

/** 
 * @Title: ShareActivity.java 
 * @Package com.qst.fly.activity 
 * @Description: TODO 
 * @author yanfeizhao 417470640@qq.com 
 * @date 2016-3-14 下午4:29:05 
 * @version V1.0 
 */
public class ShareActivity extends BaseActivity implements OnClickListener,OnHeaderVisibilityChangeListener, OnLoadDataListener{
	
	private ImageView mImgBack;
	private ImageView mImgTakePhoto;
	private ImageView mImgWeChat;
	private ImageView mImgFrinds;
	private ImageView mImgWeibo;
	private ImageView mImgQQ;
	private ImageView mImgQQZone;
	
	private TextView mTextTitle;
	
	private List<ImageInfo> mListImageInfo;
	private WaterfallListView mMultiColumnListView;
	private ImageShareListAdapter mImageShareListAdapter;
	private Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			mImageShareListAdapter.notifyDataSetChanged();
		};
	};
	
	private int mImageId[] = { R.drawable.p5, R.drawable.back_carmera,
			 R.drawable.back_carmera, R.drawable.back_carmera, R.drawable.back_carmera,
			 R.drawable.wechat, R.drawable.back_carmera, R.drawable.weibo,
			 R.drawable.back_carmera,
			R.drawable.p5, R.drawable.p5 };
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);// 去掉标题栏
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_share);
		initData();
		initView();
	}




	private void initData() {
		// TODO Auto-generated method stub
		mListImageInfo=new ArrayList<ImageInfo>();
		ImageInfo ii=null;
		for (int i = 0; i < mImageId.length; i++) {
			ii = new ImageInfo(mImageId[i]);
			mListImageInfo.add(ii);
		}
		mImageShareListAdapter = new ImageShareListAdapter(this, mListImageInfo);
	}




	private void initView() {
		// TODO Auto-generated method stub
		
		mMultiColumnListView = (WaterfallListView) findViewById(R.id.lv_act_share);
		View view = View.inflate(this, R.layout.sharelist_header, null);
		mMultiColumnListView.addHeaderView(view);
		mMultiColumnListView.setOnLoadDataListener(this);
		mMultiColumnListView.setOnHeaderVisibilityChangeListener(this);
		mMultiColumnListView.setAdapter(mImageShareListAdapter);
		
		mImgBack=(ImageView) findViewById(R.id.img_back);
		mImgTakePhoto=(ImageView)findViewById(R.id.img_take_photo);
		mImgWeChat=(ImageView) view.findViewById(R.id.img_wechat);
		mImgFrinds=(ImageView) view.findViewById(R.id.img_friends);
		mImgWeibo=(ImageView) view.findViewById(R.id.img_weibo);
		mImgQQ=(ImageView) view.findViewById(R.id.img_qq);
		mImgQQZone=(ImageView) view.findViewById(R.id.img_qq_zone);
		mTextTitle=(TextView) findViewById(R.id.tv_toptitle);
		
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
			showToast("点击了返回");
			break;
		case R.id.img_take_photo:
			showToast("点击了拍照");
			break;
		case R.id.img_wechat:
			showToast("点击了微信");
			break;
		case R.id.img_friends:
			showToast("点击了朋友圈");
			break;
		case R.id.img_weibo:
			showToast("点击了微博");
			break;
		case R.id.img_qq:
			showToast("点击了qq");
			break;
		case R.id.img_qq_zone:
			showToast("点击了qq空间");
			break;
			
		//TODO  返回到分享页面大图标显示。
		case R.id.img_weichat_small:
		case R.id.img_friends_small:
		case R.id.img_weibo_small:
		case R.id.img_qq_small:
		case R.id.img_qq_zone_small:
			mMultiColumnListView.setSelectionFromTop(0, 0);//position y 
			break;
				

		default:
			break;
		}
	}




	@Override
	public void onLoadDataListener() {
		// TODO Auto-generated method stub

		showToast("加载数据");
		new Thread() {
			public void run() {
				try {
					Thread.sleep(3000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				ImageInfo ii=null;
				for (int i = 0; i < mImageId.length; i++) {
					ii = new ImageInfo(mImageId[i]);
					mListImageInfo.add(ii);
				}
				mHandler.sendEmptyMessage(0);
				mMultiColumnListView.notifyFinishLoading(true);
			}
		}.start();
	
	}


	@SuppressLint("NewApi")
	@Override
	public void OnHeaderVisibilityChange(int headerVisibility) {
		if (headerVisibility == OnHeaderVisibilityChangeListener.HEADER_VISIBLE){
			showToast("Header显示了");
			mTextTitle.setVisibility(View.VISIBLE);
			mTextTitle.setText("分享给朋友们");
			findViewById(R.id.img_weichat_small).setVisibility(View.GONE);
			findViewById(R.id.img_friends_small).setVisibility(View.GONE);
			findViewById(R.id.img_weibo_small).setVisibility(View.GONE);
			findViewById(R.id.img_qq_small).setVisibility(View.GONE);
			findViewById(R.id.img_qq_zone_small).setVisibility(View.GONE);
			
		}
			
		if (headerVisibility == OnHeaderVisibilityChangeListener.HEADER_INVISIBLE){
			showToast("Header隐藏了");
			//TODO  将小图标显示在textVeiw里面。并且点击之后，会返回到显示大图标的状态，并且返回到图片的顶部。
//			mTextTitle.setText("我这里有5个小图标");
//			LinearLayout ll=(LinearLayout) View.inflate(this, R.id.ll_five_appicon, null);
//			ll.setVisibility(View.VISIBLE);
			mTextTitle.setVisibility(View.GONE);
			findViewById(R.id.img_weichat_small).setVisibility(View.VISIBLE);
			findViewById(R.id.img_friends_small).setVisibility(View.VISIBLE);
			findViewById(R.id.img_weibo_small).setVisibility(View.VISIBLE);
			findViewById(R.id.img_qq_small).setVisibility(View.VISIBLE);
			findViewById(R.id.img_qq_zone_small).setVisibility(View.VISIBLE);
		}
			
	}
	
	

}
