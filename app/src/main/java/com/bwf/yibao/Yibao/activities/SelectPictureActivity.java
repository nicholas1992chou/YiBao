package com.bwf.yibao.Yibao.activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bwf.yibao.R;
import com.bwf.yibao.Yibao.adapters.BaseAdapterHelper;
import com.bwf.yibao.Yibao.entities.FileBean;
import com.bwf.yibao.framwork.tools.ZcImageLoader;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by nicholas on 2016/9/20.
 */
public class SelectPictureActivity extends BaseActivity {
    GridView gridview_picselector;
    ListView pictureDirListView;

    BaseAdapterHelper<String> gridViewAdapter;
    BaseAdapterHelper<FileBean> listAdapter;
    //选中的图片路径集合
    List<String> selectedPics;

    PopupWindow picDirWindow;
    RelativeLayout bottom;
    TextView picDir, picCount, action_bar_title;

    ImageButton actionBarBack;
    TextView actionBarDone;
    TextView tv_overView;

    boolean isPopupshown = false;

    private int totalCount;


    @Override
    public int getContentViewId() {
        return R.layout.activity_selectpicture;
    }

    @Override
    public void beforeInitView(Bundle savedInstanceState) {
        selectedPics = getIntent().getStringArrayListExtra("selected");
    }
    public static void startActivityForResult(Activity activity, ArrayList<String> value){
        Intent intent = new Intent(activity,SelectPictureActivity.class);
        intent.putStringArrayListExtra("selected", value);
        activity.startActivityForResult(intent, 1);
    }


    @Override
    public void initView() {
        bottom = findViewByIdNoCast(R.id.rl_bottom);
        picDir = findViewByIdNoCast(R.id.tv_dir);
        action_bar_title = findViewByIdNoCast(R.id.action_bar_title);
        actionBarBack = findViewByIdNoCast(R.id.action_bar_back);
        actionBarDone = findViewByIdNoCast(R.id.action_bar_more);


        picCount = findViewByIdNoCast(R.id.tv_pic_count);
        gridview_picselector = findViewByIdNoCast(R.id.gridview_picselector);
        tv_overView = findViewByIdNoCast(R.id.tv_overview);
        initPopupWindow();

        setOnClick(picDir, actionBarBack, actionBarDone, tv_overView);
    }

    ProgressDialog progressDialog;

