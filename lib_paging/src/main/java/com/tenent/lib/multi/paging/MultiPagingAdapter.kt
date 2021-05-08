package com.tenent.lib.multi.paging

import android.os.Bundle
import android.view.ViewGroup
import androidx.paging.*
import androidx.recyclerview.widget.DiffUtil
import com.tencent.lib.multi.core.OnCompletedCheckCallback
import com.tencent.lib.multi.core.*
import java.util.*

/**

 * Author：岑胜德 on 2021/1/27 15:51

 * 说明：

 */
 open class MultiPagingAdapter<T : Any>(diffCallback: DiffUtil.ItemCallback<T>) : PagingDataAdapter<T, MultiViewHolder>(diffCallback), PagingManager {
    internal var delegateAdapter: MultiHelper<T>
    internal var onCompletedCheckedCallback: OnCompletedCheckCallback<T>? = null
    init {
        this.delegateAdapter = object : MultiHelper<T>(this){
            override fun getItem(position: Int): T? {
                return this@MultiPagingAdapter.getItem(position)
            }

            override fun complete(callback: OnCompletedCheckCallback<T>?) {
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



    fun setItemType(type: ItemType<T>) {
        delegateAdapter.addItemType(type)
    }

     fun complete() {
        delegateAdapter .complete(onCompletedCheckedCallback)
    }

     fun saveCheckedItem(out: Bundle?) {

    }

     fun restoreCheckedItem(`in`: Bundle?) {
    }

     fun cancelAll() {
        snapshot().items.forEach {
            if (it is Checkable){
            (it).isChecked = false
            }
        }
        notifyDataSetChanged()
    }

    fun setOnCompletedCheckItemCallback(callback: OnCompletedCheckCallback<T>) {
        this.onCompletedCheckedCallback = callback
    }

     fun checkAll() {
        snapshot().items.forEach {
           if (it is Checkable){
            it.isChecked = true
           }
        }
        notifyDataSetChanged()
    }



     fun setSingleSelection(isSingleSelection: Boolean) {
        delegateAdapter.setSingleSelection(isSingleSelection)
    }



    /*有待开发*/
     fun removeItem(position: Int) {

    }

     fun addItem(position: Int, data: T) {
    }

     fun addItem(data: T) {
    }

}