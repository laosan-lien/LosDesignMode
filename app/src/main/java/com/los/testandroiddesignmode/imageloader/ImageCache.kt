package com.los.testandroiddesignmode.imageloader

import android.graphics.Bitmap
import android.util.LruCache
import kotlin.math.min

class ImageCache :IMemoryCache {
    private lateinit var mImageCache: LruCache<String, Bitmap>

    init {
        initImageCache()
    }

    private fun initImageCache() {
        val maxMemory = Runtime.getRuntime().maxMemory() / 1024
        val cacheSize = maxMemory / 4
        mImageCache = object : LruCache<String, Bitmap>(cacheSize.toInt()) {
            override fun sizeOf(key: String?, value: Bitmap?): Int {
                return value?.rowBytes ?: super.sizeOf(key, value)
            }
        }
    }

    override fun put(url: String, bitmap: Bitmap) {
        mImageCache.put(url, bitmap)
    }

    override fun get(url: String): Bitmap? {
        return mImageCache.get(url)
    }


}