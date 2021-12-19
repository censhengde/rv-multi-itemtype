package com.tencent.lib.multi.core

import android.util.SparseArray
import android.view.View
import androidx.annotation.IdRes
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding

/**
 * Author：岑胜德 on 2021/1/6 14:58
 *
 *
 * 说明：
 */
open class MultiViewHolder(val vb:ViewBinding) : RecyclerView.ViewHolder(vb.root)