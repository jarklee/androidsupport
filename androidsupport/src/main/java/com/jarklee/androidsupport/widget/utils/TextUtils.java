/*
 * ******************************************************************************
 *  Copyright Ⓒ 2016. Dotohsoft.com. All right reserved
 *  Author TrinhQuan. Create on 2016/12/24
 * ******************************************************************************
 */

package com.jarklee.androidsupport.widget.utils;

import android.widget.TextView;

public class TextUtils {

    public static boolean checkEmpty(TextView textView) {
        return textView != null && textView.getText().toString().trim().length() != 0;
    }
}
