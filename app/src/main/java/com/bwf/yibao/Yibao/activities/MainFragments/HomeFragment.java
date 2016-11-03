package com.bwf.yibao.Yibao.activities.MainFragments;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;

import com.ToxicBakery.viewpager.transforms.AccordionTransformer;
import com.ToxicBakery.viewpager.transforms.BackgroundToForegroundTransformer;
import com.ToxicBakery.viewpager.transforms.CubeInTransformer;
import com.ToxicBakery.viewpager.transforms.CubeOutTransformer;
import com.ToxicBakery.viewpager.transforms.DepthPageTransformer;
import com.ToxicBakery.viewpager.transforms.FlipHorizontalTransformer;
import com.ToxicBakery.viewpager.transforms.FlipVerticalTransformer;
import com.ToxicBakery.viewpager.transforms.RotateDownTransformer;
import com.ToxicBakery.viewpager.transforms.RotateUpTransformer;
import com.ToxicBakery.viewpager.transforms.ScaleInOutTransformer;
import com.ToxicBakery.viewpager.transforms.StackTransformer;
import com.ToxicBakery.viewpager.transforms.TabletTransformer;
import com.ToxicBakery.viewpager.transforms.ZoomInTransformer;
import com.ToxicBakery.viewpager.transforms.ZoomOutSlideTransformer;
import com.ToxicBakery.viewpager.transforms.ZoomOutTranformer;
import com.bigkoo.convenientbanner.ConvenientBanner;
import com.bigkoo.convenientbanner.holder.CBViewHolderCreator;
import com.bigkoo.convenientbanner.holder.Holder;
import com.bigkoo.convenientbanner.listener.OnItemClickListener;
import com.bwf.yibao.R;
import com.bwf.yibao.Yibao.activities.BaseFragment;
import com.bwf.yibao.Yibao.activities.CategoryActivity;
import com.bwf.yibao.Yibao.adapters.BaseAdapterHelper;
import com.bwf.yibao.Yibao.entities.RequestParams;
import com.bwf.yibao.framwork.utils.ToastUtil;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/**
 * 应用程序首页：包含一个viewPager轮播推荐产品， 一个gridView展示热点产品， 一个ListView展示常规产品
 * 布局：swipeRefreshLayout( scrollView (viewPager + gridView + listView))
 * 数据：从存储文件获取缓存数据， 显示默认布局， 从网络请求数据， 更新视图， 缓存数据。
 */
public class HomeFragment extends BaseFragment {
    //    View
    GridView gridView;
    ListView listView;
    TextView tv_moregoods;

