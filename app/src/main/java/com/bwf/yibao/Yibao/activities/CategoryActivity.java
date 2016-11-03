package com.bwf.yibao.Yibao.activities;

import android.annotation.TargetApi;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.support.v4.widget.SwipeRefreshLayout;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bwf.yibao.R;
import com.bwf.yibao.Yibao.adapters.GoodsListAdapter;
import com.bwf.yibao.Yibao.entities.Goods;
import com.bwf.yibao.Yibao.entities.RequestParams;
import com.bwf.yibao.framwork.http.HttpHelper;
import com.bwf.yibao.framwork.http.HttpUtil;
import com.bwf.yibao.framwork.tools.GoodsComparator;
import com.bwf.yibao.framwork.utils.LogUtils;
import com.bwf.yibao.framwork.utils.ToastUtil;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class CategoryActivity extends BaseActivity {
    RelativeLayout selectedView;
    LinearLayout ll_container;
    SwipeRefreshLayout swipeRefreshLayout;
    ListView listView;
    GoodsListAdapter goodsListAdapter;
    private int currentPage;
    int firstVisibleItem = -1 ;
    int visibleItemCount = -1;
    int totalCount = -1;
    TextView mCenter;
    RequestParams requestParams;

    @Override
    public int getContentViewId() {
        return R.layout.activity_category;
    }

    @Override
    public void beforeInitView(Bundle savedInstanceState) {

    }

    @Override
    public void initView() {
        ll_container = findViewByIdNoCast(R.id.ll_container);

        mCenter = findViewByIdNoCast(R.id.mCenter);

        swipeRefreshLayout = findViewByIdNoCast(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setColorSchemeColors(Color.RED,Color.GREEN, Color.BLUE);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if(requestParams.type.equals(RequestParams.TYPE_00))
                    requestParams.conditions = "1";
                HttpHelper.getGoods(requestParams ,new HttpUtil.CallBack(){

                    @Override
                    public void onSuccess(String result) {
                        swipeRefreshLayout.setRefreshing(false);
                        List<Goods> goodsList = new ArrayList<Goods>(Arrays.asList(new Gson().fromJson(result, Goods[].class)));;
                        if(goodsList.isEmpty()){
                            ToastUtil.showToast("加载到底...");
                            return;
                        }
                        goodsListAdapter.goodsList = goodsList;
                        goodsListAdapter.notifyDataSetInvalidated();

                    }

                    @Override
                    public void onFailed(String error) {
                        swipeRefreshLayout.setRefreshing(false);
                        listView.setEmptyView(findViewById(R.id.empty_view));
                    }
                });
            }
        });
        swipeRefreshLayout.setProgressViewOffset(false, 0, 40);
        swipeRefreshLayout.setRefreshing(true);
        listView = findViewByIdNoCast(R.id.listView_goodsList);
        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView absListView, int i) {

                if(i == SCROLL_STATE_TOUCH_SCROLL && firstVisibleItem + visibleItemCount == totalCount){
                    if(requestParams.type.equals(RequestParams.TYPE_00))
                        getDataFromSever();
                }
            }

            @Override
            public void onScroll(AbsListView absListView, int firstVisibleItem, int visibleItemCount, int totalCount) {
                 CategoryActivity.this.firstVisibleItem = firstVisibleItem;
                CategoryActivity.this.visibleItemCount = visibleItemCount;
                CategoryActivity.this.totalCount = totalCount;
            }
        });

        setOnClick(R.id.iv_left);
    }

    @Override
    public void initData() {
        Intent intent = getIntent();
        requestParams= (RequestParams) intent.getSerializableExtra("params");
        for (int i = 0; i < ll_container.getChildCount(); i++){
            ll_container.getChildAt(i).setOnClickListener(this);
        }

        goodsListAdapter = new GoodsListAdapter(this);
        listView.setAdapter(goodsListAdapter);
        getDataFromSever();
    }
    GoodsComparator comparator = new GoodsComparator();
    ImageView imageView;
    @Override
    public void onClick(View view) {

            switch (view.getId()){
                case R.id.rl_price:

                    //与上一次点击进行比较
                    if(view != selectedView){//如果与上一次点击不同
                        //恢复上一个控件的状态
                        if(selectedView != null){
                            selectedView.getChildAt(0).setSelected(false);
                        }
                        //改变当前控件状态
                        selectedView = (RelativeLayout) view;
                        selectedView.getChildAt(0).setSelected(true);
                        imageView = (ImageView)selectedView.getChildAt(1);
                        imageView.setImageResource(R.mipmap.indicator_red_selected_up);
                        //业务逻辑
                        comparator = new GoodsComparator();
                        comparator.price = true;
                    }else {
                        if(imageView.isSelected()){
                            //改变状态
                            imageView.setSelected(false);
                            imageView.setImageResource(R.mipmap.indicator_red_selected_up);
                            goodsListAdapter.sort(comparator);
                            goodsListAdapter.notifyDataSetChanged();
                            return;
                        }else{
                            imageView.setSelected(true);
                            imageView.setImageResource(R.mipmap.indicator_red_selected_down);
                            goodsListAdapter.sort(comparator);
                            goodsListAdapter.reverse();
                            goodsListAdapter.notifyDataSetChanged();
                            return;
                        }

                    }
                    goodsListAdapter.sort(comparator);
                    goodsListAdapter.notifyDataSetChanged();
                break;
                case R.id.rl_renqi:
                    if(selectedView != view){//当前选中的view为null或者为其他view
                        //恢复状态
                        if(selectedView != null){
                            selectedView.getChildAt(0).setSelected(false);
                            if(selectedView.getId() == R.id.rl_price){//是否包含多项选择的view
                                imageView.setImageResource(R.mipmap.indicator_red_normal);
                                imageView.setSelected(false);
                            }
                        }
                        //改变当前状态
                        selectedView = (RelativeLayout) view;
                        selectedView.getChildAt(0).setSelected(true);
                        //逻辑代码
                        comparator = new GoodsComparator();
                        comparator.vote = true;
                    }
                    goodsListAdapter.sort(comparator);
                    goodsListAdapter.notifyDataSetChanged();
                    break;
                case R.id.rl_zuixin:
                    if(selectedView != view){//当前选中的view为null或者为其他view
                        //恢复状态
                        if(selectedView != null){
                            selectedView.getChildAt(0).setSelected(false);
                            if(selectedView.getId() == R.id.rl_price){//是否包含多项选择的view
                                imageView.setImageResource(R.mipmap.indicator_red_normal);
                                imageView.setSelected(false);
                            }
                        }
                        //改变当前状态
                        selectedView = (RelativeLayout) view;
                        selectedView.getChildAt(0).setSelected(true);
                        //逻辑代码
                        comparator = new GoodsComparator();
                        comparator.time = true;
                    }
                    goodsListAdapter.sort(comparator);
                    goodsListAdapter.notifyDataSetChanged();
                    break;
                case R.id.rl_xiaoliang:
                    if(selectedView != view){//当前选中的view为null或者为其他view
                        //恢复状态
                        if(selectedView != null){
                            selectedView.getChildAt(0).setSelected(false);
                            if(selectedView.getId() == R.id.rl_price){//是否包含多项选择的view
                                imageView.setImageResource(R.mipmap.indicator_red_normal);
                                imageView.setSelected(false);
                            }
                        }
                        //改变当前状态
                        selectedView = (RelativeLayout) view;
                        selectedView.getChildAt(0).setSelected(true);
                        //逻辑代码
                        comparator = new GoodsComparator();
                        comparator.saleState = true;
                    }
                    goodsListAdapter.sort(comparator);
                    goodsListAdapter.notifyDataSetChanged();
                    break;
                case R.id.iv_left:
                    finish();

                case R.id.mCenter:

                    break;
        }




    }
    private void getDataFromSever(){
        if(requestParams.type.equals( RequestParams.TYPE_00))
            requestParams.conditions = ++currentPage + "";

        LogUtils.i("zc", requestParams.toString());
        HttpHelper.getGoods(requestParams, new HttpUtil.CallBack() {
            @Override
            public void onSuccess(String result) {
                swipeRefreshLayout.setRefreshing(false);
                List<Goods> goodsList = new ArrayList<Goods>(Arrays.asList(new Gson().fromJson(result, Goods[].class)));
                if(goodsList == null){
                    return;
                }
                if(goodsList != null && goodsList.isEmpty()){
                    ToastUtil.showToast("加载到底...");
                    return;
                }
                goodsListAdapter.addAll(goodsList);
                goodsListAdapter.notifyDataSetChanged();

            }

            @Override
            public void onFailed(String error) {
                swipeRefreshLayout.setRefreshing(false);
                listView.setEmptyView(findViewById(R.id.empty_view));
            }
        });
    }
}
