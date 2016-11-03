package com.bwf.yibao.framwork.tools;

import android.annotation.TargetApi;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.util.LruCache;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bwf.yibao.framwork.utils.LogUtils;

import java.util.LinkedList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;

/**
 * Created by nicholas on 2016/9/22.
 */
public class ZcImageLoader {
    private static final int THREAD_COUNT = 3;

    /**
     * 单例
     */
    private static ZcImageLoader imageLoader;

    /**
     * 线程池
     */
    private ExecutorService mThreadPool;
    /**
     * 内存缓存
     */
    private LruCache<String, Bitmap> mLruCache;
    /**
     * 图片加载策略
     */
    private Type mType;
    /**
     * 利用信号量控制线程执行任务
     */
    Semaphore mSemaphore;

    /**
     * 单例
     */
    private ZcImageLoader(int threadCount, Type type) {
        //初始化
        init(threadCount, type);
    }

    private void init(int threadCount, Type type) {
        if (threadCount <= 0)
            threadCount = THREAD_COUNT;
        mThreadPool = Executors.newFixedThreadPool(threadCount);

        int maxMemory = (int) Runtime.getRuntime().maxMemory();
        mLruCache = new LruCache<String, Bitmap>(maxMemory / 8) {
            @Override
            protected int sizeOf(String key, Bitmap value) {//确定每个bitmap的大小
                //3.1之前bitmap获取大小与之后不同
                if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.HONEYCOMB_MR1)
                    return value.getRowBytes() * value.getHeight();
                return value.getByteCount();
            }
        };
        mSemaphore = new Semaphore(threadCount);

        mHandlerThread = new HandlerThread("executeTask");
        mHandlerThread.start();
        mThreadPoolHandler = new Handler(mHandlerThread.getLooper()){
            @Override
            public void handleMessage(Message msg) {
                //去任务队列中取任务
                if (mType == Type.LIFO)
                    mThreadPool.execute(taskQueue.removeLast());
                else
                    mThreadPool.execute(taskQueue.removeFirst());
                try {
                    mSemaphore.acquire();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        taskQueue = new LinkedList<Runnable>();
        if (type == null)
            type = Type.LIFO;
        mType = type;
        //
        mUiHandler = new Handler(Looper.getMainLooper()) {//避免此类在工作线程调用时，不能更新UI
                @Override
                public void handleMessage(Message msg) {
                    ImageHolder imageHolder = (ImageHolder) msg.obj;
                    if (!imageHolder.path.equals(imageHolder.imageView.getTag().toString()))
                        return;
                    imageHolder.imageView.setImageBitmap(imageHolder.bitmap);
                }
            };
    }

    /**
     * @return 单例
     */
    public static ZcImageLoader getInstance(int threadCount, Type type) {
        if (imageLoader == null) {
            synchronized (ZcImageLoader.class) {
                if (imageLoader == null) {
                    imageLoader = new ZcImageLoader(threadCount, type);
                }
            }
        }
        return imageLoader;
    }

    /**
     * 指定图片加载策略
     */
    public enum Type {
        FIFO, LIFO;
    }

    /**
     * 任务队列
     */
    private LinkedList<Runnable> taskQueue;


    /**
     * UI handler
     */
    Handler mUiHandler;

    /**
     * 轮询线程里的handler
     */
    Handler mThreadPoolHandler;
    /**
     * 后台轮询线程
     */
    HandlerThread mHandlerThread;

    /**
     * 图片加载
     */
    public void loadImage(String path, ImageView imageView) {
        //处理图片乱序
        imageView.setTag(path);
        //从LruCache中获取图片
        Bitmap bitmap = getBitmapFromLruCache(path);

        if (bitmap != null) {
            setBitmap(path, imageView, bitmap);
            return;
        }
        //添加任务
        addTask(path, imageView);
    }

    //添加 加载图片的任务
    private void addTask(final String path, final ImageView imageView) {
        Runnable task = new Runnable() {
            @Override
            public void run() {
                //从本地加载图片， 压缩
                //1.获取imageView的尺寸
                ImageViewSize imageViewSize = getImageViewSize(imageView);
                //2.压缩图片
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inJustDecodeBounds = true;
                //（1）获取图片本身的尺寸
                Bitmap bitmap = BitmapFactory.decodeFile(path, options);
                int width = options.outWidth;
                int height = options.outHeight;

                if (width > height && width > imageViewSize.width)
                    options.inSampleSize = width / imageViewSize.width;
                else if (height > width && height > imageViewSize.height)
                    options.inSampleSize = height / imageViewSize.height;
                else
                    options.inSampleSize = 1;
                options.inJustDecodeBounds = false;
                bitmap = BitmapFactory.decodeFile(path, options);
                if (bitmap != null) {
                    setBitmap(path, imageView, bitmap);
                    mLruCache.put(path, bitmap);
                }
                //释放信号量
                mSemaphore.release();
                return;
                //从网络获取图片

            }
        };
        taskQueue.add(task);
        mThreadPoolHandler.sendEmptyMessage(0);

    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private ImageViewSize getImageViewSize(ImageView imageView) {
        ImageViewSize imageSize = new ImageViewSize();
        ViewGroup.LayoutParams lp = imageView.getLayoutParams();
        int width = 0, height = 0;
        width = imageView.getWidth();
        if (width <= 0)
            width = lp.width;
        if (width <= 0)
            width = imageView.getMaxWidth();
        if (width <= 0)
            width = imageView.getContext().getResources().getDisplayMetrics().widthPixels;
        height = imageView.getHeight();
        if (height <= 0)
            height = lp.width;
        if (height <= 0)
            height = imageView.getMaxHeight();
        if (height <= 0)
            height = imageView.getContext().getResources().getDisplayMetrics().heightPixels;
        imageSize.width = width;
        imageSize.height = height;
        return imageSize;
    }

    private void setBitmap(String path, ImageView imageView, Bitmap bitmap) {
        //发消息更新ui
        ImageHolder imageHolder = new ImageHolder(path, bitmap, imageView);
        mUiHandler.sendMessage(Message.obtain(mUiHandler, 1, imageHolder));
    }

    /**
     * 从LruCache中获取图片
     *
     * @param path
     * @return
     */
    private Bitmap getBitmapFromLruCache(String path) {
        return mLruCache.get(path);
    }

    class ImageHolder {
        String path;
        Bitmap bitmap;
        ImageView imageView;

        public ImageHolder(String path, Bitmap bitmap, ImageView imageView) {
            this.path = path;
            this.bitmap = bitmap;
            this.imageView = imageView;
        }
    }

    class ImageViewSize {
        public int width;
        public int height;
    }
}
