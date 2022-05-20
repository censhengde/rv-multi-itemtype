package com.tencent.lib.multi

import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.tencent.lib.multi.core.ItemManager
import com.tencent.lib.multi.core.ItemType

/**
 * Author：岑胜德 on 2021/1/6 14:57
 *
 *
 * 说明：未分页的Adapter
 */
@Suppress("UNCHECKED_CAST")
open class MultiAdapter(
    diffCallback: DiffUtil.ItemCallback<*>? = null,
    initialCapacity: Int = 0
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var mAsyncListDiffer: AsyncListDiffer<Any>? = null
    private var _dataList: List<Any>? = null
    private val dataList: List<Any>
        get() {
            mAsyncListDiffer?.let {
                return it.currentList
            }
            return _dataList ?: emptyList()
        }

    init {
        diffCallback?.let {
            mAsyncListDiffer = AsyncListDiffer(this, diffCallback as DiffUtil.ItemCallback<Any>)
        }
    }

    private val mManager: ItemManager = object : ItemManager(this, initialCapacity) {
        override fun getItem(position: Int): Any? {
            return this@MultiAdapter.getItem(position)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return mManager.onCreateViewHolder(parent, viewType)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {}
    override fun onBindViewHolder(
        holder: RecyclerView.ViewHolder,
        position: Int,
        payloads: List<Any>
    ) {
        mManager.onBindViewHolder(holder, position, payloads)
    }

    override fun onViewRecycled(holder: RecyclerView.ViewHolder) {
        mManager.onViewRecycled(holder)
    }

    override fun getItemViewType(position: Int): Int {
        return mManager.getItemViewType(position)
    }

    override fun getItemId(position: Int): Long {
        return mManager.getItemId(position)
    }

    override fun onFailedToRecycleView(holder: RecyclerView.ViewHolder): Boolean {
        return mManager.onFailedToRecycleView(holder)
    }

    override fun onViewAttachedToWindow(holder: RecyclerView.ViewHolder) {
        mManager.onViewAttachedToWindow(holder)
    }

    override fun onViewDetachedFromWindow(holder: RecyclerView.ViewHolder) {
        mManager.onViewAttachedToWindow(holder)
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        mManager.onAttachedToRecyclerView(recyclerView)
    }

    override fun onDetachedFromRecyclerView(recyclerView: RecyclerView) {
        mManager.onDetachedFromRecyclerView(recyclerView)
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    fun getItem(position: Int): Any? {
        return if (position in dataList.indices) {
            dataList[position]
        } else {
            null
        }
    }

    fun addItemType(type: ItemType<*, *>): MultiAdapter {
        mManager.addItemType(type)
        return this
    }

    fun clearAllItemTypes() {
        mManager.clearAllItemTypes()
    }

    fun setDataList(list: List<Any>) {
        _dataList = list
    }

    fun submitList(list: List<Any>) {
        mAsyncListDiffer?.submitList(list)
    }

    fun submitList(list: List<Any>, runnable: Runnable) {
        mAsyncListDiffer?.submitList(list, runnable)
    }

}