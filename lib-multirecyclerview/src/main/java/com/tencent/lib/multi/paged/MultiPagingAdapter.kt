package com.tencent.lib.multi.paged

import android.os.Bundle
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.paging.*
import androidx.recyclerview.widget.DiffUtil
import com.tencent.lib.multi.core.OnCompletedCheckItemCallback
import com.tencent.lib.multi.core.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.util.*

/**

 * Author：岑胜德 on 2021/1/27 15:51

 * 说明：

 */
 open class MultiPagingAdapter<T : Any>(diffCallback: DiffUtil.ItemCallback<T>) : PagingDataAdapter<T, MultiViewHolder>(diffCallback), PagingManager , CheckManager, ItemManager<T> {
    internal var delegateAdapter: DelegateAdapter<T>
    internal var onCompletedCheckedCallback: OnCompletedCheckItemCallback<T>? = null
    init {
        this.delegateAdapter = object : DelegateAdapter<T>(this){
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

class Builder(val rv: PagingRecyclerView): AdapterBuilder<Builder>() {
    private var diffItemCallback: DiffUtil.ItemCallback<*>? = null
    private lateinit var dataSource: PagingSource<*, *>
    private var pagingConfig: PagingConfig? = null
    private lateinit var pagedAdapter: MultiPagingAdapter<*>


    fun setDiffCallback(callback: DiffUtil.ItemCallback<out Any>): Builder {
        diffItemCallback = callback
        return this
    }

    fun setDataSource(dataSource: PagingSource<*, *>): Builder {
        this.dataSource = dataSource
        return this
    }

    fun setPagingConfig(pagingConfig: PagingConfig): Builder {
        this.pagingConfig = pagingConfig
        return this
    }

    fun build(owner: LifecycleOwner) {
        if (pagingConfig == null) {
            pagingConfig = rv.pagingConfig
        }

        var flow: Flow<*>? = null
        pagingConfig?.let {
            flow = Pager(it) {
                dataSource as PagingSource<Any,Any>
            }.flow.cachedIn(owner.lifecycleScope)
        }
        diffItemCallback?.let {
            pagedAdapter = MultiPagingAdapter(it)
            itemType?.let {
                pagedAdapter.setItemType(it as Nothing)
            }
            itemTypes?.let {
                pagedAdapter.setItemTypes(it as Nothing)
            }
            pagedAdapter.checkable(this.checkable)
            pagedAdapter.setOnCompletedCheckItemCallback(this.onCompletedCheckItemCallback as Nothing)
        }
        //启动协程
        owner.lifecycleScope.launch {
            flow?.collectLatest { data ->
                pagedAdapter.submitData(data as Nothing)
            }

        }
    }

}

    /*有待开发*/
    override fun removeItem(position: Int) {

    }

    override fun addItem(position: Int, data: T) {
    }

    override fun addItem(data: T) {
    }

}