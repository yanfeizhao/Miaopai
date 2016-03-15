package com.qst.fly.adapter;

import java.util.List;

import com.bumptech.glide.Glide;
import com.qst.fly.R;
import com.qst.fly.entity.Picture;

import android.content.Context;
import android.widget.ImageView;

/**
* @author smallzoo
* @version
* @date 2016年3月14日 下午8:52:04
* 类说明
*/
public class PictureSelectListAdapter extends CommonAdapter<Picture> {
	private Context context;
	public PictureSelectListAdapter(Context context, List<Picture> mDatas, int itemLayoutId) {
		super(context, mDatas, itemLayoutId);
		this.context = context;
	}

	@Override
	public void convert(ViewHolder helper, Picture item, int position) {
		ImageView imageView = helper.getView(R.id.img_item_pic);
//		Glide.with(this.context).load(item.picturePath).into(imageView);
	}

}
