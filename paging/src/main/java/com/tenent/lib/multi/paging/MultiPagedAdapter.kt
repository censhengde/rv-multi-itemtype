package com.tenent.lib.multi.paging

import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.tencent.lib.multi.core.MultiHelper
import com.tencent.lib.multi.core.MultiViewHolder
import com.tencent.lib.multi.core.checking.CheckingHelper

/**

 * Author：岑胜德 on 2021/4/25 19:08

 * 说明：

 */
open class MultiPagedAdapter<T : Any,VH :RecyclerView.ViewHolder>(diffCallback: DiffUtil.ItemCallback<T>)
    : PagingDataAdapter<T, VH>(diffCallback) {

    val multiHelper = object : MultiHelper<T,VH>(this) {

        override fun getItem(p0: Int): T? {
            return this@MultiPagedAdapter.getItem(p0)
        }

    }
    val checkingHelper = object : CheckingHelper<T>(this) {
        override fun getItem(position: Int): T? = this@MultiPagedAdapter.getItem(position)
        override fun getDataSize(): Int = this@MultiPagedAdapter.itemCount

    }

    override fun getItemViewType(position: Int): Int {
        return multiHelper.getItemViewType(position)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        return multiHelper.onCreateViewHolder(parent, viewType)
    }

    override fun onBindViewHolder(holder: VH, position: Int, payloads: List<Any?>) {
        multiHelper.onBindViewHolder(holder, position, payloads)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {

    }

}