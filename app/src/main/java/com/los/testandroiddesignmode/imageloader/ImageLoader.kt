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


    //内存缓存
    private val mImageCache = ImageCache()

    //Sdcard缓存
    private val mDiskCache = DiskCache()

    //双缓存
    private val mDoubleCache = DoubleCache()

    //是否使用sdCard缓存
    private var isUseDiskCache = false

    //是否使用双缓存
    private var isDoubleCache = false


    //线程池
    private var mExecutorService: ExecutorService =
        Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors())
    private var mUiHandler: Handler = Handler(Looper.getMainLooper())


    //加载图片
    private fun displayImage(url: String, imageView: ImageView) {
        var bitmap: Bitmap? = if (isUseDiskCache) {
            mDoubleCache.get(url)
        } else if (isUseDiskCache) {
            mDiskCache.get(url)
        } else {
            mImageCache.get(url)
        }
        bitmap?.let {
            imageView.setImageBitmap(it)
            return
        }
        //没有缓存就提交给线程池进行异步下载图片
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

    fun useDiskCache(useDiskCache: Boolean) {
        isUseDiskCache = useDiskCache
    }

    fun useDoubleCache(useDoubleCache: Boolean) {
        isDoubleCache = useDoubleCache
    }


}