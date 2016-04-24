/** 
 * @Title: ImageShareListAdapter.java 
 * @Package com.qst.fly.adapter 
 * @Description: TODO 
 * @author yanfeizhao 417470640@qq.com 
 * @date 2016-3-15 上午10:02:17 
 * @version V1.0 
 */  
package com.qst.fly.adapter;
import java.io.File;
import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.widget.ImageView;

import com.qst.fly.R;
import com.qst.fly.entity.Picture;
import com.squareup.picasso.Picasso;

 public class ImageShareListAdapter extends CommonAdapter<Picture>{

	private static final int LAYOUT_ID = R.layout.item_image_sharelist;

	private static final String TAG = "ImageShareListAdapter";
	
	private Context mContext;
	private int mNumOfNetPic;
	public ImageShareListAdapter(Context context, List<Picture> mDatas) {
		super(context, mDatas, LAYOUT_ID);
		this.mContext=context;
	} 
	
	@Override
	public void convert(ViewHolder helper, Picture item, int position) {
		mNumOfNetPic=mOnLoadNetPicFinish.getNumOfNetPic();
		ImageView img=(ImageView)helper.getView(R.id.img_sharelist);
		//如果是本地图片,本地获取
		if(position>=mNumOfNetPic){
			Picasso.with(mContext).load(new File(item.img))
//								  .resize(495,880 )
								  .placeholder(R.drawable.before)
								  .into(img);
		}
		else{
			Picasso.with(mContext).load(item.img).placeholder(R.drawable.before).
			error(R.drawable.demo).into(img);
		}
	}
	
	public interface OnLoadNetPicFinish{
		 int getNumOfNetPic();
	}
	private OnLoadNetPicFinish mOnLoadNetPicFinish;
	public void setOnLoadNetPicFinish(OnLoadNetPicFinish onLoadNetPicFinish){
		this.mOnLoadNetPicFinish=onLoadNetPicFinish;
	}
} 

