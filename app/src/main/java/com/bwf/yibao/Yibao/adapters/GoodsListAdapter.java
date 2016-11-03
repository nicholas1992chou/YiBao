package com.bwf.yibao.Yibao.adapters;

import android.app.Activity;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bwf.yibao.R;
import com.bwf.yibao.Yibao.entities.Goods;
import com.bwf.yibao.framwork.tools.GoodsComparator;
import com.bwf.yibao.framwork.tools.ImageLoader;
import com.bwf.yibao.framwork.tools.UtilsURLPath;
import com.bwf.yibao.framwork.utils.RandomUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by nicholas on 2016/9/5.
 */
public class GoodsListAdapter extends BaseAdapter {
   public List<Goods> goodsList = new ArrayList<Goods>();
    ImageLoader imageLoader;
    Activity activity;

    public GoodsListAdapter(Activity activity) {
        this.activity = activity;
        imageLoader = new ImageLoader(activity);
    }

    @Override
    public int getCount() {
        return goodsList.size();
    }

    @Override
    public Object getItem(int i) {
        return goodsList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder = null;
        if(view == null){
            view = LayoutInflater.from(activity).inflate(R.layout.item_goods, null);
            viewHolder = new ViewHolder(view);
            view.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) view.getTag();
        }
        Goods goods = (Goods)getItem(i);
        if(!goods.getImagePath().isEmpty()){
            String imageUrl = UtilsURLPath.downloadPic + goods.getImagePath().get(0);
            imageLoader.loadImage(viewHolder.goodsPic_iv,imageUrl );
        }
        viewHolder.goodsName_tv.setText(goods.getGoodsName());
        viewHolder.price_tv.setText("￥" + goods.getPrice());
        viewHolder.originalPrice_tv.setText("￥" + goods.getOriginalprice());
        viewHolder.originalPrice_tv.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
        viewHolder.originalPrice_tv.getPaint().setAntiAlias(true);
        viewHolder.time_tv.setText(goods.getTime());
        if(goods.vote == 0)
        goods.vote = RandomUtil.getRandomInt(100000);
        viewHolder.tv_pingjia.setText( goods.vote+ "条评价");
        if(goods.getState() == 0)
            goods.setState(RandomUtil.getRandomInt(100));
        viewHolder.tv_haoping.setText(goods.getState()+ "%好评");
        return view;
    }

    public void reverse() {
        Collections.reverse(goodsList);
    }

    class ViewHolder{
        ImageView goodsPic_iv;
        TextView goodsName_tv, price_tv,originalPrice_tv, time_tv , tv_pingjia, tv_haoping;
        public ViewHolder(View rootView){
            goodsPic_iv = (ImageView)rootView.findViewById(R.id.goodsPic_iv);
            goodsName_tv = (TextView)rootView.findViewById(R.id.goodsName_tv);
            price_tv = (TextView)rootView.findViewById(R.id.price_tv);
            originalPrice_tv = (TextView)rootView.findViewById(R.id.originalPrice_tv);
            time_tv = (TextView)rootView.findViewById(R.id.time_tv);
            tv_pingjia = (TextView)rootView.findViewById(R.id.tv_command);
            tv_haoping = (TextView)rootView.findViewById(R.id.tv_haoping);
        }
    }
    public void addAll(List<Goods> list){
        goodsList.addAll(list);
    }
    public void removeAll(List<Goods> list){
        goodsList.removeAll(list);
    }
    public void sort(GoodsComparator goodsComparator){
        Collections.sort(this.goodsList, goodsComparator);
    }
}
