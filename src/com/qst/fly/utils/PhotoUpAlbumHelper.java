package com.qst.fly.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;
import android.provider.MediaStore.Images.Media;
import android.provider.MediaStore.Images.Thumbnails;
import android.util.Log;

import com.qst.fly.entity.PhotoUpImageBucket;
import com.qst.fly.entity.PhotoUpImageItem;

/**
 * @ClassName: PhotoUpAlbumHelper
 * @Description: 通过异步线程类获取相册以及相册内部图片
 * @author wzg0816 466959515@qq.com
 * @date 2016-3-15 上午10:30:58
 */
public class PhotoUpAlbumHelper extends AsyncTask<Object, Object, Object> {

	/** 获得当前对象的类型,返回源代码中给出的底层类的简称 */
	final String TAG = getClass().getSimpleName();
	Context context;
	/** 应用程序之间数据共享 */
	ContentResolver cr;
	/** 缩略图列表 */
	HashMap<String, String> thumbnailList = new HashMap<String, String>();
	/** 专辑列表 */
	List<HashMap<String, String>> albumList = new ArrayList<HashMap<String, String>>();
	/** 相册页面对应的具体集合 */
	HashMap<String, PhotoUpImageBucket> bucketList = new HashMap<String, PhotoUpImageBucket>();

	private GetAlbumList getAlbumList;

	/** 创建该类的构造方法，获取实例 */
	public static PhotoUpAlbumHelper getHelper() {
		PhotoUpAlbumHelper instance = new PhotoUpAlbumHelper();
		return instance;

	}

	/**
	 * 初始化cr
	 */
	public void init(Context context) {
		if (this.context == null) {
			this.context = context;
			cr = context.getContentResolver();
		}
	}

	/**
	 * 得到缩略图，主要是得到了图片ID值
	 */
	private void getThumbnail() {
		String[] projection = { Thumbnails._ID, Thumbnails.IMAGE_ID,
				Thumbnails.DATA };
		Cursor cursor1 = Thumbnails.queryMiniThumbnails(cr,
				Thumbnails.EXTERNAL_CONTENT_URI, Thumbnails.MINI_KIND,
				projection);
		// 调用得到缩略图数据的方法
		getThumbnailColumnData(cursor1);
		cursor1.close();
	}

	/**
	 * 从数据库中得到缩略图
	 */
	private void getThumbnailColumnData(Cursor cur) {
		if (cur.moveToFirst()) {
			int image_id;
			String image_path;
			int image_idColumn = cur.getColumnIndex(Thumbnails.IMAGE_ID);
			int dataColumn = cur.getColumnIndex(Thumbnails.DATA);
			do {
				image_id = cur.getInt(image_idColumn);
				image_path = cur.getString(dataColumn);
				thumbnailList.put("" + image_id, image_path);
			} while (cur.moveToNext());
		}
	}

	/**
	 * 是否创建了图片集
	 */
	boolean hasBuildImagesBucketList = false;

	/**
	 * 得到图片集
	 */
	private void buildImagesBucketList() {
		// 构造缩略图索引
		getThumbnail();
		// 构造相册索引
		String columns[] = new String[] { Media._ID, Media.BUCKET_ID,
				Media.PICASA_ID, Media.DATA, Media.DISPLAY_NAME, Media.TITLE,
				Media.SIZE, Media.BUCKET_DISPLAY_NAME };
		// 得到一个游标
		Cursor cur = cr.query(Media.EXTERNAL_CONTENT_URI, columns, null, null,
				Media.DATE_MODIFIED + " desc");
		if (cur.moveToFirst()) {
			// 获取指定列的索引
			int photoIDIndex = cur.getColumnIndexOrThrow(Media._ID);
			int photoPathIndex = cur.getColumnIndexOrThrow(Media.DATA);
			int bucketDisplayNameIndex = cur
					.getColumnIndexOrThrow(Media.BUCKET_DISPLAY_NAME);
			int bucketIdIndex = cur.getColumnIndexOrThrow(Media.BUCKET_ID);
			/**
			 * Description:这里增加了一个判断：判断照片的名 字是否合法，例如.jpg .png等没有名字的格式
			 * 如果图片名字是不合法的，直接过滤掉
			 */
			do {
				if (cur.getString(photoPathIndex)
						.substring(
								cur.getString(photoPathIndex).lastIndexOf("/") + 1,
								cur.getString(photoPathIndex).lastIndexOf("."))
						.replaceAll(" ", "").length() <= 0) {
					Log.d(TAG, "出现了异常图片的地址：cur.getString(photoPathIndex)="
							+ cur.getString(photoPathIndex));
				} else {
					String _id = cur.getString(photoIDIndex);
					String path = cur.getString(photoPathIndex);
					String bucketName = cur.getString(bucketDisplayNameIndex);
					String bucketId = cur.getString(bucketIdIndex);
					PhotoUpImageBucket bucket = bucketList.get(bucketId);
					// 这里完成图片归并到响应的相册里去
					if (bucket == null) {
						bucket = new PhotoUpImageBucket();
						bucketList.put(bucketId, bucket);
						bucket.imageList = new ArrayList<PhotoUpImageItem>();
						bucket.bucketName = bucketName;
					}
					bucket.count++;
					PhotoUpImageItem imageItem = new PhotoUpImageItem();
					imageItem.setImageId(_id);
					imageItem.setImagePath(path);
					bucket.imageList.add(imageItem);
				}
			} while (cur.moveToNext());
		}
		cur.close();
		hasBuildImagesBucketList = true;
	}

	/**
	 * 得到图片集
	 * 
	 * @param refresh
	 * @return
	 */
	private List<PhotoUpImageBucket> getImagesBucketList(boolean refresh) {
		if (refresh || (!refresh && !hasBuildImagesBucketList)) {
			buildImagesBucketList();
		}
		List<PhotoUpImageBucket> tmpList = new ArrayList<PhotoUpImageBucket>();
		Iterator<Entry<String, PhotoUpImageBucket>> itr = bucketList.entrySet()
				.iterator();
		// 将Hash转化为List
		while (itr.hasNext()) {
			Map.Entry<String, PhotoUpImageBucket> entry = (Map.Entry<String, PhotoUpImageBucket>) itr
					.next();
			tmpList.add(entry.getValue());
		}
		return tmpList;
	}

	/**
	 * 得到原始图像路径
	 * 
	 * @param image_id
	 * @return
	 */
	private String getOriginalImagePath(String image_id) {
		String path = null;
		String[] projection = { Media._ID, Media.DATA };
		Cursor cursor = cr
				.query(Media.EXTERNAL_CONTENT_URI, projection, Media._ID + "="
						+ image_id, null, Media.DATE_MODIFIED + " desc");
		if (cursor != null) {
			cursor.moveToFirst();
			path = cursor.getString(cursor.getColumnIndex(Media.DATA));
		}
		return path;
	}

	@Override
	protected Object doInBackground(Object... params) {
		// TODO Auto-generated method stub
		return getImagesBucketList((Boolean) (params[0]));
	}

	public void setGetAlbumList(GetAlbumList getAlbumList) {
		this.getAlbumList = getAlbumList;
	}

	/** 回调接口，当完成相册和图片的获取之后，调用该接口的方法传递数据 */
	public interface GetAlbumList {
		public void getAlbumList(List<PhotoUpImageBucket> list);
	}
	
    /**传送数据*/
	protected void onPostExecute(Object result) {
		super.onPostExecute(result);
		getAlbumList.getAlbumList((List<PhotoUpImageBucket>) result);
	}
}
