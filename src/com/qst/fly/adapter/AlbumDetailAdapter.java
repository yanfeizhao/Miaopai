package com.qst.fly.adapter;

import java.util.List;

import android.content.Context;
import android.graphics.Bitmap.Config;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.qst.fly.R;
import com.qst.fly.entity.PhotoUpImageItem;

public class AlbumDetailAdapter extends CommonAdapter<PhotoUpImageItem> {

	/** 图片加载器 */
	private ImageLoader mImageLoader;
	/** 图片加载器的设置 */
	private DisplayImageOptions mOptions;

	public AlbumDetailAdapter(Context context, List<PhotoUpImageItem> mDatas,
			int itemLayoutId) {
		super(context, mDatas, itemLayoutId);

		mImageLoader = ImageLoader.getInstance();
		mOptions = new DisplayImageOptions.Builder()
				.showStubImage(R.drawable.default_picture)
				.showImageForEmptyUri(R.drawable.default_picture)
				.showImageOnFail(R.drawable.default_picture)
				.cacheInMemory(true)
				.cacheOnDisc(true)
				.bitmapConfig(Config.ARGB_8888)
				.imageScaleType(ImageScaleType.IN_SAMPLE_INT)
				.build();
	}

	@Override
	public void convert(ViewHolder helper, PhotoUpImageItem item, int position) {

		ImageView ImageView = helper.getView(R.id.gridview_item);
		mImageLoader.displayImage("file://" + item.getImagePath(), ImageView,
				mOptions);
	}

}
