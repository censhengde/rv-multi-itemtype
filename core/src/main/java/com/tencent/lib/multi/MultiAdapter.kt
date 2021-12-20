package com.tencent.lib.multi

import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.tencent.lib.multi.core.MultiHelper
import com.tencent.lib.multi.core.ItemType
import java.lang.reflect.Method

/**
 * Author：岑胜德 on 2021/1/6 14:57
 *
 *
 * 说明：未分页的Adapter
 */
open class MultiAdapter(
        activity: FragmentActivity? = null,
        fragment: Fragment? = null,
        shareMethodCachePool: Map<String, Method>? = null,
        private val diffCallback: DiffUtil.ItemCallback<Any>? = null)
    : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val mAsyncListDiffer: AsyncListDiffer<Any>?
        get() {
            return AsyncListDiffer(this, diffCallback ?: return null)
        }
    private val delegate: MultiHelper = object : MultiHelper(this, activity, fragment, shareMethodCachePool) {
        override fun getItem(position: Int): Any? {
            return this@MultiAdapter.getItem(position)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return delegate.onCreateViewHolder(parent, viewType)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {}
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int, payloads: List<Any>) {
        delegate.onBindViewHolder(holder, position, payloads)
    }

    override fun onViewRecycled(holder: RecyclerView.ViewHolder) {
        delegate.onViewRecycled(holder)
    }

    override fun getItemViewType(position: Int): Int {
        return delegate.getItemViewType(position)
    }

    override fun getItemId(position: Int): Long {
        return delegate.getItemId(position)
    }

    override fun onFailedToRecycleView(holder: RecyclerView.ViewHolder): Boolean {
        return delegate.onFailedToRecycleView(holder)
    }

    override fun onViewAttachedToWindow(holder: RecyclerView.ViewHolder) {
        delegate.onViewAttachedToWindow(holder)
    }

    override fun onViewDetachedFromWindow(holder: RecyclerView.ViewHolder) {
        delegate.onViewAttachedToWindow(holder)
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        delegate.onAttachedToRecyclerView(recyclerView)
    }

    override fun onDetachedFromRecyclerView(recyclerView: RecyclerView) {
        delegate.onDetachedFromRecyclerView(recyclerView)
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
        delegate.addItemType(type)
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