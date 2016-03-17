package com.qst.fly.fragment;

import java.util.ArrayList;

import com.qst.fly.R;
import com.qst.fly.activity.AlbumActivity;
import com.qst.fly.adapter.CameraAlbumDetailAdapter;
import com.qst.fly.entity.PhotoUpImageBucket;
import com.qst.fly.entity.PhotoUpImageItem;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;

/**
* @author smallzoo
* @version
* @date 2016年3月16日 上午10:43:57
* 类说明
*/
public class AlbumDetailFragment extends Fragment implements OnItemClickListener {

	private View contentView;
	public static final String SELECTED_PHOTO = "select_photo";
	private GridView mGridView;
	private PhotoUpImageBucket mPImageBucket;
	private ArrayList<PhotoUpImageItem> selectImages;
	private CameraAlbumDetailAdapter adapter;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		contentView = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_album_detail, null);

		initView();
		loadData();
		return contentView;
	}
	
	private void loadData() {
		mPImageBucket =((AlbumActivity)getActivity()).getPhotoUpImageBucket();
		selectImages = new ArrayList<PhotoUpImageItem>(mPImageBucket.getImageList());
		adapter = new CameraAlbumDetailAdapter(getActivity(),selectImages , R.layout.albumdetail_item);
		mGridView.setAdapter(adapter);
	}

	private void initView() {
		mGridView = (GridView) contentView.findViewById(R.id.gridview_albumdetail);
		mGridView.setOnItemClickListener(this);
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		((AlbumActivity)getActivity()).setPhotoUpImageItem(selectImages.get(position));
		((AlbumActivity)getActivity()).jump2Fragment(AlbumActivity.CROP_FRAGMENT_POSITION);
	}
}
