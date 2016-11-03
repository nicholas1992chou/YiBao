package com.bwf.yibao.Yibao.adapters;

import android.app.Activity;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.bwf.yibao.R;
import com.bwf.yibao.Yibao.entities.Goods;
import com.bwf.yibao.framwork.tools.ImageLoader;
import com.bwf.yibao.framwork.tools.UtilsURLPath;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by nicholas on 2016/9/6.
 */
public class ViewPagerAdapter extends PagerAdapter {
    public List<View> viewList = new ArrayList<View>();
    public List<Goods> goodsList = new ArrayList<Goods>();
    Activity mActivity;
    ImageLoader imageLoader;
    public ViewPagerAdapter(Activity activity){
        this.mActivity = activity;
        imageLoader = new ImageLoader(activity);
    }

    @Override
    public int getCount() {
        return Integer.MAX_VALUE;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        final int nposition = position % goodsList.size();
        View view = LayoutInflater.from(mActivity).inflate(R.layout.home_item_viewpager, null, false);
        ImageView imageView = (ImageView) view.findViewById(R.id.home_item_imageView);
        //网络加载图片
        if (!goodsList.get(nposition).getImagePath().isEmpty()){
            String imageUrl = UtilsURLPath.downloadPic + goodsList.get(nposition).getImagePath().get(0);
            imageLoader.loadImage(imageView, imageUrl);
        }

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(mActivity, goodsList.get(nposition).getGoodsName(), Toast.LENGTH_SHORT).show();
            }
        });
        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }
}
