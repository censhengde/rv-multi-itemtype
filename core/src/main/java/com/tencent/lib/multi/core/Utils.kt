@file:JvmName("Utils")
package com.tencent.lib.multi.core

import android.content.Context
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import java.lang.IllegalArgumentException
import java.security.AccessControlContext

/**

 * Author：岑胜德 on 2021/2/20 15:15

 * 说明：

 */
fun assertNull(any: Any?,msg:String){
    if (any==null){
        throw IllegalArgumentException(msg)
    }

}

fun createInvalidViewHolder(context: Context): RecyclerView.ViewHolder =object :RecyclerView.ViewHolder(View(context)){}
