package com.bwf.yibao.Yibao.views;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

import com.bwf.yibao.R;
import com.bwf.yibao.framwork.utils.LogUtils;

/**
 * Created by nicholas on 2016/9/19.
 */
public class ViewpagerIndicator extends LinearLayout {
    public static final int SDEFAULT_COUNT = 3;
    private static final int sRectHeight = 5;
    private int mVisibleCount;
    private int mIndicatorColor;
    private Paint mPaint;
    private RectF mRectf;
    private float mTranslationX;
    private int mTabWidth;
    private float currentPos = 0;
    public ViewpagerIndicator(Context context) {
        this(context, null);
    }
    public ViewpagerIndicator(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ViewpagerIndicator(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray typedArray = context.getTheme().obtainStyledAttributes(attrs, R.styleable.viewpagerIndicator, defStyleAttr, 0);
        mVisibleCount = typedArray.getInteger(R.styleable.viewpagerIndicator_visibleCount, SDEFAULT_COUNT);
        mIndicatorColor = typedArray.getColor(R.styleable.viewpagerIndicator_indicatorColor,getResources().getColor(R.color.colorPrimary) );
        if(mVisibleCount < 0)
            mVisibleCount = SDEFAULT_COUNT;
        typedArray.recycle();
        initPaint();

    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        canvas.save();
        canvas.translate(mTranslationX,getHeight()- sRectHeight);
        canvas.drawRect(mRectf, mPaint);
        canvas.restore();
        super.dispatchDraw(canvas);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        //获取每个tab的宽度
        mTabWidth = Math.round(w /(float)mVisibleCount);
        //初始化indicator
        mRectf = new RectF(0, 0, mTabWidth, sRectHeight);

    }

    /**
     * 初始化画笔
     */
    private void initPaint(){
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setColor(mIndicatorColor);
        mPaint.setStyle(Paint.Style.FILL);
    }
    /**
     * 通过其他view滑动的offset 去滑动indicator；
     * @param position
     * @param offset
     */
    public void startScroll(int position,float offset){
        mTranslationX = currentPos + offset;
        invalidate();
    }

    //初始化属性动画
    ValueAnimator mAnimator = ValueAnimator.ofFloat();

    /**
     *
     * @param position 滑动结束位置
     */
    public void startSroll(final int position){
        currentPos = mTranslationX;
        mAnimator.setFloatValues(currentPos, position * mTabWidth);
        mAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                mTranslationX = (float) valueAnimator.getAnimatedValue();
                invalidate();
            }
        });
        mAnimator.setDuration(100);
        if(mAnimator.isRunning())
            mAnimator.cancel();
        mAnimator.start();

    }
}
