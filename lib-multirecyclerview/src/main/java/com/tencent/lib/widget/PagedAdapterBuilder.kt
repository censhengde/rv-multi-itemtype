package com.tencent.lib.widget

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingSource
import androidx.paging.cachedIn
import androidx.recyclerview.widget.DiffUtil
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

/**
 * Author：岑胜德 on 2021/1/29 15:41
 *
 * 说明：实现分页的Builder
 */
@SuppressWarnings("unchecked all")
class PagedAdapterBuilder(val rv: PagedRecyclerView) : AdapterBuilder<PagedAdapterBuilder>() {

    private var diffItemCallback: DiffUtil.ItemCallback<*>? = null
    private lateinit var dataSource: PagingSource< Any,  Any>
    private var pagingConfig: PagingConfig? = null
    private lateinit var pagedAdapter: MultiPagingAdapter<*>


    fun setDiffCallback(callback: DiffUtil.ItemCallback<out Any>): PagedAdapterBuilder {
        diffItemCallback = callback
        return this
    }

    fun setDataSource(dataSource: PagingSource<Any, Any>): PagedAdapterBuilder {
        this.dataSource = dataSource
        return this
    }

    fun setPagingConfig(pagingConfig: PagingConfig): PagedAdapterBuilder {
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
                dataSource
            }.flow.cachedIn(owner.lifecycleScope)
        }
        diffItemCallback?.let {
            pagedAdapter = if (checkable) {
                CheckedPagedAdapter(it as DiffUtil.ItemCallback<out Checkable>)
            } else {
                MultiPagingAdapter(it)
            }
        }
        //启动协程
        owner.lifecycleScope.launch {
            flow?.collectLatest { data ->
                pagedAdapter.submitData(data as Nothing)
            }

        }
    }

}