package com.tenent.lib.multi.paging

import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.tencent.lib.multi.core.ItemType
import com.tencent.lib.multi.core.MultiHelper
import com.tencent.lib.multi.core.checking.CheckingHelper

/**

 * Author：岑胜德 on 2021/4/25 19:08

 * 说明：

 */
open class MultiPagingDataAdapter<T : Any, VH : RecyclerView.ViewHolder>(diffCallback: DiffUtil.ItemCallback<T>)
    : PagingDataAdapter<T, VH>(diffCallback) {

    private val mDelegate = object : MultiHelper<T, VH>() {

        override fun getItem(p0: Int): T? {
            return this@MultiPagingDataAdapter.getItem(p0)
        }

    }
    val checkingHelper = object : CheckingHelper<T>(this) {
        override fun getItem(position: Int): T? = this@MultiPagingDataAdapter.getItem(position)
        override fun getDataSize(): Int = this@MultiPagingDataAdapter.itemCount

    }


    override fun getItemViewType(position: Int): Int {
        return mDelegate.getItemViewType(position)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        return mDelegate.onCreateViewHolder(parent, viewType)
    }

    override fun onBindViewHolder(holder: VH, position: Int, payloads: List<Any?>) {
        mDelegate.onBindViewHolder(holder, position, payloads)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {

    }

    override fun onViewRecycled(holder: VH) {
        mDelegate.onViewRecycled(holder)
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        mDelegate.onAttachedToRecyclerView(recyclerView)
    }

    override fun onDetachedFromRecyclerView(recyclerView: RecyclerView) {
        mDelegate.onDetachedFromRecyclerView(recyclerView)
    }

    override fun onViewAttachedToWindow(holder: VH) {
        mDelegate.onViewAttachedToWindow(holder)
    }

    override fun onViewDetachedFromWindow(holder: VH) {
        mDelegate.onViewDetachedFromWindow(holder)
    }

    override fun onFailedToRecycleView(holder: VH): Boolean {
        return mDelegate.onFailedToRecycleView(holder)
    }

    fun addItemType(type: ItemType<*, *>): MultiPagingDataAdapter<T, VH> {
        mDelegate.addItemType(type)
        return this
    }


}