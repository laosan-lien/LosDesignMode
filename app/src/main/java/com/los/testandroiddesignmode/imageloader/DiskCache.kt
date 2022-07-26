package com.los.testandroiddesignmode.imageloader

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import java.io.FileOutputStream
import java.io.IOException


private const val cacheDir = "sdcard/cache/"
private const val TAG = "Los:DiskCache"

class DiskCache:IMemoryCache {


    override fun get(url: String): Bitmap? {
        return BitmapFactory.decodeFile(cacheDir + url)
    }

    //将image缓存到内存中
    override fun put(url: String, bitmap: Bitmap) {
        var fileOutputStream: FileOutputStream? = null
        try {
            fileOutputStream = FileOutputStream(cacheDir + url)
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream)
        } catch (e: IOException) {
            Log.d(TAG, "put: images store error")
        } finally {
            if (fileOutputStream != null) {
                try {
                    fileOutputStream.close()
                } catch (e: IOException) {
                    Log.d(TAG, "put: file output stream close error")
                }
            }
        }
    }
}