package com.bwf.yibao.Yibao.activities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.PersistableBundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.bwf.yibao.MyApplication;
import com.bwf.yibao.R;
import com.bwf.yibao.Yibao.activities.MainFragments.CategoryFragment;
import com.bwf.yibao.Yibao.activities.MainFragments.HomeFragment;
import com.bwf.yibao.Yibao.activities.MainFragments.MenuFragment;
import com.bwf.yibao.Yibao.activities.MainFragments.ProfileFragment;
import com.bwf.yibao.Yibao.activities.MainFragments.PublishFragment;
import com.bwf.yibao.Yibao.entities.User;
import com.bwf.yibao.framwork.utils.Constants;
import com.uuzuche.lib_zxing.DisplayUtil;
import com.uuzuche.lib_zxing.activity.CaptureActivity;
import com.uuzuche.lib_zxing.activity.CodeUtils;

public class MainActivity extends BaseActivity implements MyApplication.OnLoginListener {
    FragmentManager fm;
    FragmentTransaction ft;


    //创建fragment
    MenuFragment menuFragment;
    //content
    Fragment fragments[];
    /*HomeFragment homeFragment;
    ProfileFragment profileFragment;
    PublishFragment publishFragment;
    CategoryFragment categoryFragment;*/

    //记录当前显示的内容（fragment）需要在activity重建之前保存其状态
    private int selectedPos;

    private DrawerLayout drawerLayout;
    ImageButton mLeft;
    TextView center;
    ImageButton mRight;
    //用于连续点击两次退出应用
    boolean isBack = false;
    public static final int BACK = 1111;

    public static final int REQUEST_CODE_PROFILE = 0x02;
    public static final int REQUEST_CODE_PUBLISH = 0x03;
    public static final int REQUEST_CODE_LOGIN = 0x04;
    private static final int REQUEST_CODE_ZXING_DEFAULT = 0x05;
    private static final int REQUEST_CODE_ZXING_GALLERY = 0x06;
    private static final int REQUEST_CODE_ZXING_CUSTOM = 0x07;

    @Override
    public int getContentViewId() {
        return R.layout.activity_main;
    }

    @Override
    public void beforeInitView(Bundle savedInstanceState) {
        fragments = new Fragment[4];
        fm = getSupportFragmentManager();
        /*
         * fm.findFragmentById(int id); 参数id可以是fragment的ID，此时返回对应的fragment或者null ，
         * 也可以是装载fragment的容器的ID，此时返回容器中添加的最后一个fragment或者null。
         *  当activity 重建时， 系统会保存fragmentManager的状态， 故启动activity时可通过fragmentManager判断activity是否是重建
         *  并可从其中获取相应fragment。当然也可以通过Bundle判断activity是否是重建
         *
         */
        if (savedInstanceState == null) {
            menuFragment = new MenuFragment();
            fragments[0] = new HomeFragment();
            ft = fm.beginTransaction();
            ft.add(R.id.fl_content, fragments[0], "homeFragment").add(R.id.fl_menu, menuFragment, "menuFragment");
            ft.commit();
            selectedPos = 0;
        } else {
            menuFragment = (MenuFragment) fm.findFragmentByTag("menuFragment");
            fragments[0] = fm.findFragmentByTag("homeFragment");
            fragments[1] = fm.findFragmentByTag("profileFragment");
            fragments[2] = fm.findFragmentByTag("publishFragment");
            fragments[3] = fm.findFragmentByTag("moreFragment");
            selectedPos = savedInstanceState.getInt("selectedPos", 0);
        }
    }

    View popupViewZxing;

