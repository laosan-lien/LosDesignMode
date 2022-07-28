package com.los.testandroiddesignmode.utils

import android.util.Log
import java.io.Closeable
import java.io.IOException

object CloseUtil {

    fun closeQuietly(closeable: Closeable?){
        if(null != closeable){
            try {
                closeable.close()
            }catch (e:IOException){
                Log.d("CloseUtil", "closeQuietly: 关闭接口异常")
            }
        }
    }

}