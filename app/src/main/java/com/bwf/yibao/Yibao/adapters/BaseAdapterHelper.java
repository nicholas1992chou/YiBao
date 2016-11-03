package com.bwf.yibao.Yibao.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;

/**
 * Created by nicholas on 2016/9/14.
 */
public abstract class BaseAdapterHelper<T> extends BaseAdapter{
    protected List<T> models;
    protected Context mContext;
    protected LayoutInflater layoutInflater;
    protected int mItemLayoutId;
    public BaseAdapterHelper(Context context, List<T> models, int mItemLayoutId){
        this.models = models;
        this.mContext = context;
        this.layoutInflater = LayoutInflater.from(context);
        this.mItemLayoutId = mItemLayoutId;
    }

    @Override
    public int getCount() {
        return models.size();
    }

    @Override
    public Object getItem(int position) {
        return models.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        ViewHolder viewHolder = getViewHolder(position,convertView,viewGroup);
        convertView(viewHolder, models.get(position));
        return viewHolder.getConvertView();
    }
    private ViewHolder getViewHolder(int position, View convertView, ViewGroup viewGroup){
        return ViewHolder.newInstance( mItemLayoutId, mContext,convertView, viewGroup,position);
    }

    public static class ViewHolder{
        /**
         * 用来存储布局内的view， 类似Map, key只能是Integer，采用二分查找算法
         */
        private final SparseArray<View> mViews;
        private View mConvertView;
        public ViewHolder(int layoutId, Context context,ViewGroup parent,  int position){
            mViews = new SparseArray<View>();
            mConvertView = LayoutInflater.from(context).inflate(layoutId, parent, false);
            mConvertView.setTag(this);
        }
        /**
         *
         * @param context 上下文环境
         * @param convertView 复用的view
         * @param parent    父容器
         * @param layoutId  条目的布局id
         * @param position 条目的位置
         * @return
         */
        public static ViewHolder newInstance(int layoutId, Context context,View convertView, ViewGroup parent, int position){
            if(convertView == null){
                return new ViewHolder(layoutId ,context,parent, position);
            }
            return (ViewHolder) convertView.getTag();
        }

        /**
         *
         * @param id  布局内view 的id， 通过id 获取该view对象
         * @param <T> 继承自View的泛型
         * @return
         */
       public <T extends View> T findViewById(int id){
           View view = mViews.get(id);
           if(view == null){
               view =  mConvertView.findViewById(id);
               mViews.put(id, view);
           }
           return (T)view;
       }

        /**
         *
         * @return 返回条目的布局view
         */
        public View getConvertView(){
            return mConvertView;
        }
        public ViewHolder setText(int viewId, String text){
            TextView textView = findViewById(viewId);
            textView.setText(text);
            return this;
        }
        public ViewHolder setImageResource(int viewId, int resId){
            ImageView imageView = findViewById(viewId);
            imageView.setImageResource(resId);
            return this;
        }
        public ViewHolder setImageBitmap(int viewId, Bitmap bitmap){
            ImageView imageView = findViewById(viewId);
            imageView.setImageBitmap(bitmap);
            return this;
        }
        public ViewHolder setImageUrl(int viewId, String url){
            SimpleDraweeView simpleDraweeView = findViewById(viewId);
            simpleDraweeView.setImageURI(url);
            /*ImageRequest request = ImageRequestBuilder.newBuilderWithSource(Uri.parse(url))
                    .setProgressiveRenderingEnabled(true)
                    .build();
            DraweeController controller = Fresco.newDraweeControllerBuilder()
                    .setImageRequest(request)
                    .setOldController(simpleDraweeView.getController())
                    .build();
            simpleDraweeView.setController(controller);*/


            return this;
        }
    }
    public void addAll(List<T> list){
        models.addAll(list);
    }
    public void removeAll(List<T> list){
        models.removeAll(list);
    }
    public void clear(){
        models.clear();
    }
    public void setModels(List<T> list){
        models = list;
    }
    //唯一需要实现的方法，用于给控件添加内容
    protected abstract void convertView(ViewHolder viewHolder, T t);


}
