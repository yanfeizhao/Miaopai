package com.qst.fly.activity;

import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;

import com.qst.fly.R;
import com.qst.fly.adapter.CameraAlbumAdapter;
import com.qst.fly.entity.PhotoUpImageBucket;
import com.qst.fly.utils.PhotoUpAlbumHelper;
import com.qst.fly.utils.PhotoUpAlbumHelper.GetAlbumList;

public class CameraAlbumActivity extends Activity {

	private ImageView mImgViewback;
	private ListView mListView;
	private CameraAlbumAdapter adapter;
	/** 加载相册和图片的异步线程类 */
	private PhotoUpAlbumHelper photoUpAlbumHelper;
	/** 存放相册列表数据 */
	private List<PhotoUpImageBucket> list;
	/** 进入相册详情页 */
	private ImageView mImgViewToAlbumDetail;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_album);

		initViews();
		// 加载数据
		loadData();
		// 设置相册点击事件
		onItemClick();

	}

	private void initViews() {

		mImgViewback = (ImageView) findViewById(R.id.img_album_back);
		mListView = (ListView) findViewById(R.id.listview_album);
//		adapter = new CameraAlbumAdapter(CameraAlbumActivity.this, mDatas,
//				itemLayoutId);
		mListView.setAdapter(adapter);

	}

	private void loadData() {
		photoUpAlbumHelper = PhotoUpAlbumHelper.getHelper();// 创建异步线程实例
		photoUpAlbumHelper.init(CameraAlbumActivity.this);// 初始化实例
		// 回调接口，创建匿名内部对象，实现接口中的方法，获取到PhotoUpAlbumHelper的接口GetAlbumList所传递的数据
		photoUpAlbumHelper.setGetAlbumList(new GetAlbumList() {
			@Override
			public void getAlbumList(List<PhotoUpImageBucket> list) {
				adapter.setArrayList(list);
				adapter.notifyDataSetChanged();// 更新视图
				CameraAlbumActivity.this.list = list;
			}
		});
		photoUpAlbumHelper.execute(false);// 异步线程执行
	}

	// 通过点击每个相册，进入相册内部查看该相册内的图片
	private void onItemClick() {
		mListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent intent = new Intent(CameraAlbumActivity.this,
						CameraAlbumDetailActivity.class);
				intent.putExtra("imagelist", list.get(position));
				startActivity(intent);
			}
		});
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}
}
