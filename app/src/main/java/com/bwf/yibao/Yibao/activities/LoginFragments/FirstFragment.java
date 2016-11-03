package com.bwf.yibao.Yibao.activities.LoginFragments;

import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.bwf.yibao.MyApplication;
import com.bwf.yibao.R;
import com.bwf.yibao.Yibao.activities.BaseFragment;
import com.bwf.yibao.Yibao.entities.User;
import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.bean.SHARE_MEDIA;

import java.util.Map;

/**
 * Created by nicholas on 2016/9/8.
 */
public class FirstFragment extends BaseFragment {
    public Button login_btn, register_btn;
    public ImageButton login_indicator_weixin, login_indicator_qq, login_indicator_sina, login_indicator_netease;

    private OnTopBtnClickListener onTopBtnClickListener;
    private OnBottomBtnClickListener onBottomBtnClickListener;

    @Override
    protected int getResource() {
        return R.layout.fragment_splash;
    }

    @Override
    protected void beforeInitView() {

    }

    @Override
    protected void initView(View rootView) {
        login_btn = findViewByIdNoCast(R.id.login_btn);
        register_btn = findViewByIdNoCast(R.id.register_btn);
        login_indicator_weixin = findViewByIdNoCast(R.id.login_indicator_weixin);
        login_indicator_qq = findViewByIdNoCast(R.id.login_indicator_qq);
        login_indicator_sina = findViewByIdNoCast(R.id.login_indicator_sina);
        login_indicator_netease = findViewByIdNoCast(R.id.login_indicator_netease);

        setOnClick(login_btn,register_btn, login_indicator_weixin,login_indicator_qq, login_indicator_sina, login_indicator_netease );
    }

    @Override
    protected void initData() {

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.login_btn:
                    if(onTopBtnClickListener != null)
                        onTopBtnClickListener.onClick();
                break;
            case R.id.register_btn:
                if(onBottomBtnClickListener != null)
                    onBottomBtnClickListener.onClick();
                break;
            case R.id.login_indicator_weixin:
/*
                MyApplication.getApplication().getUmShareAPI().doOauthVerify(getActivity(), SHARE_MEDIA.WEIXIN, new UMAuthListener() {
                    @Override
                    public void onComplete(SHARE_MEDIA share_media, int i, Map<String, String> map) {
                        //授权成功，获取用户信息
                        MyApplication.getApplication().getUmShareAPI().getPlatformInfo(getActivity(), share_media, new UMAuthListener() {
                            @Override
                            public void onComplete(SHARE_MEDIA share_media, int i, Map<String, String> map) {
                                User user = new User();
                                user.setUserName(map.get("screen_name"));
                                user.profile_image = map.get("profile_image_url");
                                MyApplication.getApplication().setUser(user);
                                getActivity().finish();
                            }

                            @Override
                            public void onError(SHARE_MEDIA share_media, int i, Throwable throwable) {

                            }

                            @Override
                            public void onCancel(SHARE_MEDIA share_media, int i) {

                            }
                        });
                    }

                    @Override
                    public void onError(SHARE_MEDIA share_media, int i, Throwable throwable) {

                    }

                    @Override
                    public void onCancel(SHARE_MEDIA share_media, int i) {

                    }
                });
*/
                break;
            case R.id.login_indicator_qq:
                MyApplication.getApplication().getUmShareAPI().doOauthVerify(getActivity(), SHARE_MEDIA.QQ, new UMAuthListener() {
                    @Override
                    public void onComplete(SHARE_MEDIA share_media, int i, Map<String, String> map) {
                        //授权成功，获取用户信息
                        MyApplication.getApplication().getUmShareAPI().getPlatformInfo(getActivity(), share_media, new UMAuthListener() {
                            @Override
                            public void onComplete(SHARE_MEDIA share_media, int i, Map<String, String> map) {
                                User user = new User();
                                user.setUserName(map.get("screen_name"));
                                user.profile_image = map.get("profile_image_url");
                                MyApplication.getApplication().setUser(user);
                                getActivity().finish();
                            }

                            @Override
                            public void onError(SHARE_MEDIA share_media, int i, Throwable throwable) {

                            }

                            @Override
                            public void onCancel(SHARE_MEDIA share_media, int i) {

                            }
                        });
                    }

                    @Override
                    public void onError(SHARE_MEDIA share_media, int i, Throwable throwable) {

                    }

                    @Override
                    public void onCancel(SHARE_MEDIA share_media, int i) {

                    }
                });

                break;
            case R.id.login_indicator_sina:
                break;
            case R.id.login_indicator_netease:
                break;


        }
    }

    public interface OnTopBtnClickListener{
        public void onClick();
    }
    public interface OnBottomBtnClickListener{
        public void onClick();
    }

    public void setOnTopBtnClickListener(OnTopBtnClickListener onTopBtnClickListener) {
        this.onTopBtnClickListener = onTopBtnClickListener;
    }

    public void setOnBottomBtnClickListener(OnBottomBtnClickListener onBottomBtnClickListener) {
        this.onBottomBtnClickListener = onBottomBtnClickListener;
    }
}
