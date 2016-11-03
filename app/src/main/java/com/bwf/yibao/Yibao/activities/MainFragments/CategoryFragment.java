package com.bwf.yibao.Yibao.activities.MainFragments;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.Toast;

import com.bwf.yibao.R;
import com.bwf.yibao.Yibao.activities.BaseFragment;
import com.bwf.yibao.Yibao.activities.CategoryActivity;
import com.bwf.yibao.Yibao.adapters.ExpandableAdapter;
import com.bwf.yibao.Yibao.entities.Category;
import com.bwf.yibao.Yibao.entities.RequestParams;
import com.bwf.yibao.framwork.http.HttpHelper;
import com.bwf.yibao.framwork.http.HttpUtil;
import com.google.gson.Gson;

import java.util.Arrays;
import java.util.List;


public class CategoryFragment extends BaseFragment {
    Activity mActivity;
    //ExpandableListView for goods category
    ExpandableListView expandableListView;
    //adpter
    ExpandableAdapter expandableAdapter;
    @Override
    protected int getResource() {
        return R.layout.category_fragment;
    }

    @Override
    protected void beforeInitView() {
        mActivity = getActivity();
    }

    @Override
    protected void initView(View rootView) {
        expandableListView = findViewByIdNoCast(R.id.category_ExpandableListView);

        expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView expandableListView, View view, int i, int i1, long l) {
                Intent intent = new Intent(getActivity(), CategoryActivity.class);
                RequestParams requestParams = new RequestParams();
                requestParams.type = RequestParams.TYPE_01;
                requestParams.conditions = ((Category.Secondary) expandableAdapter.getChild(i, i1)).getSortname();
                intent.putExtra("params", requestParams);
                startActivity(intent);
                return true;
            }
        });
    }

    @Override
    protected void initData() {
        expandableAdapter = new ExpandableAdapter(mActivity);
        expandableListView.setAdapter(expandableAdapter);

        getDataFromServer();
    }
    public void getDataFromServer(){
        RequestParams requestParams = new RequestParams();
        requestParams.type = RequestParams.TYPE_01;
        HttpHelper.requestCategory(HttpUtil.Method.POST, requestParams , new HttpUtil.CallBack() {
            @Override
            public void onSuccess(String result) {
                List<Category> categoryList = Arrays.asList(new Gson().fromJson(result,  Category[].class));
                expandableAdapter.categoryList = categoryList;
                expandableAdapter.notifyDataSetInvalidated();
            }

            @Override
            public void onFailed(String error) {
                Toast.makeText(mActivity,error, Toast.LENGTH_SHORT ).show();
            }
        });
    }
    @Override
    public void onClick(View view) {

    }
}
