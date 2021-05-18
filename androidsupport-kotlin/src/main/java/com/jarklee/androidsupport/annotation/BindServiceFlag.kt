/*
 * ******************************************************************************
 *  Copyright Ⓒ 2016. TrinhQuan. All right reserved
 *  Author: TrinhQuan. Created on 2016/12/20
 *  Contact: trinhquan.171093@gmail.com
 * ******************************************************************************
 */

package com.jarklee.androidsupport.annotation

import android.content.Context
import androidx.annotation.IntDef

@IntDef(
    0,
    Context.BIND_AUTO_CREATE,
    Context.BIND_DEBUG_UNBIND,
    Context.BIND_NOT_FOREGROUND
)
@Retention(AnnotationRetention.SOURCE)
annotation class BindServiceFlag
