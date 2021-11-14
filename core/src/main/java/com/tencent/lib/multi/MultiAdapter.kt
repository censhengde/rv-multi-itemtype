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
open class MultiAdapter<T, VH : RecyclerView.ViewHolder>(
        activity: FragmentActivity? = null,
        fragment: Fragment? = null,
        private val diffCallback: DiffUtil.ItemCallback<T>? = null)
    : RecyclerView.Adapter<VH>() {

    private val mAsyncListDiffer: AsyncListDiffer<T>?
        get() {
            return AsyncListDiffer(this, diffCallback ?: return null)
        }
    private val multiHelper: MultiHelper<T, VH> = object : MultiHelper<T, VH>(activity, fragment) {
        override fun getItem(position: Int): T? {
            return this@MultiAdapter.getItem(position)
        }
    }
    val checkingHelper: CheckingHelper<T> = object : CheckingHelper<T>(this) {
        override fun getItem(position: Int): T? {
            return this@MultiAdapter.getItem(position)
        }

        override fun getDataSize(): Int {
            return this@MultiAdapter.itemCount
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        return multiHelper.onCreateViewHolder(parent, viewType)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {}
    override fun onBindViewHolder(holder: VH, position: Int, payloads: List<Any>) {
        multiHelper.onBindViewHolder(holder, position, payloads)
    }

    override fun onViewRecycled(holder: VH) {
        multiHelper.onViewRecycled(holder)
    }

    override fun getItemViewType(position: Int): Int {
        return multiHelper.getItemViewType(position)
    }

    override fun getItemId(position: Int): Long {
        return multiHelper.getItemId(position)
    }

    override fun onFailedToRecycleView(holder: VH): Boolean {
        return multiHelper.onFailedToRecycleView(holder)
    }

    override fun onViewAttachedToWindow(holder: VH) {
        multiHelper.onViewAttachedToWindow(holder)
    }

    override fun onViewDetachedFromWindow(holder: VH) {
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

    fun getItem(position: Int): T? {
        return if (position in dataList.indices) {
            dataList[position]
        } else {
            null
        }
    }

    fun setData(data: List<T>): MultiAdapter<T, VH> {

        return this
    }

//    fun removeItem(position: Int) {
//        val currentList = dataList
//        if (position in currentList.indices) {
//            /*如果删除的Item是被选中的Item，则数量要减一*/
//            val item = currentList[position]
//            if (item is Checkable) {
//                val checkable = item as Checkable
//                if (checkable.isChecked) {
//                    checkingHelper.checkedItemCount = checkingHelper.checkedItemCount - 1
//                }
//            }
//            currentList.removeAt(position)
//            notifyItemRemoved(position)
//            notifyItemRangeChanged(position, currentList.size - position)
//        }
//    }

    fun addItemType(type: MultiItem<*, *>?): MultiAdapter<T, VH> {
        multiHelper.addMultiItem(type)
        return this
    }

    open var dataList: List<T> = kotlin.collections.emptyList()
        set(value) {
            field = value
            if (mAsyncListDiffer != null) {
                mAsyncListDiffer?.submitList(field)
            }
            notifyDataSetChanged()
        }
        get() {
            val currentList = if (mAsyncListDiffer == null) this.dataList else mAsyncListDiffer!!.currentList
            return (currentList ?: emptyList1()) as MutableList<T>
        }
}