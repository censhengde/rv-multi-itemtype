package com.tencent.lib.multi

import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.tencent.lib.multi.core.MultiHelper
import com.tencent.lib.multi.core.MultiItem
import com.tencent.lib.multi.core.checking.CheckingHelper
import kotlin.collections.emptyList as emptyList1

/**
 * Author：岑胜德 on 2021/1/6 14:57
 *
 *
 * 说明：未分页的Adapter
 */
open class MultiAdapter (
        activity: FragmentActivity? = null,
        fragment: Fragment? = null,
        private val diffCallback: DiffUtil.ItemCallback<Any>? = null)
    : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val mAsyncListDiffer: AsyncListDiffer<Any>?
        get() {
            return AsyncListDiffer(this, diffCallback ?: return null)
        }
    private val multiHelper: MultiHelper= object : MultiHelper(this,activity, fragment) {
        override fun getItem(position: Int): Any? {
            return this@MultiAdapter.getItem(position)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return multiHelper.onCreateViewHolder(parent, viewType) as RecyclerView.ViewHolder
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {}
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int, payloads: List<Any>) {
        multiHelper.onBindViewHolder(holder, position, payloads)
    }

    override fun onViewRecycled(holder: RecyclerView.ViewHolder) {
        multiHelper.onViewRecycled(holder)
    }

    override fun getItemViewType(position: Int): Int {
        return multiHelper.getItemViewType(position)
    }

    override fun getItemId(position: Int): Long {
        return multiHelper.getItemId(position)
    }

    override fun onFailedToRecycleView(holder: RecyclerView.ViewHolder): Boolean {
        return multiHelper.onFailedToRecycleView(holder)
    }

    override fun onViewAttachedToWindow(holder: RecyclerView.ViewHolder) {
        multiHelper.onViewAttachedToWindow(holder)
    }

    override fun onViewDetachedFromWindow(holder: RecyclerView.ViewHolder) {
        multiHelper.onViewAttachedToWindow(holder)
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        multiHelper.onAttachedToRecyclerView(recyclerView)
    }

    override fun onDetachedFromRecyclerView(recyclerView: RecyclerView) {
        multiHelper.onDetachedFromRecyclerView(recyclerView)
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

    fun addItemType(type: MultiItem<*, *>): MultiAdapter {
        multiHelper.addMultiItem(type)
        return this
    }

    open var dataList: List<Any> = kotlin.collections.emptyList()
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