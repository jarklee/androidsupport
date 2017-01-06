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
import android.graphics.drawable.Drawable;
import android.support.v7.widget.AppCompatAutoCompleteTextView;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;

import com.jarklee.androidsupport.R;
import com.jarklee.androidsupport.widget.utils.OnClearableViewListener;
import com.jarklee.androidsupport.widget.utils.TextWatcherAdapter;

public class ClearableAutoCompleteTextView extends AppCompatAutoCompleteTextView implements View.OnTouchListener, View.OnFocusChangeListener, TextWatcherAdapter.TextWatcherListener {

    private Drawable xD;
    private OnClearableViewListener listener;
    private OnTouchListener l;
    private OnFocusChangeListener f;

    private static final int DEFAULT_CLEAR_ICON = android.R.drawable.presence_offline;

    private int mClearIconResource = R.drawable.ic_edittext_clear;
    private Typeface mCustomTypeFaceFont;

    public ClearableAutoCompleteTextView(Context context) {
        this(context, null);
    }

    public ClearableAutoCompleteTextView(Context context, AttributeSet attrs) {
        this(context, attrs, android.support.v7.appcompat.R.attr.autoCompleteTextViewStyle);
    }

    public ClearableAutoCompleteTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
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

    public void setOnClearListener(OnClearableViewListener listener) {
        this.listener = listener;
    }

    @Override
    public void setOnTouchListener(OnTouchListener l) {
        this.l = l;
    }

    @Override
    public void setOnFocusChangeListener(OnFocusChangeListener f) {
        this.f = f;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (getCompoundDrawables()[2] != null) {
            boolean tappedX = event.getX() > (getWidth() - getPaddingRight() - xD.getIntrinsicWidth());
            if (tappedX) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    setText("");
                    if (listener != null) {
                        listener.didClearText();
                    }
                }
                return true;
            }
        }
        return l != null && l.onTouch(v, event);
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if (hasFocus) {
            resetError();
            setClearIconVisible(isNotEmpty(String.valueOf(getText())));
        } else {
            setClearIconVisible(false);
        }
        if (f != null) {
            f.onFocusChange(v, hasFocus);
        }
    }

    private boolean isNotEmpty(String text) {
        return text.length() > 0;
    }

    @Override
    public void onTextChanged(EditText view, String text) {
        if (isFocused()) {
            resetError();
            setClearIconVisible(isNotEmpty(text));
        }
    }

    @SuppressWarnings("deprecation")
    private void init() {
        if (isInEditMode())
            return;
        xD = getCompoundDrawables()[2];
        if (xD == null) {
            xD = getResources().getDrawable(mClearIconResource);
        }
        assert xD != null;
        xD.setBounds(0, 0, xD.getIntrinsicWidth(), xD.getIntrinsicHeight());
        setClearIconVisible(false);
        super.setOnTouchListener(this);
        super.setOnFocusChangeListener(this);
        addTextChangedListener(new TextWatcherAdapter(this, this));
    }

    private void resetError() {
        if (getError() != null) {
            setError(null);
        }
    }

    protected void setClearIconVisible(boolean visible) {
        boolean wasVisible = (getCompoundDrawables()[2] != null);
        if (visible != wasVisible) {
            Drawable x = visible ? xD : null;
            setCompoundDrawables(getCompoundDrawables()[0], getCompoundDrawables()[1], x, getCompoundDrawables()[3]);
        }
    }
}
