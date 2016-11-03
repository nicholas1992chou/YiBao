package com.bwf.yibao.Yibao.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewConfiguration;
import android.widget.ScrollView;

/**
 * Created by nicholas on 2016/9/7.
 */
public class CustomScrollView extends ScrollView {
    public CustomScrollView(Context context) {
        this(context, null);
    }

    public CustomScrollView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CustomScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
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
               /* if(Math.abs((ev.getX()-getX)) > TouchSlop ){
                    return false;
                }*/

                break;
            default:
                break;
        }

        return super.onInterceptTouchEvent(ev);
    }
}
