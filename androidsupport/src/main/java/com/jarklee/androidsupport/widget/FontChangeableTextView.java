/*
 * ******************************************************************************
 *  Copyright Ⓒ 2017. TrinhQuan. All right reserved
 *  Author: TrinhQuan. Created on 2017/1/6
 *  Contact: trinhquan.171093@gmail.com
 * ******************************************************************************
 */

package com.jarklee.androidsupport.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatTextView;

import com.jarklee.androidsupport.R;

public class FontChangeableTextView extends AppCompatTextView {

    private Typeface mCustomTypeFaceFont;

    public FontChangeableTextView(Context context) {
        this(context, null);
    }

    public FontChangeableTextView(Context context, AttributeSet attrs) {
        this(context, attrs, android.R.attr.textViewStyle);
    }

    public FontChangeableTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        loadStateFromAttrs(attrs);
        if (mCustomTypeFaceFont != null) {
            setTypeface(mCustomTypeFaceFont);
        }
    }

    private void loadStateFromAttrs(AttributeSet attributeSet) {
        if (attributeSet == null) {
            return;
        }
        TypedArray a = null;
        try {
            a = getContext().obtainStyledAttributes(attributeSet, R.styleable.Jarklee_FontChangeable);
            String customFont = a.getString(R.styleable.Jarklee_FontChangeable_FontChangeable_custom_font);
            if (customFont != null) {
                mCustomTypeFaceFont = Typeface.createFromAsset(getContext().getAssets(), customFont);
                Typeface original = getTypeface();
                if (original.isBold() && original.isItalic()) {
                    mCustomTypeFaceFont = Typeface.create(mCustomTypeFaceFont, Typeface.BOLD_ITALIC);
                } else if (original.isBold()) {
                    mCustomTypeFaceFont = Typeface.create(mCustomTypeFaceFont, Typeface.BOLD);
                } else if (original.isItalic()) {
                    mCustomTypeFaceFont = Typeface.create(mCustomTypeFaceFont, Typeface.ITALIC);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (a != null) {
                a.recycle();
            }
        }
    }
}
