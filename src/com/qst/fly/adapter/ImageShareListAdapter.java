/** 
 * @Title: ImageShareListAdapter.java 
 * @Package com.qst.fly.adapter 
 * @Description: TODO 
 * @author yanfeizhao 417470640@qq.com 
 * @date 2016-3-15 上午10:02:17 
 * @version V1.0 
 */  
package com.qst.fly.adapter;
import java.util.List;
import android.content.Context;
import com.qst.fly.R;
import com.qst.fly.entity.ImageInfo;

public class ImageShareListAdapter extends CommonAdapter<ImageInfo>{

	private static final int LAYOUT_ID = R.layout.item_image_sharelist;
	
	public ImageShareListAdapter(Context context, List<ImageInfo> mDatas) {
		super(context, mDatas, LAYOUT_ID);
	}
	
	@Override
	public void convert(ViewHolder helper, ImageInfo item, int position) {
		
		helper.setImageResource(R.id.img_sharelist, item.getImageId());
	}

}
