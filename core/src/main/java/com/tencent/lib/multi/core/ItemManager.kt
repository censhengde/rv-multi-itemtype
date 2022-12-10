package com.tencent.lib.multi.core

import android.util.ArrayMap
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import java.util.ArrayList

/**
 * Author：岑胜德 on 2021/1/27 16:33
 *
 *
 * 说明：实现Item多样式的公共逻辑封装。本质上是Adapter 生命周期的代理类，
 * 将 Adapter 生命周期分发给了position对应的ItemType。
 */
@Suppress("UNCHECKED_CAST")
abstract class ItemManager(
    val adapter: RecyclerView.Adapter<*>,
    initialCapacity: Int = 0
) {
    // ItemType 池.
    private val itemTypePool = ArrayList<ItemType<Any, RecyclerView.ViewHolder>>(initialCapacity)

    internal val cachePool by lazy(LazyThreadSafetyMode.NONE) { ArrayMap<Class<*>, ReceiverWrapper>() }

    fun getItemViewType(position: Int): Int {
        val data = getItem(position)
        return findCurrentItemViewType(data, position)
    }

    fun getItemId(position: Int): Long {
        val data = getItem(position) ?: RecyclerView.NO_ID
        findCurrentItemViewType(data, position).also {
            return itemTypePool[it].getItemId(data, position)
        }
    }


    private fun findCurrentItemViewType(data: Any?, position: Int): Int {
        itemTypePool.forEachIndexed { index, item ->
            if (item.isMatched(data, position)) {
                return index // index 直接作为 itemViewType 返回。
            }
        }
        // 未匹配到对应的 ItemType 则抛异常。
        throw java.lang.RuntimeException("ItemType is not found in position:$position")
    }

    fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return itemTypePool[viewType].onCreateViewHolder(parent)
    }

    fun onBindViewHolder(
        holder: RecyclerView.ViewHolder,
        position: Int,
        payloads: List<Any>
    ) {
        if (position == RecyclerView.NO_POSITION) {
            return
        }
        val bean = getItem(position) ?: return
        val type = itemTypePool[holder.itemViewType]
        type.onBindViewHolder(holder, bean, position, payloads)
    }

    fun onViewRecycled(holder: RecyclerView.ViewHolder) {
        itemTypePool[holder.itemViewType].onViewRecycled(holder)
    }

    fun onFailedToRecycleView(holder: RecyclerView.ViewHolder): Boolean {
        return itemTypePool[holder.itemViewType].onFailedToRecycleView(holder)
    }

    fun onViewAttachedToWindow(holder: RecyclerView.ViewHolder) {
        itemTypePool[holder.itemViewType].onViewAttachedToWindow(holder)
    }

    fun onViewDetachedFromWindow(holder: RecyclerView.ViewHolder) {
        itemTypePool[holder.itemViewType].onViewDetachedFromWindow(holder)
    }

    fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        itemTypePool.forEach {
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
        itemTypePool.forEach {
            it.onDetachedFromRecyclerView(recyclerView)
        }
    }

    abstract fun getItem(position: Int): Any?

    /**
     * 添加 ItemType
     *
     * @param itemType
     */
    @Suppress("UNCHECKED_CAST")
    fun addItemType(itemType: ItemType<*, *>) {
        // 关联
        itemType.onAttach(this)
        itemTypePool.add(itemType as ItemType<Any, RecyclerView.ViewHolder>)
    }

    /**
     * 在 addItemType 方法之前最好清空一遍 itemTypePool，确保 itemTypePool
     * 里一种item类型仅有一个ItemType实例。否则当Activity/Fragment重走创建视图生命周期
     * 会导致 ItemType 实例重复添加。
     */
    fun clearAllItemTypes() {
        itemTypePool.clear()
    }

    fun <T> updateItem(position: Int, block: (bean: T) -> Any?) {
        getItem(position)?.run {
            adapter.notifyItemChanged(position, block(this as T))
        }
    }
}