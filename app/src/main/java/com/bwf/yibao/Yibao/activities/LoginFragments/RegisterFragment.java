package com.bwf.yibao.Yibao.activities.LoginFragments;

import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.bwf.yibao.R;
import com.bwf.yibao.Yibao.activities.BaseFragment;
import com.bwf.yibao.Yibao.entities.RequestParams;
import com.bwf.yibao.Yibao.entities.User;
import com.bwf.yibao.framwork.http.HttpHelper;
import com.bwf.yibao.framwork.http.HttpUtil;
import com.bwf.yibao.framwork.utils.LogUtils;
import com.bwf.yibao.framwork.utils.ToastUtil;
import com.google.gson.Gson;

import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;

/**
 * Created by nicholas on 2016/9/8.
 */
public class RegisterFragment extends BaseFragment {
    public static final int IDENTIFY_RESULT = 0;
    public static final int REGISTER_RESULT_ERROR = 1;
    public static final int REGISTER_RESULT_SUCCESS = 2;
    public static final int GET_CODE = 3;
    public static final int SET_TIMEOUT = 4;
    /*public static final String APPKEY = "16ed311541efc";
    public static final String APPSCRETE = "7da658affa676577ccdeb1c4b7728938";*/
    public static final String APPKEY = "170c7199cfd90";
    public static final String APPSCRETE = "40aac28da81fb895f0c234cb27c307f0";
    private int time_out = 60;


    Button btn_submit;
    EditText edt_register_phone, edt_register_password, edt_identify_code;
    Button btn_getVerificationCode;
    ImageButton iBtn_actionbar_back;
    @Override
    protected int getResource() {
        return R.layout.fragment_register;
    }

    @Override
    protected void beforeInitView() {

    }

    @Override
    protected void initView(View rootView) {
        btn_submit = findViewByIdNoCast(R.id.btn_submit);
        btn_getVerificationCode = findViewByIdNoCast(R.id.btn_getVerificationCode);
        edt_register_phone = findViewByIdNoCast(R.id.edt_register_phone);
        edt_register_password = findViewByIdNoCast(R.id.edt_register_password);
        edt_identify_code = findViewByIdNoCast(R.id.edt_identify_code);
        iBtn_actionbar_back = findViewByIdNoCast(R.id.mLeft);

        setOnClick(btn_getVerificationCode,btn_submit, iBtn_actionbar_back);
    }

    @Override
    protected void initData() {

    }
    private String phoneNum = null;
    private String password = null;
    private String code = null;
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_getVerificationCode:
                phoneNum = edt_register_phone.getText().toString().trim();
                if(TextUtils.isEmpty(phoneNum)){
                    ToastUtil.showToast("please provide your phone number");
                    return;
                }
                if(phoneNum.length() != 11){
                    ToastUtil.showToast("the length of your phone number should be 11");
                    return;
                }
                SMSSDK.initSDK(getContext(),APPKEY, APPSCRETE);
                SMSSDK.registerEventHandler(new EventHandler(){
                    @Override
                    public void afterEvent(int event, int result, Object data) {
                        handler.sendMessage(Message.obtain(handler, IDENTIFY_RESULT, event, result, data));
                    }
                });
                SMSSDK.getVerificationCode("86",phoneNum);
                ToastUtil.showToast("验证码已发送至：" + phoneNum);
                //set clickable
                btn_getVerificationCode.setClickable(false);
                //set time_out
                handler.sendEmptyMessage(SET_TIMEOUT);
                break;
            case R.id.btn_submit:
                phoneNum = edt_register_phone.getText().toString().trim();
                password = edt_register_password.getText().toString().trim();
                code =  edt_identify_code.getText().toString().trim();
                if(TextUtils.isEmpty(phoneNum)){
                    ToastUtil.showToast("please provide your phone number");
                    return;
                }
                if(phoneNum.length() != 11){
                    ToastUtil.showToast("the length of your phone number should be 11");
                    return;
                }
                if(TextUtils.isEmpty(password)){
                    ToastUtil.showToast("please provide your password");
                    return;
                }
                if(TextUtils.isEmpty(code)){
                    ToastUtil.showToast("please enter the code");
                    return;
                }
                SMSSDK.submitVerificationCode("86", phoneNum, code);
                break;
            case R.id.mLeft:
                getFragmentManager().popBackStack();
                break;
        }
    }
    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case IDENTIFY_RESULT:
                    switch (msg.arg1){
                        case SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE:
                            if(msg.arg2 == SMSSDK.RESULT_COMPLETE){
                                //send a register request to server
                                RequestParams requestParams = new RequestParams();
                                User user = new User();
                                        user.setUserName(phoneNum);
                                        user.setPwd(password);
                                String userInfo = new Gson().toJson(user);
                                requestParams.user = userInfo;
                                HttpHelper.registerUser(requestParams, new HttpUtil.CallBack() {
                                    @Override
                                    public void onSuccess(String result) {
                                        handler.sendMessage(Message.obtain(handler,REGISTER_RESULT_SUCCESS, result));
                                    }

                                    @Override
                                    public void onFailed(String error) {
                                        handler.sendMessage(Message.obtain(handler,REGISTER_RESULT_ERROR, error));
                                    }
                                });

                            }else{
                                ToastUtil.showToast("Invalid validation code");
                            }
                        case SMSSDK.EVENT_GET_VERIFICATION_CODE:
                            if(msg.arg2 == SMSSDK.RESULT_COMPLETE){
                                LogUtils.i("zc", "success to send code");
                            }
                            else
                                LogUtils.i("zc", "failed to send code");
                            break;
                    }
                    break;

                case REGISTER_RESULT_SUCCESS:
                    ToastUtil.showToast("success to register");
                    break;
                case REGISTER_RESULT_ERROR:
                    ToastUtil.showToast((String)msg.obj);
                    break;
                case GET_CODE:
                    btn_getVerificationCode.setText("VERIGICATION CODE");
                    btn_getVerificationCode.setClickable(true);
                    break;
                case SET_TIMEOUT:
                    btn_getVerificationCode.setText("Get Code: " + (time_out--));
                    if (time_out < 0){
                        handler.sendEmptyMessage(GET_CODE);
                        time_out = 60;
                    }
                    else
                        handler.sendEmptyMessageDelayed(SET_TIMEOUT, 1000);
                    break;
            }


        }
    };
}
