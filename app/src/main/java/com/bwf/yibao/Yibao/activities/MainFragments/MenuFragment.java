package com.bwf.yibao.Yibao.activities.MainFragments;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bwf.yibao.MyApplication;
import com.bwf.yibao.R;
import com.bwf.yibao.Yibao.activities.BaseFragment;
import com.bwf.yibao.Yibao.activities.LoginActivity;
import com.bwf.yibao.Yibao.activities.SelectPictureActivity;
import com.bwf.yibao.Yibao.entities.User;
import com.bwf.yibao.framwork.utils.IntentUtils;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.ArrayList;


public class MenuFragment extends BaseFragment implements MyApplication.OnLoginListener {

    //获取控件
    LinearLayout ll_menu_head_before_login;
    LinearLayout ll_menu_head_after_login;

    SimpleDraweeView iv_head_pic;
    TextView tv_name;

    Button btn_login, btn_account, btn_exit;
    //接口回调
    private OnCheckedChangeListener onCheckedChangeListener;
    private OnLoginBtnClick onLoginBtnClick;

    @Override
    protected int getResource() {
        return R.layout.menu_fragment;
    }

    @Override
    protected void beforeInitView() {
    }

    @Override
    protected void initView(View rootView) {
        ll_menu_head_before_login = findViewByIdNoCast(R.id.ll_menu_head_before_login);
        ll_menu_head_after_login = findViewByIdNoCast(R.id.ll_menu_head_after_login);
        tv_name = findViewByIdNoCast(R.id.tv_name);
        iv_head_pic = findViewByIdNoCast(R.id.iv_head_pic);
        btn_login = findViewByIdNoCast(R.id.login_btn);
        btn_account = findViewByIdNoCast(R.id.btn_account);
        btn_exit = findViewByIdNoCast(R.id.btn_exit);


        setOnClick(R.id.login_btn, R.id.tv_name, R.id.iv_head_pic, R.id.rb1, R.id.rb2, R.id.rb3, R.id.rb4, R.id.btn_account
                , R.id.btn_exit);
        //注册
        MyApplication.getApplication().registerOnLoginListener(this);
    }

    @Override
    protected void initData() {

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.login_btn:
                if (onLoginBtnClick != null)
                    onLoginBtnClick.onLogin();
                startActivityForResult(new Intent(getActivity(), LoginActivity.class), 1);
                break;
            case R.id.tv_name:

                break;
            case R.id.iv_head_pic://图片选择器
                SelectPictureActivity.startActivityForResult(getActivity(),new ArrayList<String>());
                break;
            case R.id.rb1:
                if (onCheckedChangeListener != null)
                    onCheckedChangeListener.onCheckedChanged(0);
                break;
            case R.id.rb2:
                if (onCheckedChangeListener != null)
                    onCheckedChangeListener.onCheckedChanged(1);
                break;
            case R.id.rb3:
                if (onCheckedChangeListener != null)
                    onCheckedChangeListener.onCheckedChanged(2);
                break;
            case R.id.rb4:
                if (onCheckedChangeListener != null)
                    onCheckedChangeListener.onCheckedChanged(3);
                break;
            case R.id.btn_account:
                //退出当前账号，转至登录窗口
                getContext()
                        .getSharedPreferences("userInfo", Context.MODE_PRIVATE)//
                        .edit()//
                        .clear()//
                        .commit();
                IntentUtils.openActivity(getActivity(), LoginActivity.class);
                MyApplication.getApplication().setUser(null);
                break;
            case R.id.btn_exit:
                System.exit(0);
                break;
        }
    }

    @Override
    public void onDestroy() {
        MyApplication.getApplication().unRegisterOnLoginListener(this);
        super.onDestroy();

    }

    public void setOnCheckedChangeListener(OnCheckedChangeListener onCheckedChangeListener) {
        this.onCheckedChangeListener = onCheckedChangeListener;
    }

    public void setOnLoginBtnClick(MenuFragment.OnLoginBtnClick onLoginBtnClick) {
        this.onLoginBtnClick = onLoginBtnClick;
    }

    @Override
    public void onLogin(User user) {
        switchView(user);
    }

    private void switchView(User user) {
        if (user == null) {
            ll_menu_head_before_login.setVisibility(View.VISIBLE);
            ll_menu_head_after_login.setVisibility(View.GONE);
        } else {
            ll_menu_head_before_login.setVisibility(View.GONE);
            ll_menu_head_after_login.setVisibility(View.VISIBLE);
            //设置名字
            tv_name.setText(user.getUserName());
            //设置图像
            iv_head_pic.setImageURI(user.profile_image);
        }
    }

    public interface OnCheckedChangeListener {
        void onCheckedChanged(int index);
    }

    public interface OnLoginBtnClick {
        void onLogin();
    }


}