    SwipeRefreshLayout home_frag_swipeRefreshLayout;
    //    Adapter
    BaseAdapterHelper<String> gridAdapter;
    BaseAdapterHelper<String> listAdapter;
    List<String> bannerItems = new ArrayList<String>(Arrays.asList("http://img.my.csdn.net/uploads/201508/05/1438760724_9463.jpg",
            "http://img.my.csdn.net/uploads/201508/05/1438760724_2371.jpg",
            "http://img.my.csdn.net/uploads/201508/05/1438760707_4653.jpg",
            "http://img.my.csdn.net/uploads/201508/05/1438760706_6864.jpg"));
    List<String> gridItems = new ArrayList<String>(Arrays.asList("http://img.my.csdn.net/uploads/201508/05/1438760758_3497.jpg",
            "http://img.my.csdn.net/uploads/201508/05/1438760758_6667.jpg",
            "http://img.my.csdn.net/uploads/201508/05/1438760757_3588.jpg",
            "http://img.my.csdn.net/uploads/201508/05/1438760756_3304.jpg",
            "http://img.my.csdn.net/uploads/201508/05/1438760755_6715.jpeg",
            "http://img.my.csdn.net/uploads/201508/05/1438760726_5120.jpg",
            "http://img.my.csdn.net/uploads/201508/05/1438760726_8364.jpg",
            "http://img.my.csdn.net/uploads/201508/05/1438760725_4031.jpg"));
    List<String> listItems = new ArrayList<String>(Arrays.asList("http://img.my.csdn.net/uploads/201508/05/1438760706_9279.jpg",
            "http://img.my.csdn.net/uploads/201508/05/1438760704_2341.jpg",
            "http://img.my.csdn.net/uploads/201508/05/1438760704_5707.jpg",
            "http://img.my.csdn.net/uploads/201508/05/1438760685_5091.jpg",
            "http://img.my.csdn.net/uploads/201508/05/1438760685_4444.jpg",
            "http://img.my.csdn.net/uploads/201508/05/1438760684_8827.jpg",
            "http://img.my.csdn.net/uploads/201508/05/1438760683_3691.jpg",
            "http://img.my.csdn.net/uploads/201508/05/1438760683_7315.jpg",
            "http://img.my.csdn.net/uploads/201508/05/1438760663_7318.jpg",
            "http://img.my.csdn.net/uploads/201508/05/1438760662_3454.jpg",
            "http://img.my.csdn.net/uploads/201508/05/1438760662_5113.jpg",
            "http://img.my.csdn.net/uploads/201508/05/1438760661_3305.jpg",
            "http://img.my.csdn.net/uploads/201508/05/1438760661_7416.jpg",
            "http://img.my.csdn.net/uploads/201508/05/1438760589_2946.jpg",
            "http://img.my.csdn.net/uploads/201508/05/1438760589_1100.jpg",
            "http://img.my.csdn.net/uploads/201508/05/1438760588_8297.jpg",
            "http://img.my.csdn.net/uploads/201508/05/1438760587_2575.jpg",
            "http://img.my.csdn.net/uploads/201508/05/1438760587_8906.jpg",
            "http://img.my.csdn.net/uploads/201508/05/1438760550_2875.jpg",
            "http://img.my.csdn.net/uploads/201508/05/1438760550_9517.jpg",
            "http://img.my.csdn.net/uploads/201508/05/1438760549_7093.jpg",
            "http://img.my.csdn.net/uploads/201508/05/1438760549_1352.jpg",
            "http://img.my.csdn.net/uploads/201508/05/1438760548_2780.jpg",
            "http://img.my.csdn.net/uploads/201508/05/1438760531_1776.jpg",
            "http://img.my.csdn.net/uploads/201508/05/1438760531_1380.jpg",
            "http://img.my.csdn.net/uploads/201508/05/1438760530_4944.jpg",
            "http://img.my.csdn.net/uploads/201508/05/1438760530_5750.jpg",
            "http://img.my.csdn.net/uploads/201508/05/1438760529_3289.jpg",
            "http://img.my.csdn.net/uploads/201508/05/1438760500_7871.jpg",
            "http://img.my.csdn.net/uploads/201508/05/1438760500_6063.jpg",
            "http://img.my.csdn.net/uploads/201508/05/1438760499_6304.jpeg",
            "http://img.my.csdn.net/uploads/201508/05/1438760499_5081.jpg",
            "http://img.my.csdn.net/uploads/201508/05/1438760498_7007.jpg",
            "http://img.my.csdn.net/uploads/201508/05/1438760478_3128.jpg",
            "http://img.my.csdn.net/uploads/201508/05/1438760478_6766.jpg",
            "http://img.my.csdn.net/uploads/201508/05/1438760477_1358.jpg",
            "http://img.my.csdn.net/uploads/201508/05/1438760477_3540.jpg",
            "http://img.my.csdn.net/uploads/201508/05/1438760476_1240.jpg",
            "http://img.my.csdn.net/uploads/201508/05/1438760446_7993.jpg",
            "http://img.my.csdn.net/uploads/201508/05/1438760446_3641.jpg",
            "http://img.my.csdn.net/uploads/201508/05/1438760445_3283.jpg",
            "http://img.my.csdn.net/uploads/201508/05/1438760444_8623.jpg",
            "http://img.my.csdn.net/uploads/201508/05/1438760444_6822.jpg",
            "http://img.my.csdn.net/uploads/201508/05/1438760422_2224.jpg",
            "http://img.my.csdn.net/uploads/201508/05/1438760421_2824.jpg",
            "http://img.my.csdn.net/uploads/201508/05/1438760420_2660.jpg",
            "http://img.my.csdn.net/uploads/201508/05/1438760420_7188.jpg",
            "http://img.my.csdn.net/uploads/201508/05/1438760419_4123.jpg"));

    private ConvenientBanner convenientBanner;

    @Override
    protected int getResource() {
        return R.layout.home_fragment;
    }

    @Override
    protected void beforeInitView() {

    }

