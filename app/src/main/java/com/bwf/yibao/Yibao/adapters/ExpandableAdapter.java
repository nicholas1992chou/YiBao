package com.bwf.yibao.Yibao.adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bwf.yibao.R;
import com.bwf.yibao.Yibao.entities.Category;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by nicholas on 2016/9/6.
 */
public class ExpandableAdapter extends BaseExpandableListAdapter {
    public Activity mActivity;
    public List<Category> categoryList = new ArrayList<Category>();
    int[] res = {R.mipmap.user2, R.mipmap.btn_white_icn_arr};

    public ExpandableAdapter(Activity mActivity) {
        this.mActivity = mActivity;
    }

    @Override
    public int getGroupCount() {
        return categoryList.size();
    }

    @Override
    public int getChildrenCount(int i) {
        Category category = categoryList.get(i);
        if (category != null) {
            List<Category.Secondary> sublist = category.getSortval();
            if (sublist != null) {
                return sublist.size();
            }
        }

        return 0;
    }

    @Override
    public Object getGroup(int i) {
        return categoryList.get(i);
    }

    @Override
    public Object getChild(int i, int i1) {
        Category category = categoryList.get(i);
        if (category != null) {
            List<Category.Secondary> sublist = category.getSortval();
            if (sublist != null) {
                return sublist.get(i1);
            }
        }
        return null;
    }

    @Override
    public long getGroupId(int i) {
        return i;
    }

    @Override
    public long getChildId(int i, int i1) {
        return i1;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int i, boolean b, View view, ViewGroup viewGroup) {
        GrowpViewHolder growpViewHolder = null;
        if (view == null) {
            view = LayoutInflater.from(mActivity).inflate(R.layout.item_category_group, viewGroup, false);
            growpViewHolder = new GrowpViewHolder(view);
        } else {
            growpViewHolder = (GrowpViewHolder) view.getTag();
        }
        Category category = categoryList.get(i);
        //设置布局内容
        growpViewHolder.icon_iv.setImageResource(res[0]);
        if (category != null) {
            growpViewHolder.categoryName_tv.setText(category.getSortkey() + "");
        }
        growpViewHolder.indicator_iv.setImageResource(res[1]);
        return view;
    }

    @Override
    public View getChildView(int i, int i1, boolean b, View view, ViewGroup viewGroup) {
        ChildViewHolder childViewHolder = null;
        if (view == null) {
            view = LayoutInflater.from(mActivity).inflate(R.layout.item_category_child, viewGroup, false);
            childViewHolder = new ChildViewHolder(view);
        } else {
            childViewHolder = (ChildViewHolder) view.getTag();
        }
        Category.Secondary secondary = (Category.Secondary) getChild(i, i1);
        if (secondary != null) {
            childViewHolder.name_tv.setText(secondary.getSortname() + "");
            childViewHolder.count_tv.setText(secondary.getSortnum() + "");
        }
        return view;
    }

    @Override
    public boolean isChildSelectable(int i, int i1) {
        return true;
    }

    public class GrowpViewHolder {
        ImageView icon_iv;
        TextView categoryName_tv;
        ImageView indicator_iv;

        public GrowpViewHolder(View convertView) {
            this.icon_iv = (ImageView) convertView.findViewById(R.id.icon_iv);
            this.categoryName_tv = (TextView) convertView.findViewById(R.id.categoryName_tv);
            this.indicator_iv = (ImageView) convertView.findViewById(R.id.indicator_iv);
            convertView.setTag(this);
        }
    }

    public class ChildViewHolder {
        TextView name_tv;
        TextView count_tv;
        public ChildViewHolder(View convertView) {
            this.name_tv = (TextView) convertView.findViewById(R.id.name_tv);
            this.count_tv = (TextView) convertView.findViewById(R.id.count_tv);
            convertView.setTag(this);
        }
    }
}
