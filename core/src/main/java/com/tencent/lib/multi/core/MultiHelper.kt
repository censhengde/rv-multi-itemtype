package com.tencent.lib.multi.core

import android.util.SparseArray
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import java.util.ArrayList

/**
 * Author：岑胜德 on 2021/1/27 16:33
 *
 *
 * 说明：实现Item多样式的公共逻辑封装。本质上是Adapter 生命周期的代理类，
 * 将 Adapter 生命周期分发给了position对应的ItemType。
 */
abstract class MultiHelper(val adapter: RecyclerView.Adapter<*>,
                           val activity: FragmentActivity? = null,
                           val fragment: Fragment? = null,
                           private val initialCapacity: Int = 0) {
    /**
     * MultiItem 集合.
     */
    private val itemsPool = ArrayList<MultiItem<Any, RecyclerView.ViewHolder>>(initialCapacity)


    fun getItemId(position: Int): Long {
        val type = findCurrentItemViewType(getItem(position), position)
        return itemsPool[type].getItemId(position)
    }

    fun getItemViewType(position: Int): Int {
        val data = getItem(position)
        return findCurrentItemViewType(data, position)
    }

    private fun findCurrentItemViewType(data: Any?, position: Int): Int {
        itemsPool.forEachIndexed { index, item ->
            if (item.isMatchForMe(data, position)) {
                return index
            }
        }
        throw java.lang.RuntimeException("Item view type not found!")
    }

    fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val item = itemsPool[viewType]
        return item.onCreateViewHolder(parent)
    }

    fun onBindViewHolder(holder: RecyclerView.ViewHolder,
                         position: Int,
                         payloads: List<Any?>) {
        if (position == RecyclerView.NO_POSITION) {
            return
        }
        val currentItem = itemsPool[holder.itemViewType]
        val bean = getItem(position) ?: return
        currentItem.onBindViewHolder(holder, bean, position, payloads)
    }

    fun onViewRecycled(holder: RecyclerView.ViewHolder) {
        itemsPool[holder.itemViewType].onViewRecycled(holder)
    }

    fun onFailedToRecycleView(holder: RecyclerView.ViewHolder): Boolean {
        return itemsPool[holder.itemViewType].onFailedToRecycleView(holder)
    }

    fun onViewAttachedToWindow(holder: RecyclerView.ViewHolder) {
        itemsPool[holder.itemViewType].onViewAttachedToWindow(holder)
    }

    fun onViewDetachedFromWindow(holder: RecyclerView.ViewHolder) {
        itemsPool[holder.itemViewType].onViewDetachedFromWindow(holder)
    }

    fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        itemsPool.forEach {
            it.onAttachedToRecyclerView(recyclerView)
        }
    }

    /**
     * Called by RecyclerView when it stops observing this Adapter.
     *
     * @param recyclerView The RecyclerView instance which stopped observing this adapter.
     * @see .onAttachedToRecyclerView
     */
    fun onDetachedFromRecyclerView(recyclerView: RecyclerView) {
        itemsPool.forEach {
            it.onDetachedFromRecyclerView(recyclerView)
        }
    }

    abstract fun getItem(position: Int): Any?

    /**
     * 添加 MultiItem
     *
     * @param item
     */
    @SuppressWarnings("unchecked all")
    fun addMultiItem(item: MultiItem<*, *>) {
        if (itemsPool.contains(item)) {
            return
        }
        // 关联
        item.onAttach(this)
        itemsPool.add(item as MultiItem<Any, RecyclerView.ViewHolder>)
    }
}