    @Override
    protected void initView(View rootView) {
        listView = (ListView) rootView.findViewById(R.id.frag_home_listview);
        View headerView = listView.inflate(getContext(), R.layout.list_head_view, null);
        gridView = (GridView) headerView.findViewById(R.id.home_feture_grid);
        gridAdapter = new BaseAdapterHelper<String>(getContext(), gridItems, R.layout.fragment_home_features_item) {
            @Override
            protected void convertView(ViewHolder viewHolder, String s) {
                viewHolder.setImageUrl(R.id.simple_drawee_view, s);
            }
        };
        gridView.setAdapter(gridAdapter);
        convenientBanner = (ConvenientBanner) headerView.findViewById(R.id.home_convenientbanner);
        convenientBanner.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                convenientBanner.setPageTransformer(transformers[++clickCount % transformers.length]);
            }
        })//
                .setPageIndicator(new int[]{R.drawable.pager_indicator_selected, R.drawable.pager_indicator_normal})//
                .setPageIndicatorAlign(ConvenientBanner.PageIndicatorAlign.ALIGN_PARENT_RIGHT)//
                .setManualPageable(true);
        convenientBanner.setPageTransformer(new CubeOutTransformer());

        convenientBanner.setPages(new CBViewHolderCreator<ImageViewHolder>() {
            @Override
            public ImageViewHolder createHolder() {
                return new ImageViewHolder();
            }
        }, bannerItems);
        convenientBanner.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {
                if (state == ViewPager.SCROLL_STATE_DRAGGING) {
                    home_frag_swipeRefreshLayout.setEnabled(false);
                } else
                    home_frag_swipeRefreshLayout.setEnabled(true);
            }
        });

        listView.addHeaderView(headerView);
        home_frag_swipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.home_frag_swipeRefreshLayout);
        home_frag_swipeRefreshLayout.setColorSchemeColors(Color.RED, Color.GREEN, Color.BLUE);


        home_frag_swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                home_frag_swipeRefreshLayout.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        home_frag_swipeRefreshLayout.setRefreshing(false);
                        ToastUtil.showToast("更新成功");
                    }
                }, 2000);
            }
        });
        tv_moregoods = (TextView) headerView.findViewById(R.id.tv_moregoods);
        setOnClick(tv_moregoods, convenientBanner);
    }


    /**
     * 初始化数据， 从缓存获取， 再调用网络请求，回调时更新视图
     */
    @Override
    protected void initData() {
        listAdapter = new BaseAdapterHelper<String>(getContext(), listItems, R.layout.fragment_home_list_item) {
            @Override
            protected void convertView(ViewHolder viewHolder, String s) {
                viewHolder.setImageUrl(R.id.simple_drawee_view, s);
            }
        };
        listView.setAdapter(listAdapter);

    }

    int clickCount = 0;
    ViewPager.PageTransformer[] transformers =
            {       new CubeOutTransformer(),
                    new BackgroundToForegroundTransformer(),
                    new TabletTransformer(),
                    new AccordionTransformer(),
                    new CubeInTransformer(),
                    new DepthPageTransformer(),
                    new FlipHorizontalTransformer(),
                    new FlipVerticalTransformer(),
                    new RotateDownTransformer(),
                    new RotateUpTransformer(),
                    new ScaleInOutTransformer(),
                    new StackTransformer(),
                    new ZoomOutSlideTransformer(),
                    new ZoomInTransformer(),
                    new ZoomOutTranformer()
            };

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_moregoods:
                Intent intent = new Intent(getActivity(), CategoryActivity.class);
                RequestParams requestParams = new RequestParams(RequestParams.TYPE_00, "1");
                intent.putExtra("params", requestParams);
                startActivity(intent);
                break;
            case R.id.home_convenientbanner:
                break;
            default:
                break;
        }


    }

    @Override
    public void onPause() {
        super.onPause();
        convenientBanner.stopTurning();
    }

    @Override
    public void onResume() {
        super.onResume();
        convenientBanner.startTurning(3000);
    }

    private static class ImageViewHolder implements Holder<String> {
        SimpleDraweeView simpleDraweeView;

        @Override
        public View createView(Context context) {
            simpleDraweeView = (SimpleDraweeView) LayoutInflater.from(context).inflate(R.layout.pages_simpledrawee, null, false);
            return simpleDraweeView;
        }

        @Override
        public void UpdateUI(Context context, int position, String data) {
            simpleDraweeView.setImageURI(data);
        }
    }

}
