package com.qst.fly.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Bitmap.Config;
import android.widget.ImageView;

import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.qst.fly.R;
import com.qst.fly.entity.PhotoUpImageBucket;

public class AlbumAdapter extends CommonAdapter<PhotoUpImageBucket> {

	private List<PhotoUpImageBucket> mlist;
	private ImageLoader mImageLoader;
	private DisplayImageOptions mOptions;
	// 开发测试标记位
	private String TAG = AlbumAdapter.class.getSimpleName();

	public AlbumAdapter(Context context, List<PhotoUpImageBucket> mDatas,
			int itemLayoutId) {
		super(context, mDatas, R.layout.album_item);
		mImageLoader = ImageLoader.getInstance();
		
		mlist = new ArrayList<PhotoUpImageBucket>();

		// 视图加载器
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
				context).threadPriority(Thread.NORM_PRIORITY - 2)
				.denyCacheImageMultipleSizesInMemory()
				.discCacheFileNameGenerator(new Md5FileNameGenerator())
				.tasksProcessingOrder(QueueProcessingType.LIFO)
				.memoryCacheExtraOptions(96, 120).build();

		// 初始化图片加载器的配置
		mImageLoader.init(config);
		// 使用DisplayImageOption.Builder()创建DisplayImageOptions
		mOptions = new DisplayImageOptions.Builder()
				// 设置图片下载期间显示的图片
				.showStubImage(R.drawable.default_picture)
				// 设置图片Uri为空或是错误的时候显示的图片
				.showImageForEmptyUri(R.drawable.default_picture)
				// 设置图片加载或解码过程中发生错误显示的图片
				.showImageOnFail(R.drawable.default_picture)
				// 设置下载的图片是否缓存在内存中
				.cacheInMemory(true)
				// 设置下载的图片是否缓存在SD卡中
				.cacheOnDisc(true)
				.bitmapConfig(Config.RGB_565)
				.imageScaleType(ImageScaleType.IN_SAMPLE_INT)
				// 创建配置过的DisplayImageOption对象
				.build();
	}

	@Override
	public void convert(ViewHolder helper, PhotoUpImageBucket item, int position) {

		helper.setText(R.id.text_numofphoto, item.getCount() + "张");
		helper.setText(R.id.text_fromwhichapp, item.getBucketName());
		ImageView imageViews = helper.getView(R.id.img_albumitem);
		mImageLoader.displayImage("file://"
				+ item.getImageList().get(0).getImagePath(), imageViews,
				mOptions);
	}

	/** 设置相册列表数据，主要用于把相册数据传递到Adapter中 */
	public void setArrayList(List<PhotoUpImageBucket> mlist) {
		this.mlist = mlist;
	}
}
