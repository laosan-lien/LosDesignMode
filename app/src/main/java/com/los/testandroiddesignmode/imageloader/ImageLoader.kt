package com.los.testandroiddesignmode.imageloader

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.ImageView
import java.io.IOException
import java.net.HttpURLConnection
import java.net.URL
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

private const val TAG = "Los:ImageLoader"

class ImageLoader {


    private var mImageCache: IMemoryCache? = null

    fun setImageCache(memoryCache: ImageCache) {
        mImageCache = memoryCache
    }


    //线程池
    private var mExecutorService: ExecutorService =
        Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors())
    private var mUiHandler: Handler = Handler(Looper.getMainLooper())


    //加载图片
    private fun displayImage(url: String, imageView: ImageView) {
        val bitmap: Bitmap?
        if (mImageCache != null) {
            bitmap = mImageCache?.get(url)
            bitmap?.let {
                imageView.setImageBitmap(it)
                return
            }

            submitLoadRequest(url, imageView)
            //没有缓存就提交给线程池进行异步下载图片

        } else {
            Log.d(TAG, "displayImage: mImageCahce is null")
        }
    }

    private fun submitLoadRequest(url: String, imageView: ImageView) {
        imageView.tag = url
        mExecutorService.submit {
            val bitmap = downloadImage(url)
            if(bitmap == null){
                Log.d(TAG, "submitLoadRequest: bitmap download error")
                return@submit
            }
            if (imageView.tag == url) {
                updateImageView(imageView, bitmap)
            }
            mImageCache?.put(url, bitmap)
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