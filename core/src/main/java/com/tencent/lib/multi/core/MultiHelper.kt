package com.tencent.lib.multi.core

import android.util.SparseArray
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import java.lang.ref.WeakReference

/**
 * Author：岑胜德 on 2021/1/27 16:33
 *
 *
 * 说明：实现Item多样式的公共逻辑封装。本质上是Adapter 生命周期的代理类，
 * 将 Adapter 生命周期分发给了position对应的ItemType。
 */
abstract class MultiHelper<T, VH : RecyclerView.ViewHolder?>(private val activity: FragmentActivity? = null, private val fragment: Fragment? = null) {
    /**
     * MultiItem 集合.
     */
    private val mItemTypePool = SparseArray<MultiItem<Any, RecyclerView.ViewHolder>>()
    private var mActivityRef: WeakReference<FragmentActivity>? = null
    private var mFragmentRef: WeakReference<Fragment>? = null


    fun getItemId(position: Int): Long {
        val type = findCurrentItem(getItem(position), position)
        return type?.getItemId(position) ?: RecyclerView.NO_ID
    }

    fun getItemViewType(position: Int): Int {
        if (position == RecyclerView.NO_POSITION) {
            return RecyclerView.INVALID_TYPE
        }
        val data = getItem(position)
        val currentItem = findCurrentItem(data, position)
        return currentItem?.itemType ?: RecyclerView.INVALID_TYPE
    }

    /**
     * 遍历查找当前position对应的ItemType。
     *
     * @param data
     * @param position
     * @return
     */
    private fun findCurrentItem(data: T?, position: Int): MultiItem<*, *>? {
        //为当前position 匹配它的ItemType
        for (i in 0 until mItemTypePool.size()) {
            val item = mItemTypePool.valueAt(i)
            if (item.isMatchForMe(data, position)) {
                return item
            }
        }
        return null
    }

    fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val item = mItemTypePool[viewType]
        if (viewType == RecyclerView.INVALID_TYPE || item == null) { //表示无效
            /*一般由于ItemType matchItemTYpe方法实现错误引起的异常*/
            throw RuntimeException("")
        }
        val holder = item.onCreateViewHolder(parent)
        item.onViewHolderCreated(holder, this as MultiHelper<Any, RecyclerView.ViewHolder>)
        return holder as VH
    }

    fun onBindViewHolder(holder: VH,
                         position: Int,
                         payloads: List<Any?>) {
        if (position == RecyclerView.NO_POSITION) {
            return
        }
        /*统一捕获由position引发的可能异常*/
        val currentItem = mItemTypePool[holder!!.itemViewType]
        val bean = getItem(position)
        if (bean == null || currentItem == null) {
            return
        }
        currentItem.onBindViewHolder(holder, bean, position, payloads)
    }

    fun onViewRecycled(holder: VH) {
        val item = mItemTypePool[holder!!.itemViewType]
        item?.onViewRecycled(holder)
    }

    fun onFailedToRecycleView(holder: VH): Boolean {
        val item = mItemTypePool[holder!!.itemViewType]
        return item != null && item.onFailedToRecycleView(holder)
    }

    fun onViewAttachedToWindow(holder: VH) {
        val item = mItemTypePool[holder!!.itemViewType]
        item?.onViewAttachedToWindow(holder)
    }

    fun onViewDetachedFromWindow(holder: VH) {
        val item = mItemTypePool[holder!!.itemViewType]
        item?.onViewDetachedFromWindow(holder)
    }

    fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        for (i in 0 until mItemTypePool.size()) {
            mItemTypePool.valueAt(i).onAttachedToRecyclerView(recyclerView)
        }
    }

    /**
     * Called by RecyclerView when it stops observing this Adapter.
     *
     * @param recyclerView The RecyclerView instance which stopped observing this adapter.
     * @see .onAttachedToRecyclerView
     */
    fun onDetachedFromRecyclerView(recyclerView: RecyclerView) {
        for (i in 0 until mItemTypePool.size()) {
            mItemTypePool.valueAt(i).onDetachedFromRecyclerView(recyclerView)
        }
    }

    abstract fun getItem(position: Int): T?

    /**
     * 添加 MultiItem
     *
     * @param item
     */
    fun addMultiItem(item: MultiItem<*, *>?) {
        if (item == null) {
            return
        }
        // 关联 Activity、Fragment。
        item.onAttach(if (mActivityRef != null) mActivityRef!!.get() else null,
                if (mFragmentRef != null) mFragmentRef!!.get() else null)
        mItemTypePool.put(item.itemType, item as MultiItem<Any, RecyclerView.ViewHolder>)
    }
}