    @Override
    public void initData() {
        listAdapter = new BaseAdapterHelper<FileBean>(this, new ArrayList<FileBean>(), R.layout.list_picture_dir) {
            @Override
            protected void convertView(ViewHolder viewHolder, FileBean fileBean) {
                viewHolder.setText(R.id.tv_dir, fileBean.fileName);
                viewHolder.setText(R.id.tv_pic_count, fileBean.picPathList.size() + "张");
                viewHolder.setImageResource(R.id.iv_pic, R.drawable.shape_black);
                ZcImageLoader.getInstance(3, ZcImageLoader.Type.LIFO).loadImage(fileBean.picPathList.get(0), (ImageView) viewHolder.findViewById(R.id.iv_pic));

            }
        };
        pictureDirListView.setAdapter(listAdapter);
        gridViewAdapter = new BaseAdapterHelper<String>(this, new ArrayList<String>(), R.layout.grid_picture_100dp) {
            @Override
            protected void convertView(ViewHolder viewHolder, String s) {
                viewHolder.setImageResource(R.id.iv_pic, R.drawable.shape_black);
                ZcImageLoader.getInstance(3, ZcImageLoader.Type.LIFO).loadImage(s, (ImageView) viewHolder.findViewById(R.id.iv_pic));
                CheckBox checkBox = viewHolder.findViewById(R.id.checkbox);
                //恢复checkBox 的状态
                checkBox.setOnCheckedChangeListener(null);
                if(selectedPics.contains(s))
                    checkBox.setChecked(true);
                else
                    checkBox.setChecked(false);
                //设置监听
                final String path = s;
                checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                        if(isChecked)
                            selectedPics.add(path);
                         else
                            selectedPics.remove(path);
                        if(selectedPics.isEmpty()){
                            actionBarDone.setText("完成");
                            tv_overView.setText("预览");
                        }
                        else{
                            actionBarDone.setText("完成("+selectedPics.size()+"/"+totalCount+")");
                            tv_overView.setText("预览("+selectedPics.size()+")");
                        }

                    }
                });
            }
        };
        gridview_picselector.setAdapter(gridViewAdapter);
        new AsyncTask<Void, String, List<FileBean>>() {
            @Override
            protected void onPreExecute() {
                progressDialog = ProgressDialog.show(SelectPictureActivity.this, null, "正在扫描图片");
            }

            @Override
            protected List<FileBean> doInBackground(Void... voids) {
                List<FileBean> fileBeanList = null;
                Uri uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                ContentResolver contentResolver = SelectPictureActivity.this.getContentResolver();
                Cursor cursor = contentResolver.query(uri, null, MediaStore.Images.Media.MIME_TYPE + " =? or " + MediaStore.Images.Media.MIME_TYPE + "=?", new String[]{"image/jpeg", "image/png"}, MediaStore.Images.Media.DATE_MODIFIED);

                HashMap<File,FileBean> filePathMap = new HashMap<File,FileBean>();
                while (cursor.moveToNext()) {
                    //获取所有图片的路径
                    String path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
                   //获取该图片所在目录
                    File parentFile = new File(path).getParentFile();
                    List<String> picPathList = null;
                    String filePath = null;
                    String fileName = null;
                    if (parentFile == null)//规避特殊情况
                        continue;
                    FileBean fileBean = filePathMap.get(parentFile);
                    if (fileBean == null) {//利用hashset去重（避免图片在同一目录，多次扫描该目录）
                        filePath = parentFile.getAbsolutePath();
                        fileName = filePath.substring(filePath.lastIndexOf("/"));
                        List<String> pics = new ArrayList<String>();
                        pics.add(path);
                        fileBean = new FileBean(filePath, fileName,pics);
                    }else{
                        fileBean.picPathList.add(path);
                    }
                    totalCount++;
                    filePathMap.put(parentFile,fileBean);
                }
                fileBeanList = new ArrayList<FileBean>(filePathMap.values());
                cursor.close();
                return fileBeanList;
            }

            @Override
            protected void onPostExecute(List<FileBean> fileBeenList) {
                progressDialog.dismiss();
                listAdapter.setModels(fileBeenList);
                if(!fileBeenList.isEmpty()){
                    picDir.setText(fileBeenList.get(0).fileName.substring(1));
                    gridViewAdapter.setModels(fileBeenList.get(0).picPathList);
                }
                else
                    picDir.setText("未扫描到图片");
                if(selectedPics.isEmpty()){
                    actionBarDone.setText("完成");
                    tv_overView.setText("预览");
                }
                else{
                    actionBarDone.setText("完成("+selectedPics.size()+"/"+totalCount+")");
                    tv_overView.setText("预览("+selectedPics.size()+")");
                }
                listAdapter.notifyDataSetInvalidated();
                gridViewAdapter.notifyDataSetInvalidated();
                super.onPostExecute(fileBeenList);
            }
        }.execute();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.tv_dir:
                showPopupWindow();
                break;
            case R.id.action_bar_back:
                finish();
                break;
            case R.id.tv_overview:

                break;
            case R.id.action_bar_more:
                Intent intent = getIntent();
                intent.putStringArrayListExtra("result", (ArrayList<String>) selectedPics);
                setResult(RESULT_OK, intent);
                finish();
                break;
        }

    }

    void initPopupWindow() {
        View contentView = LayoutInflater.from(this).inflate(R.layout.popupwindow_picture_dir, null);
        picDirWindow = new PopupWindow(this);
        picDirWindow.setContentView(contentView);
        picDirWindow.setOutsideTouchable(true);
        picDirWindow.setBackgroundDrawable(new BitmapDrawable());
        picDirWindow.setFocusable(true);
        picDirWindow.setAnimationStyle(R.style.anim_sliding_from_bottom);

        pictureDirListView = (ListView) contentView.findViewById(R.id.lv_pic_dir);
        pictureDirListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                FileBean fileBean = (FileBean) adapterView.getItemAtPosition(i);
                picDir.setText(fileBean.fileName.substring(1));
                gridViewAdapter.setModels(fileBean.picPathList);
                gridViewAdapter.notifyDataSetInvalidated();
                //关闭弹窗
                closePopupWindow();

            }
        });
    }

//关闭弹窗
     private void closePopupWindow() {
         if(picDirWindow != null)
             picDirWindow.dismiss();//该对象依然存在
     }
    private void showPopupWindow(){

        picDirWindow.setWidth(gridview_picselector.getWidth());
        picDirWindow.setHeight(gridview_picselector.getHeight());
        if(picDirWindow!= null && !picDirWindow.isShowing())
            picDirWindow.showAtLocation(gridview_picselector, Gravity.BOTTOM, 0,bottom.getHeight());
    }
}
