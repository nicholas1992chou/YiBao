package com.bwf.yibao.Yibao.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.bwf.yibao.R;

public class TestActivity extends BaseActivity {
Toolbar toolbar;

    @Override
    public int getContentViewId() {
        return R.layout.activity_test;
    }

    @Override
    public void beforeInitView(Bundle bundle) {

    }

    @Override
    public void initView() {
        toolbar = findViewByIdNoCast(R.id.toolbar);
        toolbar.setNavigationIcon(R.mipmap.ic_launcher);
        toolbar.inflateMenu(R.menu.basemenu);
        //toolbar.setLogo(R.mipmap.menu_icon);
        //toolbar.setTitle("应用");
       // setSupportActionBar(toolbar);

    }

    @Override
    public void initData() {

    }

    @Override
    public void onClick(View view) {

    }
}
