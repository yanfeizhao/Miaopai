package com.qst.fly.adapter;

import java.util.List;

import com.qst.fly.entity.Picture;

import android.content.Context;

/**
* @author smallzoo
* @version
* @date 2016年3月14日 下午8:52:04
* 类说明
*/
public class PictureSelectListAdapter extends CommonAdapter<Picture> {

	public PictureSelectListAdapter(Context context, List<Picture> mDatas, int itemLayoutId) {
		super(context, mDatas, itemLayoutId);
	}

	@Override
	public void convert(ViewHolder helper, Picture item, int position) {
		
	}

}
