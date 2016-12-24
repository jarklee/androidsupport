/*
 * ******************************************************************************
 *  Copyright Ⓒ 2016. Dotohsoft.com. All right reserved
 *  Author TrinhQuan. Create on 2016/12/24
 * ******************************************************************************
 */

package com.jarklee.androidsupport.widget.utils;

import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;

import java.util.ArrayList;
import java.util.List;

public class SpannableBuilder {

    private static final int DEFAULT_FLAG = Spanned.SPAN_EXCLUSIVE_EXCLUSIVE;

    private String spannableString;
    private List<SpannableToken> tokens;

    public SpannableBuilder() {
        spannableString = "";
    }

    public SpannableBuilder appendText(String text) {
        spannableString += text;
        return this;
    }

    public SpannableBuilder appendText(String text, Object span) {
        return appendText(text, span, DEFAULT_FLAG);
    }

    public SpannableBuilder appendText(String text, Object span, int flag) {
        if (tokens == null) {
            tokens = new ArrayList<>();
        }
        SpannableToken token = new SpannableToken(span, spannableString.length(), text.length(), flag);
        tokens.add(token);
        return appendText(text);
    }

    public SpannableBuilder addSpan(Object span, int start, int stop) {
        return addSpan(span, start, stop, DEFAULT_FLAG);
    }

    public SpannableBuilder addSpan(Object span, int start, int stop, int flag) {
        if (tokens == null) {
            tokens = new ArrayList<>();
        }
        SpannableToken token = new SpannableToken(span, start, stop, flag);
        tokens.add(token);
        return this;
    }

    public Spannable getSpannable() {
        Spannable spannable = new SpannableString(spannableString);
        if (tokens != null) {
            for (SpannableToken token : tokens) {
                spannable.setSpan(token.getStyle(), token.getStart(),
                        token.getStart() + token.getLength(),
                        token.getFlag());
            }
        }
        return spannable;
    }

    private static class SpannableToken {
        private final int flag;
        private int start;
        private int length;
        private Object style;

        public SpannableToken(Object style, int start, int length, int flag) {
            this.start = start;
            this.length = length;
            this.style = style;
            this.flag = flag;
        }

        public int getStart() {
            return start;
        }

        public int getLength() {
            return length;
        }

        public Object getStyle() {
            return style;
        }

        public int getFlag() {
            return flag;
        }
    }
}
