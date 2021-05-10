package com.tenent.lib.multi.paging

import android.os.Bundle
import android.view.ViewGroup
import androidx.paging.*
import androidx.recyclerview.widget.DiffUtil
import com.tencent.lib.multi.core.OnCheckingFinishedCallback
import com.tencent.lib.multi.core.*
import java.util.*

/**

 * Author：岑胜德 on 2021/1/27 15:51

 * 说明：

 */
 open class MultiPagingAdapter<T : Any>(diffCallback: DiffUtil.ItemCallback<T>) : PagingDataAdapter<T, MultiViewHolder>(diffCallback), PagingManager {
    internal var delegateAdapter: MultiHelper<T>
    internal var onCompletedCheckedFinishedCallback: OnCheckingFinishedCallback<T>? = null
    init {
        this.delegateAdapter = object : MultiHelper<T>(this){
            override fun getItem(position: Int): T? {
                return this@MultiPagingAdapter.getItem(position)
            }

            override fun finishChecking(finishedCallback: OnCheckingFinishedCallback<T>?) {
                finishedCallback?.let {
                    /*筛选出被选中的Item*/
                    val checkedItem = ArrayList<T>()
                    this@MultiPagingAdapter.snapshot().items.forEach {
                        if ((it as Checkable).isChecked) {
                            checkedItem.add(it)
                        }
                    }
                    finishedCallback.onCheckingFinished(checkedItem)
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
        delegateAdapter .finishChecking(onCompletedCheckedFinishedCallback)
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

    fun setOnCompletedCheckItemCallback(finishedCallback: OnCheckingFinishedCallback<T>) {
        this.onCompletedCheckedFinishedCallback = finishedCallback
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
        delegateAdapter.setSingleChecking(isSingleSelection)
    }



    /*有待开发*/
     fun removeItem(position: Int) {

    }

     fun addItem(position: Int, data: T) {
    }

     fun addItem(data: T) {
    }

}