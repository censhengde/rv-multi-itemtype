package com.tencent.lib.multi

import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.tencent.lib.multi.core.MultiItemManager
import com.tencent.lib.multi.core.ItemType
import java.lang.reflect.Method

/**
 * Author：岑胜德 on 2021/1/6 14:57
 *
 *
 * 说明：未分页的Adapter
 */
@Suppress("UNCHECKED_CAST")
open class MultiAdapter(
        activity: FragmentActivity? = null,
        fragment: Fragment? = null,
        diffCallback: DiffUtil.ItemCallback<*>? = null)
    : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var mAsyncListDiffer: AsyncListDiffer<Any>? = null

    init {
        diffCallback?.let {
            mAsyncListDiffer = AsyncListDiffer(this, diffCallback as DiffUtil.ItemCallback<Any>)
        }
    }

    private val mManager: MultiItemManager = object : MultiItemManager(this, activity, fragment) {
        override fun getItem(position: Int): Any? {
            return this@MultiAdapter.getItem(position)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return mManager.onCreateViewHolder(parent, viewType)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {}
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int, payloads: List<Any>) {
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

    open var dataList: List<Any> = emptyList()
        set(value) {
            field = value
            mAsyncListDiffer?.let {
                it.submitList(field)
                return
            }
            notifyDataSetChanged()
        }
        get() {
            mAsyncListDiffer?.let {
                return it.currentList
            }
            return field
        }
}