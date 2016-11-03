package com.bwf.yibao.Yibao.adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bwf.yibao.R;
import com.bwf.yibao.Yibao.entities.Goods;
import com.bwf.yibao.framwork.tools.ImageLoader;
import com.bwf.yibao.framwork.tools.UtilsURLPath;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by nicholas on 2016/9/2.
 */
public class HotGoodsAdapter extends BaseAdapter {

    public List<Goods> hotGoods = new ArrayList<Goods>();
    ImageLoader imageLoader;
    Activity activity;

    public HotGoodsAdapter(Activity activity) {
        this.activity = activity;
        imageLoader = new ImageLoader(activity);
    }

    @Override
    public int getCount() {
        return hotGoods.size();
    }

    @Override
    public Object getItem(int i) {
        return hotGoods.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }
    ViewHolder viewHolder;
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
       if(view == null){
           view = LayoutInflater.from(activity).inflate(R.layout.hot_goods, null);
           viewHolder = new ViewHolder(view);
           view.setTag(viewHolder);
       }else{
           viewHolder = (ViewHolder) view.getTag();
       }
        Goods goods = (Goods) getItem(i);

        viewHolder.goodsPic.setImageResource(R.mipmap.user2);
        if(!goods.getImagePath().isEmpty()){
            String imageUrl = UtilsURLPath.downloadPic + goods.getImagePath().get(0);
            imageLoader.loadImage(viewHolder.goodsPic,imageUrl );
        }
        viewHolder.goodsName.setText(goods.getGoodsName());
        return view;
    }
    class ViewHolder{
        ImageView goodsPic;
        TextView goodsName;
        public ViewHolder(View rootView){
            goodsPic = (ImageView) rootView.findViewById(R.id.goodsPic);
            goodsName = (TextView) rootView.findViewById(R.id.goodsName);
        }
    }
}
