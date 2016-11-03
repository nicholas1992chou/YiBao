package com.bwf.yibao.Yibao.activities.MainFragments;

import android.graphics.Paint;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.bwf.yibao.MyApplication;
import com.bwf.yibao.R;
import com.bwf.yibao.Yibao.activities.BaseFragment;
import com.bwf.yibao.Yibao.adapters.BaseAdapterHelper;
import com.bwf.yibao.Yibao.entities.Goods;
import com.bwf.yibao.Yibao.entities.RequestParams;
import com.bwf.yibao.Yibao.entities.User;
import com.bwf.yibao.Yibao.views.ViewpagerIndicator;
import com.bwf.yibao.framwork.http.HttpHelper;
import com.bwf.yibao.framwork.http.HttpUtil;
import com.bwf.yibao.framwork.tools.ImageLoader;
import com.bwf.yibao.framwork.tools.UtilsURLPath;
import com.bwf.yibao.framwork.utils.RandomUtil;
import com.bwf.yibao.framwork.utils.ToastUtil;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class ProfileFragment extends BaseFragment {
    TextView tv_tab_01, tv_tab_02,tv_tab_03, tv_empty_view;
    TextView selectedView;
    ListView listView_goodsList;
    ViewpagerIndicator viewpagerIndicator;

    ImageLoader imageLoader;
    BaseAdapterHelper<Goods> goodsListAdapter;
    RequestParams requestParams = new RequestParams();
    @Override
    protected int getResource() {
        return R.layout.fragment_profile;
    }

    @Override
    protected void beforeInitView() {

    }

    @Override
    protected void initView(View rootView) {
        tv_tab_01 = findViewByIdNoCast(R.id.tv_tab_01);
        tv_tab_02 = findViewByIdNoCast(R.id.tv_tab_02);
        tv_tab_03 = findViewByIdNoCast(R.id.tv_tab_03);
        tv_empty_view = findViewByIdNoCast(R.id.empty_view);
        tv_tab_01.setSelected(true);
        selectedView = tv_tab_01;
        listView_goodsList = findViewByIdNoCast(R.id.listView_goodsList);
        listView_goodsList.setEmptyView(tv_empty_view);
        setOnClick(tv_tab_01, tv_tab_02, tv_tab_03);
        viewpagerIndicator = findViewByIdNoCast(R.id.ll_container);

    }


    @Override
    protected void initData() {
        imageLoader = new ImageLoader(getActivity());
        goodsListAdapter = new BaseAdapterHelper<Goods>(getActivity(),new ArrayList<Goods>(), R.layout.item_goods) {
            @Override
            protected void convertView(ViewHolder viewHolder, Goods goods) {
                viewHolder.setImageResource(R.id.goodsPic_iv, R.mipmap.user2);
                if(!goods.getImagePath().isEmpty()){
                    String imageUrl = UtilsURLPath.downloadPic + goods.getImagePath().get(0);
                    imageLoader.loadImage((ImageView) viewHolder.findViewById(R.id.goodsPic_iv),imageUrl );
                }

                viewHolder.setText(R.id.goodsName_tv, goods.getGoodsName());
                viewHolder.setText(R.id.price_tv, "￥" + goods.getPrice());
                viewHolder.setText(R.id.originalPrice_tv, "￥" + goods.getOriginalprice() + "");
                ((TextView)viewHolder.findViewById(R.id.originalPrice_tv)).getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
                ((TextView)viewHolder.findViewById(R.id.originalPrice_tv)).getPaint().setAntiAlias(true);
                int vote = RandomUtil.getRandomInt(100000);
                goods.vote = vote;
                viewHolder.setText(R.id.time_tv, goods.getTime());
                viewHolder.setText(R.id.tv_command, goods.vote + "条评价");
                viewHolder.setText(R.id.tv_haoping, RandomUtil.getRandomInt(100) + "%好评");
            }
        };
        listView_goodsList.setAdapter(goodsListAdapter);
        //goodsListAdapter = new GoodsListAdapter(getActivity());
        //listView_goodsList.setAdapter(goodsListAdapter);
        queryCollection();
    }


    @Override
    public void onClick(View view) {
        goodsListAdapter.clear();
        goodsListAdapter.notifyDataSetChanged();
        switch (view.getId()){
            case R.id.tv_tab_01:
                selectedView.setSelected(false);
                selectedView = tv_tab_01;
                selectedView.setSelected(true);
                viewpagerIndicator.startSroll(0);
                //queryCollection();
                break;
            case R.id.tv_tab_02:
                selectedView.setSelected(false);
                selectedView = tv_tab_02;
                selectedView.setSelected(true);
                viewpagerIndicator.startSroll(1);
                //queryPublishedGoods();
                break;
            case R.id.tv_tab_03:
                selectedView.setSelected(false);
                selectedView = tv_tab_03;
                selectedView.setSelected(true);
                viewpagerIndicator.startSroll(2);
                //queryPublishedGoods();
                break;
        }
    }
    public void queryCollection(){
        User user = MyApplication.getApplication().getUser();
        if(user != null){
            requestParams.userName = user.getUserName();
                    HttpHelper.queryCollection(requestParams, new HttpUtil.CallBack() {
                        @Override
                        public void onSuccess(String result) {
                            List<Goods> goodsList = new ArrayList<Goods>(Arrays.asList(new Gson().fromJson(result, Goods[].class)));
                                goodsListAdapter.setModels(goodsList);
                            goodsListAdapter.notifyDataSetInvalidated();
                        }

                        @Override
                        public void onFailed(String error) {

                        }
                    });
        }else {
            ToastUtil.showToast("请先登录");
        }
    }
    public void queryPublishedGoods(){
        User user = MyApplication.getApplication().getUser();
        if(user != null){
            requestParams.type = RequestParams.TYPE_02;
            requestParams.conditions = user.getUserName();
                    HttpHelper.queryPublishedGoods(requestParams, new HttpUtil.CallBack() {
                        @Override
                        public void onSuccess(String result) {
                            List<Goods> goodsList = new ArrayList<Goods>(Arrays.asList(new Gson().fromJson(result, Goods[].class)));
                                goodsListAdapter.setModels(goodsList);
                            goodsListAdapter.notifyDataSetInvalidated();
                        }

                        @Override
                        public void onFailed(String error) {
                            ToastUtil.showToast(error);
                        }
                    });
        }
    }
}
