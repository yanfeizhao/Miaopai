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

public class CameraAlbumDetailAdapter extends CommonAdapter<PhotoUpImageItem> {

	/** 相册详情页里面的图片集合 */
	private List<PhotoUpImageItem> list;
	/** 图片加载器 */
	private ImageLoader imageLoader;
	/** 图片加载器的设置 */
	private DisplayImageOptions options;

	public CameraAlbumDetailAdapter(Context context,
			List<PhotoUpImageItem> mDatas, int itemLayoutId) {
		super(context, mDatas, itemLayoutId);

		// 实例化图片加载器
		imageLoader = ImageLoader.getInstance();
		// 使用DisplayImageOption.Builder()创建DisplayImageOptions
		options = new DisplayImageOptions.Builder()
				.showStubImage(R.drawable.ic_launcher)
				// 设置图片下载期间显示的图片
				.showImageForEmptyUri(R.drawable.ic_launcher)
				// 设置图片Uri为空或是错误的时候显示的图片
				.showImageOnFail(R.drawable.ic_launcher)
				// 设置图片加载或解码过程中发生错误显示的图片
				.cacheInMemory(true)
				// 设置下载的图片是否缓存在内存中
				.cacheOnDisc(true)
				// 设置下载的图片是否缓存在SD卡中
				.bitmapConfig(Config.ARGB_8888)
				// 创建配置过的DisplayImageOption对象
				.imageScaleType(ImageScaleType.IN_SAMPLE_INT).build(); 
	}

	@Override
	public void convert(ViewHolder helper, PhotoUpImageItem item, int position) {

		//图片加载器的使用代码，就这一句代码即可实现图片的加载。请注意
        //这里的uri地址，因为我们现在实现的是获取本地图片，所以使
        //用"file://"+图片的存储地址。如果要获取网络图片，
        //这里的uri就是图片的网络地址。
		ImageView ImageView = helper.getView(R.id.gridview_item);
        imageLoader.displayImage("file://"+item.getImagePath(), ImageView, options);
        
	}

}
