package com.bwf.yibao.framwork.tools;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.support.v4.util.LruCache;
import android.widget.ImageView;


import com.bwf.yibao.framwork.utils.MD5Util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ImageLoader {
	public static final int IMAGE_WIDTH = 80;
	public static final int IMAGE_HEIGHT = 80;
	private static final int totalthread = 3;

	public static final String EXTENSION_NAME = ".jpg";
	/**
	 *	isBusy 用户快速滑动， 不加载图片
	 */
	private boolean isBusy = false;
	private LruCache<String, Bitmap> mLruCache;
	private ExecutorService mExecutorService;
	private Activity mActivity;
	public ImageLoader(Activity mActivity){
		super();
		this.mActivity = mActivity;
		init();
	}
	private void init() {
		mExecutorService  = Executors.newFixedThreadPool(totalthread);
		//获取系统分配给app的最大空间
		long maxMemory = Runtime.getRuntime().maxMemory();
		mLruCache = new LruCache<String, Bitmap>((int) (maxMemory / 8));
	}

	/**
	 *
	 * @param iv 显示图片的ImageView
	 * @param imageUrl  图片地址
     */
	public void loadImage(ImageView iv, String imageUrl){
		//设置标志，处理图片错乱
		iv.setTag(imageUrl);
		//从LruCache中取
		Bitmap bmp = getBmpFromLruCache(imageUrl);
		//如果LruCache中有图片
		if(bmp != null){
			setBmp(bmp, iv, imageUrl);
			return;
		}

		//LruCache中没有图片，则本地缓存中取
		bmp = getBmpFromLocal(imageUrl);
		if(bmp != null){
			setBmp(bmp, iv, imageUrl);
			//保存到LruCache
			//saveToLruCache(imageName, getSmallBmp(bmp));
			saveToLruCache(imageUrl, bmp);
			return;
		}
		//从网络上获取图片
		LoadRunnable task = new LoadRunnable(iv, imageUrl);
		//mExecutorService.submit(task);
		mExecutorService.execute(task);
	}

	
	
	
	
	private void saveToLruCache(String imageName, Bitmap smallBmp) {
			mLruCache.put(imageName, smallBmp);
	}
	private Bitmap getSmallBmp(Bitmap bmp) {
		Bitmap newBmp = Bitmap.createScaledBitmap(bmp, IMAGE_WIDTH, IMAGE_HEIGHT, true);
		//回收bitmap
		return newBmp;
	}
	private Bitmap getBmpFromLocal(String imageUrl) {
		//sd卡是否可用
		if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED) ){
			//将原名称加密， 即与已经加密过的名称一致。
			String imageName = MD5Util.string2MD5(imageUrl) + EXTENSION_NAME;
				Bitmap bitmap = BitmapFactory.decodeFile(FilePath.getImageCacheDir() + File.separator + imageName );
			if(bitmap != null)
				saveToLruCache(imageUrl, bitmap);//保存图片原来名称
			return bitmap;
		}
		return null;
	}
	private void setBmp(Bitmap bmp, ImageView iv, String imageName) {
		//如果图片与imageView不是对应的则不显示
		if(!imageName.equals(iv.getTag())){
			return;
		}
		//获取压缩图片（缩略图）
		//bmp = getSmallBmp(bmp);

		//更新UI
		mActivity.runOnUiThread(new UpdateUi(iv, bmp));

	}

	private Bitmap getBmpFromLruCache(String imageUrl) {
		return mLruCache.get(imageUrl);
	}
	private class UpdateUi implements Runnable{
		ImageView iv;
		Bitmap bmp;
		public UpdateUi(ImageView iv,Bitmap bmp){
			this.iv = iv;
			this.bmp = bmp;
		}
		@Override
		public void run() {
			iv.setImageBitmap(bmp);
		}
		
	}
	private class LoadRunnable implements Runnable{
		ImageView iv;
		String imageUrl;
		LoadRunnable(ImageView iv, String imageUrl){
			this.iv = iv;
			this.imageUrl = imageUrl;
		}
		@Override
		public void run() {
				try {

					//URL url = new URL(Url.imageUrl + File.separator + imageName);
					URL url = new URL(imageUrl);
					//获取连接
					HttpURLConnection conn = (HttpURLConnection) url.openConnection();
					//设置连接超时
					conn.setConnectTimeout(8*1000);
					 //设置读取文件超时
					conn.setReadTimeout(60*1000);
					if(conn.getResponseCode() == 200){
						Bitmap bmp = BitmapFactory.decodeStream(conn.getInputStream());
						if(bmp != null){
							//存入本地文件
							saveToLocal(imageUrl, bmp);
							//缩略图存入LruCache
							saveToLruCache(imageUrl, bmp);
							//更新UI
							setBmp(bmp, iv, imageUrl);
						}
					}
					
				} catch (Exception e) {
					e.printStackTrace();
				}
		}
		private void saveToLocal(String imageUrl, Bitmap bmp) {
			//如果文件的所有父级目录不存在， 则先创建。
			File dir = new File(FilePath.getImageCacheDir());
			if(!dir.exists()){
				dir.mkdirs();
			}
			//加密
			String imageName = MD5Util.string2MD5(imageUrl) + EXTENSION_NAME;
			File file = new File(dir, imageName);
			try {
				FileOutputStream fos = new FileOutputStream(file);
				//压缩图片
				bmp.compress(Bitmap.CompressFormat.JPEG, 100, fos);
				
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} 
		}
	}

		
}
