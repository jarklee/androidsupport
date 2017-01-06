/*
 * ******************************************************************************
 *  Copyright Ⓒ 2017. TrinhQuan. All right reserved
 *  Author: TrinhQuan. Created on 2017/1/6
 *  Contact: trinhquan.171093@gmail.com
 * ******************************************************************************
 */

package com.jarklee.androidsupport.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Path;
import android.graphics.RectF;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

import com.jarklee.androidsupport.R;

public class RoundCornerRelativeLayout extends RelativeLayout {
    private static final int DEFAULT_RADIUS = 0;
    private int mRadius = DEFAULT_RADIUS;

    public RoundCornerRelativeLayout(Context context) {
        this(context, null);
    }

    public RoundCornerRelativeLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RoundCornerRelativeLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        loadStateFromAttrs(attrs);
        init();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public RoundCornerRelativeLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        loadStateFromAttrs(attrs);
    }

    private void loadStateFromAttrs(AttributeSet attributeSet) {
        if (attributeSet == null) {
            return;
        }
        TypedArray a = null;
        try {
            a = getContext().obtainStyledAttributes(attributeSet, R.styleable.Jarklee_RoundCornerLayout);
            mRadius = a.getDimensionPixelSize(R.styleable.Jarklee_RoundCornerLayout_RoundCornerLayout_radius, DEFAULT_RADIUS);
        } finally {
            if (a != null) {
                a.recycle();
            }
        }
    }

    private RectF mRect;
    private Path mPath;

    private void init() {
        mRect = new RectF(0, 0, getWidth(), getHeight());
        mPath = new Path();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mRect.set(0, 0, w, h);
        mPath.reset();
        mPath.addRoundRect(mRect, mRadius, mRadius, Path.Direction.CW);
        mPath.close();
    }

    public void setCornerRadius(int radius) {
        mRadius = radius;
        invalidate();
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        int save = canvas.save();
        canvas.clipPath(mPath);
        super.dispatchDraw(canvas);
        canvas.restoreToCount(save);
    }

}
