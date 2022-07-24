package com.los.testandroiddesignmode.imageloader

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.util.LruCache
import android.widget.ImageView
import java.io.IOException
import java.net.HttpURLConnection
import java.net.URL
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

private const val TAG = "Los:ImageLoader"

class ImageLoader {


    private val mImageCache = ImageCache()
    private var mExecutorService: ExecutorService =
        Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors())
    private var mUiHandler: Handler = Handler(Looper.getMainLooper())


    //加载图片
    private fun displayImage(url: String, imageView: ImageView) {
        var bitmap: Bitmap? = mImageCache.get(url)
        bitmap?.let {
            imageView.setImageBitmap(it)
            return
        }
        imageView.tag = url
        mExecutorService.submit {
            bitmap = downloadImage(url) ?: return@submit
            if (imageView.tag == url) {
                updateImageView(imageView, bitmap!!)
            }
            mImageCache.put(url, bitmap!!)
        }
    }

    private fun updateImageView(imageView: ImageView, bitmap: Bitmap) {
        mUiHandler.post {
            imageView.setImageBitmap(bitmap)
        }
    }

    private fun downloadImage(url: String): Bitmap? {
        var bitmap: Bitmap? = null
        try {
            val imageUrl = URL(url)
            val httpURLConnection = imageUrl.openConnection() as HttpURLConnection
            bitmap = BitmapFactory.decodeStream(httpURLConnection.inputStream)
            httpURLConnection.disconnect()
        } catch (e: IOException) {
            Log.d(TAG, "downloadImage: bitmap download error")
        }
        return bitmap
    }




}