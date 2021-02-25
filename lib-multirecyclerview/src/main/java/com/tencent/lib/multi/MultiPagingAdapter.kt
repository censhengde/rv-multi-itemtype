package com.tencent.lib.multi

import android.os.Bundle
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import com.tencent.lib.multi.paged.PagingManager
import java.util.*

/**

 * Author：岑胜德 on 2021/1/27 15:51

 * 说明：

 */
 open class MultiPagingAdapter<T : Any>(diffCallback: DiffUtil.ItemCallback<T>) : PagingDataAdapter<T, MultiViewHolder>(diffCallback), PagingManager ,CheckManager{
    internal var delegateAdapter: DelegateAdapter<T>
    internal var onCompletedCheckedCallback: OnCompletedCheckItemCallback<T>? = null
    init {
        this.delegateAdapter = object :DelegateAdapter<T>(this){
            override fun getItem(position: Int): T? {
                return this@MultiPagingAdapter.getItem(position)
            }

            override fun complete(callback: OnCompletedCheckItemCallback<T>?) {
                callback?.let {
                    /*筛选出被选中的Item*/
                    val checkedItem = ArrayList<T>()
                    this@MultiPagingAdapter.snapshot().items.forEach {
                        if ((it as Checkable).isChecked) {
                            checkedItem.add(it)
                        }
                    }
                    callback.onCompletedChecked(checkedItem)
                }
            }

        }
    }

    override fun getItemViewType(position: Int): Int {
       return delegateAdapter.getItemViewType(position)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MultiViewHolder {

        return delegateAdapter.onCreateViewHolder(parent, viewType)
    }

    override fun onBindViewHolder(holder: MultiViewHolder, position: Int) {
        delegateAdapter.onBindViewHolder(holder, position)
    }

    fun setItemTypes(types: MutableList<ItemType<T>>) {
        delegateAdapter.setItemTypes(types)
    }

    fun setItemType(type: ItemType<T>) {
        delegateAdapter.setItemType(type)
    }

    override fun complete() {
        delegateAdapter .complete(onCompletedCheckedCallback)
    }

    override fun saveCheckedItem(out: Bundle?) {

    }

    override fun restoreCheckedItem(`in`: Bundle?) {
    }

    override fun cancelAll() {
        snapshot().items.forEach {
            if (it is Checkable){
            (it).isChecked = false
            }
        }
        notifyDataSetChanged()
    }

    fun setOnCompletedCheckItemCallback(callback: OnCompletedCheckItemCallback<T>) {
        this.onCompletedCheckedCallback = callback
    }

    override fun checkAll() {
        snapshot().items.forEach {
           if (it is Checkable){
            it.isChecked = true
           }
        }
        notifyDataSetChanged()
    }

     fun checkable(checkable: Boolean) {
        delegateAdapter.checkable = checkable
    }

     fun setSingleSelection(isSingleSelection: Boolean) {
        delegateAdapter.setSingleSelection(isSingleSelection)
    }



}