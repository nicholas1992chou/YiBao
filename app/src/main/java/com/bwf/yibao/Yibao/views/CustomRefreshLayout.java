package com.bwf.yibao.Yibao.views;

import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ViewConfiguration;

/**
 * Created by nicholas on 2016/9/6.
 */
public class CustomRefreshLayout extends SwipeRefreshLayout {

    public CustomRefreshLayout(Context context) {
        this(context, null);
    }

    public CustomRefreshLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        TouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();

    }
    int TouchSlop;
    float getX;
    float getY;
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        switch (ev.getAction()){
            case MotionEvent.ACTION_DOWN:
                getX  = ev.getX();
                getY  = ev.getY();

                break;
            case MotionEvent.ACTION_MOVE:
                if(Math.abs((ev.getX()-getX)) > TouchSlop ){
                    return false;
                }

                break;
            default:
                break;
        }

        return super.onInterceptTouchEvent(ev);
    }
}
