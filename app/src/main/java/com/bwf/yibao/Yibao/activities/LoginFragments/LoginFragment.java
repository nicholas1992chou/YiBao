package com.bwf.yibao.Yibao.activities.LoginFragments;

import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.bwf.yibao.MyApplication;
import com.bwf.yibao.R;
import com.bwf.yibao.Yibao.activities.BaseFragment;
import com.bwf.yibao.Yibao.entities.User;
import com.bwf.yibao.framwork.utils.NetWorkUtils;
import com.bwf.yibao.framwork.utils.ToastUtil;

/**
 * Created by nicholas on 2016/9/8.
 */
public class LoginFragment extends BaseFragment {
    public EditText edt_phone;
    public EditText edt_password;
    public Button btn_submit;
    public TextView tv_forget;
    TextView tv_title;
    ImageButton iBtn_actionbar_back;
    @Override
    protected int getResource() {
        return R.layout.fragment_login;
    }

    @Override
    protected void beforeInitView() {
        edt_phone = findViewByIdNoCast(R.id.login_edt_phone);
        edt_password = findViewByIdNoCast(R.id.login_edt_password);
        btn_submit = findViewByIdNoCast(R.id.btn_submit);
        tv_forget = findViewByIdNoCast(R.id.login_tv_forget);

    }

    @Override
    protected void initView(View rootView) {
        edt_phone = findViewByIdNoCast(R.id.login_edt_phone);
        edt_password = findViewByIdNoCast(R.id.login_edt_password);
        btn_submit = findViewByIdNoCast(R.id.btn_submit);
        tv_forget = findViewByIdNoCast(R.id.login_tv_forget);
        tv_title = findViewByIdNoCast(R.id.mCenter);
        tv_title.setText("sign in");
        iBtn_actionbar_back = findViewByIdNoCast(R.id.mLeft);
        setOnClick(btn_submit, tv_forget, iBtn_actionbar_back);

    }

    @Override
    protected void initData() {

    }

    @Override
    public void onClick(View view) {
        String phoneNumber = null;
        String password = null;
        switch (view.getId()){
            case R.id.btn_submit:
                phoneNumber = edt_phone.getText().toString().trim();
                password = edt_password.getText().toString().trim();
                if(TextUtils.isEmpty(phoneNumber))
                {
                    ToastUtil.showToast("请输入手机号");
                    return;
                }
                if (TextUtils.isEmpty(password))
                {
                    ToastUtil.showToast("请输入密码");
                    return;
                }
                if(!NetWorkUtils.isNetDeviceAvailable(getActivity()))
                {
                    ToastUtil.showToast("网络断开");
                    return;
                }
                //显示进度动画

                btn_submit.setText("登录中...");
                User user = new User();
                user.setUserName(phoneNumber);
                user.setPwd(password);
                user.profile_image = "res://mipmap/" + R.mipmap.user2;
                if(phoneNumber.equals("123") && "123".equals(password)){
                    MyApplication.getApplication().setUser(user);
                    this.getActivity().finish();
                }
                break;
            case R.id.login_tv_forget:
                break;
            case R.id.mLeft:
                getFragmentManager().popBackStack();
                break;
        }
    }

}
