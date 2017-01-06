/*
 * ******************************************************************************
 *  Copyright Ⓒ 2017. TrinhQuan. All right reserved
 *  Author: TrinhQuan. Created on 2017/1/6
 *  Contact: trinhquan.171093@gmail.com
 * ******************************************************************************
 */

package com.jarklee.androidsupport.widget.utils;

import android.widget.TextView;

public class TextUtils {

    public static boolean checkEmpty(TextView textView) {
        return textView != null && textView.getText().toString().trim().length() != 0;
    }
}
