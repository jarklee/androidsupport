/*
 * ******************************************************************************
 *  Copyright Ⓒ 2016. Dotohsoft.com. All right reserved
 *  Author TrinhQuan. Create on 2016/12/24
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
import android.widget.LinearLayout;

import com.jarklee.androidsupport.R;

public class RoundCornerLinearLayout extends LinearLayout {

    private static final int DEFAULT_RADIUS = 0;
    private int mRadius = DEFAULT_RADIUS;

    public RoundCornerLinearLayout(Context context) {
        this(context, null);
    }

    public RoundCornerLinearLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        loadStateFromAttrs(attrs);
        init();
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public RoundCornerLinearLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        loadStateFromAttrs(attrs);
        init();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public RoundCornerLinearLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        loadStateFromAttrs(attrs);
        init();
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
