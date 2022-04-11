package com.tencent.lib.multi

import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.tencent.lib.multi.core.ItemType
import com.tencent.lib.multi.core.ItemTypeManager

/**

 * Author：岑胜德 on 2021/4/25 19:08

 * 说明：

 */
@Suppress("UNCHECKED_CAST")
open class MultiPagingDataAdapter(
    diffCallback: DiffUtil.ItemCallback<*>,
    initialCapacity: Int = 0
) : PagingDataAdapter<Any, RecyclerView.ViewHolder>(diffCallback as DiffUtil.ItemCallback<Any>) {

    private val mDelegate = object : ItemTypeManager(this, initialCapacity) {
        override fun getItem(position: Int): Any? {
            return this@MultiPagingDataAdapter.getItem(position)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return mDelegate.getItemViewType(position)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return mDelegate.onCreateViewHolder(parent, viewType)
    }

    override fun onBindViewHolder(
        holder: RecyclerView.ViewHolder,
        position: Int,
        payloads: List<Any>
    ) {
        mDelegate.onBindViewHolder(holder, position, payloads)
    }

    final override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

    }

    override fun onViewRecycled(holder: RecyclerView.ViewHolder) {
        mDelegate.onViewRecycled(holder)
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        mDelegate.onAttachedToRecyclerView(recyclerView)
    }

    override fun onDetachedFromRecyclerView(recyclerView: RecyclerView) {
        mDelegate.onDetachedFromRecyclerView(recyclerView)
    }

    override fun onViewAttachedToWindow(holder: RecyclerView.ViewHolder) {
        mDelegate.onViewAttachedToWindow(holder)
    }

    override fun onViewDetachedFromWindow(holder: RecyclerView.ViewHolder) {
        mDelegate.onViewDetachedFromWindow(holder)
    }

    override fun onFailedToRecycleView(holder: RecyclerView.ViewHolder): Boolean {
        return mDelegate.onFailedToRecycleView(holder)
    }

    fun addItemType(itemType: ItemType<*, *>): MultiPagingDataAdapter {
        mDelegate.addItemType(itemType)
        return this
    }

}