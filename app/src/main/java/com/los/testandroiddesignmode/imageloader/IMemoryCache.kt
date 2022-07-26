package com.los.testandroiddesignmode.imageloader

import android.graphics.Bitmap

interface IMemoryCache {

    fun put(url: String, bitmap: Bitmap)

    fun get(url: String): Bitmap?

}