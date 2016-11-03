package com.bwf.yibao.Yibao.activities.MainFragments;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.bwf.yibao.MyApplication;
import com.bwf.yibao.R;
import com.bwf.yibao.Yibao.activities.BaseFragment;
import com.bwf.yibao.Yibao.activities.SelectPictureActivity;
import com.bwf.yibao.Yibao.adapters.BaseAdapterHelper;
import com.bwf.yibao.Yibao.entities.Category;
import com.bwf.yibao.Yibao.entities.Goods;
import com.bwf.yibao.Yibao.entities.RequestParams;
import com.bwf.yibao.framwork.http.HttpHelper;
import com.bwf.yibao.framwork.http.HttpUtil;
import com.bwf.yibao.framwork.tools.UtilsURLPath;
import com.bwf.yibao.framwork.tools.ZcImageUploader;
import com.bwf.yibao.framwork.utils.Constants;
import com.bwf.yibao.framwork.utils.LogUtils;
import com.bwf.yibao.framwork.utils.ToastUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class PublishFragment extends BaseFragment {
    public static final int CATEGORY_WINDOW = 0;
    public static final int PHOTO_WINDOW = 1;
    public static final String IMAGE_DIR = "yb";
    private ImageView iv_camera;
    private PopupWindow popupWindow;

    View popupViewPhoto, popupViewCategory;
    //absListView
    ListView listView_Category;
    GridView gridView_Secondary;
    //adapter
    BaseAdapterHelper<Category> listAdatper;
    BaseAdapterHelper<Category.Secondary> gridAdatper;
    List<Category> categoryList = new ArrayList<>();

    ArrayList<String> selectedPics;

    private EditText edt_keyword, edt_category,edt_detail,edt_price, edt_originalPrice,
            edt_secondary, edt_phone, edt_qq, edt_location ;
    private Button btn_cancel, btn_push;

    private File imagePath;

    @Override
    protected int getResource() {
        return R.layout.fragment_publish;
    }

    @Override
    protected void beforeInitView() {

    }

    @Override
    protected void initView(View rootView) {
        iv_camera = findViewByIdNoCast(R.id.iv_camera);
        edt_keyword = findViewByIdNoCast(R.id.edt_keyword);
        edt_category = findViewByIdNoCast(R.id.edt_category);
        edt_detail = findViewByIdNoCast(R.id.edt_detail);
        edt_price = findViewByIdNoCast(R.id.edt_price);
        edt_originalPrice = findViewByIdNoCast(R.id.edt_originalPrice);
        edt_secondary = findViewByIdNoCast(R.id.edt_secondary);
        edt_phone = findViewByIdNoCast(R.id.edt_phone);
        edt_location = findViewByIdNoCast(R.id.edt_location);
        edt_qq = findViewByIdNoCast(R.id.edt_qq);
        btn_cancel = findViewByIdNoCast(R.id.btn_cancel);
        btn_push = findViewByIdNoCast(R.id.btn_push);

        popupViewPhoto = LayoutInflater.from(getActivity()).inflate(R.layout.popupwindow_photo, null);
        popupViewCategory =  LayoutInflater.from(getActivity()).inflate(R.layout.popup_category, null);
        listView_Category = (ListView) popupViewCategory.findViewById(R.id.listView_category);
        gridView_Secondary = (GridView) popupViewCategory.findViewById(R.id.gridView_secondary);
        //为absListView 设置 ItemClickListener
        listView_Category.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View item, int position, long id) {
                Category category = (Category) adapterView.getItemAtPosition(position);
                gridAdatper.setModels(category.getSortval());
                gridAdatper.notifyDataSetInvalidated();
                //点击后默认选中二级列表的第一个
                edt_category.setText(category.getSortkey());
                edt_secondary.setText(category.getSortval().get(0).getSortname());
            }
        });
        gridView_Secondary.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View item, int position, long id) {
                Category.Secondary secondary = (Category.Secondary) adapterView.getItemAtPosition(position);
                edt_secondary.setText(secondary.getSortname());
                //关闭弹窗
                closePopupWindow();
            }
        });

        View actionBarBack = popupViewCategory.findViewById(R.id.action_bar_back);
        View emptyView = popupViewCategory.findViewById(R.id.empty_view);
        //为fragment内控件设置监听
        setOnClick(iv_camera,edt_category, edt_secondary,actionBarBack, emptyView, btn_cancel, btn_push);
    }
    @Override
    protected void initData() {
        initPopupWindow();
        getData();
        selectedPics = new ArrayList<>();
    }

    private void initPopupWindow() {
        Button btn_camera = (Button) popupViewPhoto.findViewById(R.id.btn_camera_pop);
        Button btn_gallery = (Button) popupViewPhoto.findViewById(R.id.btn_gallery);
        Button btn_cancel = (Button) popupViewPhoto.findViewById(R.id.popup_btn_cancel);
        setOnclick(btn_camera,btn_gallery, btn_cancel );
        popupWindow= new PopupWindow(getActivity());
        popupWindow.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#00303F9F")));
        popupWindow.setOutsideTouchable(true);
        popupWindow.setFocusable(true);
    }

    BaseAdapterHelper<Category> categoryAdapter;
    BaseAdapterHelper<Category.Secondary> secondaryAdapter;
    private void getData() {
        HttpHelper.requestCategory(HttpUtil.Method.GET, new RequestParams("1", ""), new HttpUtil.CallBack() {
            @Override
            public void onSuccess(String result) {
                categoryList = new Gson().fromJson(result,new TypeToken<List<Category>>(){}.getType() );
                categoryAdapter = new BaseAdapterHelper<Category>(getActivity(),categoryList,R.layout.list_category_group) {
                    @Override
                    protected void convertView(ViewHolder viewHolder, Category category) {
                        viewHolder.setText(R.id.categoryName_tv, category.getSortkey());
                    }
                };
                listView_Category.setAdapter(categoryAdapter);
                gridAdatper = new BaseAdapterHelper<Category.Secondary>(getActivity(),new ArrayList<Category.Secondary>(), R.layout.grid_category_child_ ) {
                    @Override
                    protected void convertView(ViewHolder viewHolder, Category.Secondary secondary) {
                        viewHolder.setText(R.id.name_tv, secondary.getSortname());
                    }
                };
                gridView_Secondary.setAdapter(gridAdatper);
            }
            @Override
            public void onFailed(String error) {

            }
        });
    }
    private int[] getScreenSize() {
        int[] screenSize = new int[2];
        DisplayMetrics dm = getResources().getDisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
        screenSize[0] = dm.widthPixels;
        screenSize[1] = dm.heightPixels;
        return screenSize;
    }


    private void setOnclick(View...views){
        for(View view: views){
            view.setOnClickListener(popupClickListener);
        }
    }
    int sel = -1;
    View.OnClickListener popupClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.btn_camera_pop:
                    sel= Constants.TAKE_PHOTO;
                    break;
                case R.id.btn_gallery:
                    sel = Constants.GET_PICTURE_FORM_GALLERY;
                    break;
                case R.id.popup_btn_cancel:
                    break;
            }
            closePopupWindow();
        }
    };
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.iv_camera:
               showPopupWindow(PHOTO_WINDOW);
                break;
            case R.id.edt_category:
            case R.id.edt_secondary:
                showPopupWindow(CATEGORY_WINDOW);
                break;
            case R.id.btn_cancel:
                clearData();
                break;
            case R.id.btn_push:
                final Goods goods =  createModel();
                RequestParams params = new RequestParams();
                params.goods = goods;
                HttpHelper.publishGoods(params, new HttpUtil.CallBack() {
                    @Override
                    public void onSuccess(String result) {
                        ToastUtil.showToast("上传成功");
                        for(String imagePath: selectedPics){
                            ZcImageUploader.getInstance().uploadImage(UtilsURLPath.uploadPic,imagePath,goods.getImageId() + "",imagePath.substring(imagePath.lastIndexOf("/") + 1));
                        }
                    }

                    @Override
                    public void onFailed(String error) {

                    }
                });
                break;
            case R.id.action_bar_back:
                closePopupWindow();
                break;
            case R.id.empty_view:
                getData();
                break;
        }
    }
    private Goods createModel(){
        String calssify = edt_secondary.getText().toString();
        String gName = MyApplication.getApplication().getUser().getUserName();
        long imageId = new Date().getTime();
        int goodsId = (int) imageId;
        String goodsName = edt_keyword.getText().toString();
        String goodsInfo = edt_detail.getText().toString();
        float price = Float.parseFloat(edt_price.getText().toString());
        float originalprice = Float.parseFloat(edt_originalPrice.getText().toString());
        String qq = edt_qq.getText().toString();
        String time = new SimpleDateFormat("yyyy-mm-hh").format(imageId);
        String phone = edt_phone.getText().toString();
        int state = 0;
        Goods goods =  new Goods(calssify, gName, imageId,goodsId, goodsName,
                goodsInfo, null, originalprice,
        phone,price,qq,state,time);
        return goods;
    }
    private void clearData() {
        selectedPics.clear();
        edt_keyword.setText("");
        edt_detail.setText("");
        edt_price.setText("");
        edt_originalPrice.setText("");
        edt_category.setText("");
        edt_secondary.setText("");
        edt_phone.setText("");
        edt_qq.setText("");
    }

    private File createFileDir(String path){
        if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
            File fileDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),path);
            if(!fileDir.exists())
                fileDir.mkdirs();
            return fileDir;
        }
        return null;
    }
    private void showPopupWindow(int type){
        if(type == CATEGORY_WINDOW){
            popupWindow.setWidth(getScreenSize()[0]);
            popupWindow.setHeight(getScreenSize()[1]);
            popupWindow.setContentView(popupViewCategory);
            popupWindow.setAnimationStyle(R.style.anim_sliding_from_right);
            popupWindow.showAtLocation(rootView,Gravity.RIGHT, 0, 0);
            popupWindow.setOnDismissListener(null);
        }else{
            popupWindow.setContentView(popupViewPhoto);
            popupWindow.setWidth((int) (getScreenSize()[0]*0.8));
            popupWindow.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
            popupWindow.setAnimationStyle(R.style.anim_sliding_from_bottom);
            popupWindow.showAtLocation(rootView,Gravity.CENTER, 0, 0);
            popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
                @Override
                public void onDismiss() {
                   switch (sel){
                       case -1:
                           break;
                       case Constants.TAKE_PHOTO:
                           Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE_SECURE);
                           imagePath = new File(createFileDir(IMAGE_DIR), new Date().getTime() + ".jpg");
                           Uri uri = Uri.fromFile(imagePath);
                           intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
                           startActivityForResult(intent,Constants.TAKE_PHOTO);
                           break;
                       case Constants.GET_PICTURE_FORM_GALLERY:
                           intent = new Intent(getActivity(), SelectPictureActivity.class);
                           intent.putStringArrayListExtra("selected",selectedPics);
                           startActivityForResult(intent, Constants.GET_PICTURE_FORM_GALLERY);
                           break;
                   }
                    sel = -1;
                }
            });
        }
    }
    private void closePopupWindow(){
        if(popupWindow != null)
            popupWindow.dismiss();
    }
    View.OnClickListener popupListener2 = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.action_bar_back:
                    closePopupWindow();
                    break;
                case R.id.empty_view:
                    getData();
                    break;
            }
        }
    };
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == Constants.GET_PICTURE_FORM_GALLERY && resultCode == Activity.RESULT_OK){
            selectedPics = data.getStringArrayListExtra("result");
            LogUtils.i("zc", selectedPics.size() + "....");
        }else if(requestCode == Constants.TAKE_PHOTO && resultCode == Activity.RESULT_OK){
            if(imagePath.exists())
                selectedPics.add(imagePath.getAbsolutePath());
            else
                ToastUtil.showToast("添加失败");
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
