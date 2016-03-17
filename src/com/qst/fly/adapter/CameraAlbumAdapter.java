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

public class CameraAlbumAdapter extends CommonAdapter<PhotoUpImageBucket> {

	private List<PhotoUpImageBucket> mlist;
	// 初始化获取实例
	private ImageLoader imageLoader = ImageLoader.getInstance();
	private DisplayImageOptions options;
	// 开发测试标记位
	private String TAG = CameraAlbumAdapter.class.getSimpleName();

	public CameraAlbumAdapter(Context context, List<PhotoUpImageBucket> mDatas,
			int itemLayoutId) {
		super(context, mDatas, R.layout.album_item);

		mlist = new ArrayList<PhotoUpImageBucket>();
		// 视图加载器
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
				context).threadPriority(Thread.NORM_PRIORITY - 2)
				.denyCacheImageMultipleSizesInMemory()
				.discCacheFileNameGenerator(new Md5FileNameGenerator())
				.tasksProcessingOrder(QueueProcessingType.LIFO)
				// 初始化图片加载器的配置
				.memoryCacheExtraOptions(96, 120).build();
		// Initialize ImageLoader with configuration.
		imageLoader.init(config);

		// 使用DisplayImageOption.Builder()创建DisplayImageOptions
		options = new DisplayImageOptions.Builder()
				// 设置图片下载期间显示的图片
				.showStubImage(R.drawable.cat_in_launch)
				// 设置图片Uri为空或是错误的时候显示的图片
				.showImageForEmptyUri(R.drawable.ic_launcher)
				// 设置图片加载或解码过程中发生错误显示的图片
				.showImageOnFail(R.drawable.ic_launcher)
				// 设置下载的图片是否缓存在内存中
				.cacheInMemory(true)
				// 设置下载的图片是否缓存在SD卡中
				.cacheOnDisc(true).bitmapConfig(Config.RGB_565)
				.imageScaleType(ImageScaleType.IN_SAMPLE_INT)
				// 创建配置过的DisplayImageOption对象
				.build();
	}

	@Override
	public void convert(ViewHolder helper, PhotoUpImageBucket item, int position) {

		helper.setText(R.id.text_numofphoto, "" + item.getCount());
		helper.setText(R.id.text_fromwhichapp, item.getBucketName());
		ImageView imageViews = helper.getView(R.id.img_albumitem);
		imageLoader
				.displayImage("file://"
						+ item.getImageList().get(0).getImagePath(), imageViews,
						options);
	}

	/** 设置相册列表数据，主要用于把相册数据传递到Adapter中 */
	public void setArrayList(List<PhotoUpImageBucket> mlist) {
		this.mlist = mlist;
	}

}