    @Override
    public void initView() {
        drawerLayout = findViewByIdNoCast(R.id.drawerLayout);
        mLeft = findViewByIdNoCast(R.id.mLeft);
        center = findViewByIdNoCast(R.id.mCenter);
        mRight = findViewByIdNoCast(R.id.mRight);
        mRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initPopupWindow();
            }
        });


        mLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.openDrawer(Gravity.LEFT);
            }
        });
        MyApplication.getApplication().registerOnLoginListener(this);
    }

    @Override
    public void initData() {

        menuFragment.setOnCheckedChangeListener(new MenuFragment.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(int position) {
                switch (position) {
                    case 0:
                        fm.beginTransaction().hide(fragments[selectedPos]).show(fragments[0]).commit();
                        selectedPos = 0;
                        drawerLayout.closeDrawer(Gravity.LEFT);
                        break;
                    case 1:
                        if (MyApplication.getApplication().getUser() == null) {//未登录
                            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                            startActivityForResult(intent, REQUEST_CODE_PROFILE);
                        } else {
                            ft = fm.beginTransaction();
                            ft.hide(fragments[selectedPos]);
                            if (fragments[1] == null) {
                                fragments[1] = new ProfileFragment();
                                ft.add(R.id.fl_content, fragments[1], "profileFragment").commit();
                            } else {
                                ft.show(fragments[1]).commit();
                            }
                            selectedPos = 1;
                            drawerLayout.closeDrawer(Gravity.LEFT);
                        }
                        break;
                    case 2:
                        if (MyApplication.getApplication().getUser() == null) {//未登录
                            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                            startActivityForResult(intent, REQUEST_CODE_PUBLISH);
                        } else {
                            ft = fm.beginTransaction();
                            ft.hide(fragments[selectedPos]);
                            if (fragments[2] == null) {
                                fragments[2] = new PublishFragment();
                                ft.add(R.id.fl_content, fragments[2], "publishFragment").commit();
                            } else {
                                ft.show(fragments[2]).commit();
                            }
                            selectedPos = 2;
                            drawerLayout.closeDrawer(Gravity.LEFT);
                        }
                        break;
                    case 3:
                        ft = fm.beginTransaction();
                        ft.hide(fragments[selectedPos]);
                        if (fragments[3] == null) {
                            fragments[3] = new CategoryFragment();
                            ft.add(R.id.fl_content, fragments[3], "categoryFragment").commit();
                        } else {
                            ft.show(fragments[3]).commit();
                        }
                        selectedPos = 3;
                        drawerLayout.closeDrawer(Gravity.LEFT);
                        break;
                }

            }
        });
        menuFragment.setOnLoginBtnClick(new MenuFragment.OnLoginBtnClick() {
            @Override
            public void onLogin() {
                //关闭菜单
                drawerLayout.closeDrawer(Gravity.LEFT);
            }
        });
    }

    int sel = -1;

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_camera_pop:
                sel = Constants.TAKE_PHOTO;
                break;
            case R.id.btn_gallery:
                sel = Constants.GET_PICTURE_FORM_GALLERY;
                break;
            case R.id.popup_btn_cancel:
                sel = Constants.ZXING;
                break;
        }
        closePopupWindow();
    }

    private void closePopupWindow() {
        if (popupWindow != null)
            popupWindow.dismiss();
    }

    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case BACK:
                    isBack = false;
                    break;
            }
        }
    };
    PopupWindow popupWindow;

    private void initPopupWindow() {
        popupViewZxing = LayoutInflater.from(MainActivity.this).inflate(R.layout.popupwindow_photo, null);
        Button btn_camera = (Button) popupViewZxing.findViewById(R.id.btn_camera_pop);
        btn_camera.setText("默认布局");
        Button btn_gallery = (Button) popupViewZxing.findViewById(R.id.btn_gallery);
        btn_gallery.setText("相册选取");
        Button btn_cancel = (Button) popupViewZxing.findViewById(R.id.popup_btn_cancel);
        btn_cancel.setText("自定义布局");
        setOnClick(btn_camera, btn_gallery, btn_cancel);
        popupWindow = new PopupWindow(this);
        popupWindow.setContentView(popupViewZxing);

        popupWindow.setWidth((int) (DisplayUtil.screenWidthPx * 0.8));
        popupWindow.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);

        popupWindow.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#00000000")));
        popupWindow.setOutsideTouchable(true);
        popupWindow.setFocusable(true);
        popupWindow.setAnimationStyle(R.style.anim_sliding_from_bottom);
        popupWindow.showAtLocation(drawerLayout, Gravity.CENTER, 0, 0);

        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                switch (sel) {
                    case Constants.TAKE_PHOTO:
                        Intent intent = new Intent(MainActivity.this, CaptureActivity.class);
                        startActivityForResult(intent, REQUEST_CODE_ZXING_DEFAULT);
                        break;
                    case Constants.GET_PICTURE_FORM_GALLERY:
                        intent = new Intent(Intent.ACTION_GET_CONTENT);
                        intent.addCategory(Intent.CATEGORY_OPENABLE);
                        intent.setType("image*//*");
                        startActivityForResult(intent, REQUEST_CODE_ZXING_GALLERY);
                        break;
                    case Constants.ZXING:
                        intent = new Intent(MainActivity.this, MyCaptureActivity.class);
                        startActivityForResult(intent, REQUEST_CODE_ZXING_CUSTOM);
                        break;
                }
                sel = -1;
            }
        });

    }


    @Override
    public void onBackPressed() {
        //如果侧滑菜单打开，则关闭侧滑，不退出应用
        if (drawerLayout.isDrawerOpen(Gravity.LEFT)) {
            drawerLayout.closeDrawer(Gravity.LEFT);
            return;
        }
        if (!isBack) {
            isBack = true;
            Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
            mHandler.sendEmptyMessageDelayed(BACK, 2000);
            return;
        }
        mHandler.removeMessages(BACK);
        super.onBackPressed();
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        outState.putInt("selectedPos", selectedPos);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_CODE_PROFILE://登录成功，显示发布页面
                if (resultCode == Activity.RESULT_OK) {
                    ft = fm.beginTransaction();
                    ft.hide(fragments[selectedPos]);//隐藏登录之前的页面
                    if (fragments[1] == null) {
                        fragments[1] = new ProfileFragment();
                        ft.add(R.id.fl_content, fragments[1], "profileFragment");
                    } else {
                        ft.show(fragments[1]);
                    }
                    ft.commit();
                    selectedPos = 1;
                    //关闭菜单
                    drawerLayout.closeDrawer(Gravity.LEFT);
                }
                break;
            case REQUEST_CODE_PUBLISH://登录成功，显示发布页面
                if (resultCode == Activity.RESULT_OK) {
                    ft = fm.beginTransaction();
                    ft.hide(fragments[selectedPos]);//隐藏登录之前的页面
                    if (fragments[2] == null) {
                        fragments[2] = new PublishFragment();
                        ft.add(R.id.fl_content, fragments[2], "publishFragment");
                    } else {
                        ft.show(fragments[2]);
                    }
                    ft.commit();
                    selectedPos = 2;
                    //关闭菜单
                    drawerLayout.closeDrawer(Gravity.LEFT);
                }
                break;
            case REQUEST_CODE_ZXING_DEFAULT:
                //处理扫描结果（在界面上显示）
                if (null != data) {
                    Bundle bundle = data.getExtras();
                    if (bundle == null) {
                        return;
                    }
                    if (bundle.getInt(CodeUtils.RESULT_TYPE) == CodeUtils.RESULT_SUCCESS) {
                        String result = bundle.getString(CodeUtils.RESULT_STRING);
                        Toast.makeText(this, "解析结果:" + result, Toast.LENGTH_LONG).show();
                    } else if (bundle.getInt(CodeUtils.RESULT_TYPE) == CodeUtils.RESULT_FAILED) {
                        Toast.makeText(MainActivity.this, "解析二维码失败", Toast.LENGTH_LONG).show();
                    }
                }
                break;
            case REQUEST_CODE_ZXING_GALLERY:
                if (data != null) {
                    Uri uri = data.getData();
                    try {
                        CodeUtils.analyzeBitmap(uri.getPath(), new CodeUtils.AnalyzeCallback() {
                            @Override
                            public void onAnalyzeSuccess(Bitmap mBitmap, String result) {
                                Toast.makeText(MainActivity.this, "解析结果:" + result, Toast.LENGTH_LONG).show();
                            }

                            @Override
                            public void onAnalyzeFailed() {
                                Toast.makeText(MainActivity.this, "解析二维码失败", Toast.LENGTH_LONG).show();
                            }
                        });
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                break;
            case REQUEST_CODE_ZXING_CUSTOM:
//处理扫描结果（在界面上显示）
                if (null != data) {
                    Bundle bundle = data.getExtras();
                    if (bundle == null) {
                        return;
                    }
                    if (bundle.getInt(CodeUtils.RESULT_TYPE) == CodeUtils.RESULT_SUCCESS) {
                        String result = bundle.getString(CodeUtils.RESULT_STRING);
                        Toast.makeText(this, "解析结果:" + result, Toast.LENGTH_LONG).show();
                    } else if (bundle.getInt(CodeUtils.RESULT_TYPE) == CodeUtils.RESULT_FAILED) {
                        Toast.makeText(MainActivity.this, "解析二维码失败", Toast.LENGTH_LONG).show();
                    }
                }
                break;
        }

    }

    @Override
    protected void onDestroy() {
        MyApplication.getApplication().unRegisterOnLoginListener(this);
        super.onDestroy();
    }

    @Override
    public void onLogin(User user) {
        if (user == null) {
            fm.beginTransaction().hide(fragments[selectedPos]).show(fragments[0]).commit();
            selectedPos = 0;
        }
    }
}
