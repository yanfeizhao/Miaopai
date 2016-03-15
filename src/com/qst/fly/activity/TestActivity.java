package com.qst.fly.activity;

import java.util.ArrayList;
import java.util.List;

import com.meetme.android.horizontallistview.HorizontalListView;
import com.qst.fly.R;
import com.qst.fly.adapter.PictureSelectListAdapter;
import com.qst.fly.entity.Picture;

import android.app.Activity;
import android.os.Bundle;

/**
* @author smallzoo
* @version
* @date 2016年3月15日 上午10:21:05
* 类说明
*/
public class TestActivity extends Activity {

	private HorizontalListView mHorizontalListView;
	private PictureSelectListAdapter adapter;
	private List<Picture> poictures;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_test);
		
		initView();
		initData();
	}
	private void initData() {
		poictures = new ArrayList<Picture>();
		poictures.add(new Picture("aa"));
		poictures.add(new Picture("aa"));
		poictures.add(new Picture("aa"));
		poictures.add(new Picture("aa"));
		adapter = new PictureSelectListAdapter(this, poictures, R.layout.item_select_picture);
		mHorizontalListView.setAdapter(adapter);
	}
	private void initView() {
		mHorizontalListView = (HorizontalListView) findViewById(R.id.hh);
		
	}
}
