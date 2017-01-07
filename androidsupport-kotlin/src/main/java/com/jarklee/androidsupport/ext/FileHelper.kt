/*
 * ******************************************************************************
 *  Copyright Ⓒ 2016. TrinhQuan. All right reserved
 *  Author: TrinhQuan. Created on 2016/12/20
 *  Contact: trinhquan.171093@gmail.com
 * ******************************************************************************
 */

package com.jarklee.androidsupport.ext

import android.content.Context

import java.io.File

fun Context.getCachedFolder(): File {
    var cachedFolder = this.externalCacheDir
    if (cachedFolder == null) {
        cachedFolder = this.cacheDir
    }
    return cachedFolder
}

fun Context.getAppFolder(): File {
    var appFolder = this.getExternalFilesDir(null)
    if (appFolder == null) {
        appFolder = this.filesDir
    }
    return appFolder
}

fun Context.randomTempFile(): File {
    val cacheDir = getCachedFolder()
    var randomFileName = String.format("temp_file_%s.tmp", StringHelper.randomString("temp_file"))
    var tempFile = File(cacheDir, randomFileName)
    while (tempFile.exists()) {
        randomFileName = String.format("temp_file_%s.tmp", StringHelper.randomString("temp_file"))
        tempFile = File(cacheDir, randomFileName)
    }
    return tempFile
}

fun Context.tempFile(tempFileInvoke: (tempFile: File) -> Unit) {
    val tempFile = this.randomTempFile()
    try {
        tempFileInvoke(tempFile)
    } finally {
        tempFile.delete()
    }
}

fun <T> Context.tempFile(tempFileInvoke: (tempFile: File) -> T): T {
    val tempFile = this.randomTempFile()
    try {
        return tempFileInvoke(tempFile)
    } finally {
        tempFile.delete()
    }
}