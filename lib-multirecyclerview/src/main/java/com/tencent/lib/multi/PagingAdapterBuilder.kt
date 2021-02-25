package com.tencent.lib.multi

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.paging.*
import androidx.recyclerview.widget.DiffUtil
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

/**
 * Author：岑胜德 on 2021/1/29 15:41
 *
 * 说明：实现分页的Builder
 */
class PagingAdapterBuilder(val rv: PagedRecyclerView) : AdapterBuilder<PagingAdapterBuilder>() {

    private var diffItemCallback: DiffUtil.ItemCallback<*>? = null
    private lateinit var dataSource: PagingSource<*,*>
    private var pagingConfig: PagingConfig? = null
    private lateinit var pagedAdapter: MultiPagingAdapter<*>


    fun setDiffCallback(callback: DiffUtil.ItemCallback<out Any>): PagingAdapterBuilder {
        diffItemCallback = callback
        return this
    }

    fun setDataSource(dataSource: PagingSource<*, *>): PagingAdapterBuilder {
        this.dataSource = dataSource
        return this
    }

    fun setPagingConfig(pagingConfig: PagingConfig): PagingAdapterBuilder {
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