package com.qst.fly.adapter;

import java.util.List;

import android.content.Context;

import com.qst.fly.entity.PhotoUpImageItem;

public class CameraAlbumDetailAdapter extends CommonAdapter<PhotoUpImageItem> {

	public CameraAlbumDetailAdapter(Context context,
			List<PhotoUpImageItem> mDatas, int itemLayoutId) {
		super(context, mDatas, itemLayoutId);

	}

	@Override
	public void convert(ViewHolder helper, PhotoUpImageItem item, int position) {

	}

}
