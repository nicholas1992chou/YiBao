package com.bwf.yibao.Yibao.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;

import com.bwf.yibao.MyApplication;
import com.bwf.yibao.R;
import com.bwf.yibao.Yibao.activities.LoginFragments.FirstFragment;
import com.bwf.yibao.Yibao.activities.LoginFragments.LoginFragment;
import com.bwf.yibao.Yibao.activities.LoginFragments.RegisterFragment;

public class LoginActivity extends BaseActivity {
    public FirstFragment firstFragment;
    public LoginFragment loginFragment;
    public RegisterFragment registerFragment;
    public FragmentManager fragmentManager;

    public FragmentTransaction fragmentTransaction;
    @Override
    public int getContentViewId() {
        return R.layout.activity_login;
    }

    @Override
    public void beforeInitView(Bundle savedInstanceState) {
        fragmentManager = getSupportFragmentManager();
        if (fragmentManager.findFragmentById(R.id.loginActivity_fl) == null){
            firstFragment = new FirstFragment();
            fragmentManager.beginTransaction().add(R.id.loginActivity_fl, firstFragment, "firstFragment").commit();
        }
    }

    @Override
    public void initView() {
        firstFragment.setOnTopBtnClickListener(new FirstFragment.OnTopBtnClickListener() {
            @Override
            public void onClick() {
                fragmentTransaction = fragmentManager.beginTransaction();
                if(loginFragment == null){
                    loginFragment  = new LoginFragment();
                    //fragmentManager.beginTransaction().add(R.id.loginActivity_fl,loginFragment, "loginFragment").commit();
                }
                fragmentTransaction.addToBackStack(null).replace(R.id.loginActivity_fl, loginFragment,"loginFragment" ).commit();

            }
        });
        firstFragment.setOnBottomBtnClickListener(new FirstFragment.OnBottomBtnClickListener() {
            @Override
            public void onClick() {
                fragmentTransaction = fragmentManager.beginTransaction();
                if(registerFragment == null){
                    registerFragment  = new RegisterFragment();
                }
                fragmentTransaction.addToBackStack(null).replace(R.id.loginActivity_fl, registerFragment,"registerFragment" ).commit();
            }
        });

    }

    @Override
    public void initData() {

    }

    @Override
    public void onClick(View view) {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        MyApplication.getApplication().getUmShareAPI().onActivityResult(requestCode, resultCode, data);
    }
}
