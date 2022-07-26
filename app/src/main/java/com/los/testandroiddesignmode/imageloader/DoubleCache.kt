package com.los.testandroiddesignmode.imageloader

import android.graphics.Bitmap

class DoubleCache:IMemoryCache {

    //内存缓存
    private val mMemoryCache = ImageCache()

    //Sdcard缓存
    private val mDiskCache = DiskCache()

    //先从内存缓存中获取图片，如果没有，再从sd卡中获取
    override fun get(url: String): Bitmap? {
        var bitmap = mMemoryCache.get(url)
        if (bitmap == null) {
            bitmap = mDiskCache.get(url)
        }
        return bitmap
    }

    //将图片缓存到内存和sd卡中
    override fun put(url: String, bitmap: Bitmap) {
        mMemoryCache.put(url, bitmap)
        mDiskCache.put(url, bitmap)
    }
}