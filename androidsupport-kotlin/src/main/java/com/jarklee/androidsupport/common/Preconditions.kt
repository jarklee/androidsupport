/*
 * ******************************************************************************
 *  Copyright Ⓒ 2016. TrinhQuan. All right reserved
 *  Author: TrinhQuan. Created on 2016/12/20
 *  Contact: trinhquan.171093@gmail.com
 * ******************************************************************************
 */

package com.jarklee.androidsupport.common

import com.jarklee.androidsupport.exception.ParameterException

@Throws(ParameterException::class)
fun <T> checkNotNull(reference: T?) {
    if (reference == null) {
        throw ParameterException(NullPointerException())
    }
    if (reference is String) {
        if (reference.length == 0) {
            throw ParameterException("zero param length")
        }
    }
}
