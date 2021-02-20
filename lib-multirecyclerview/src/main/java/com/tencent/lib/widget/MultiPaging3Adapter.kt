package com.tencent.lib.widget

import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil

/**

 * Author：岑胜德 on 2021/1/27 15:51

 * 说明：

 */
 internal open class MultiPaging3Adapter<T:Any >(diffCallback: DiffUtil.ItemCallback<T>) : PagingDataAdapter<T, MultiViewHolder>(diffCallback) {
    internal var delegateAdapter: DelegateAdapter<T>
    init {
        this.delegateAdapter = object :DelegateAdapter<T>(){
            override fun getItem(position: Int): T? {
            return this@MultiPaging3Adapter.getItem(position)
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
}