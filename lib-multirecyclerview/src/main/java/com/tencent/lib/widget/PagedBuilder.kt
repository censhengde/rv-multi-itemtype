package com.tencent.lib.widget

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingSource
import androidx.paging.cachedIn
import androidx.recyclerview.widget.DiffUtil
import com.tencent.lib.widget.paging3.MultiPaging3Adapter
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

/**
 * Author：岑胜德 on 2021/1/29 15:41
 *
 * 说明：实现分页的Builder
 */
class PagedBuilder(val rv: PagedRecyclerView) {

    private var diffItemCallback: DiffUtil.ItemCallback<in Any>? = null
    private lateinit var dataSource: PagingSource<*, *>
    private var pagingConfig: PagingConfig? = null
    private lateinit var pagedAdapter: MultiPaging3Adapter<in Any>


    fun <T : Any> setDiffCallback(callback: DiffUtil.ItemCallback<T>): PagedBuilder {
        diffItemCallback = callback as DiffUtil.ItemCallback<in Any>
        return this
    }

    fun setDataSource(dataSource: PagingSource<*, *>): PagedBuilder {
        this.dataSource = dataSource
        return this
    }

    fun setPagingConfig(pagingConfig: PagingConfig): PagedBuilder {
        this.pagingConfig = pagingConfig
        return this
    }

    /*重写父类实现*/
//    override fun build() {
//        assertNull(diffItemCallback, "请先调用 setDiffCallback（） ")
//        assertNull(dataSource, "请先调用 setDataSource（） ")
//        assertNull(pagingConfig, "请先调用 setPagingConfig（） ")
//        pagedAdapter = MultiPaging3Adapter(diffItemCallback)
////        if (itemType != null) {
////            pagedAdapter.setItemType(itemType as Nothing)
////        }
////        if (itemTypes != null) {
////            pagedAdapter.setItemTypes(itemTypes as Nothing)
////        }
//        recyclerView.adapter = pagedAdapter
//    }

    fun build(owner: LifecycleOwner) {
        if (pagingConfig == null) {
            pagingConfig = createPagingConfig()
        }

        var flow: Flow<*>? = null
        pagingConfig?.let {
            flow = Pager(it) {
                dataSource as PagingSource<Any, Any>
            }.flow.cachedIn(owner.lifecycleScope)
        }
        diffItemCallback?.let {
            pagedAdapter = MultiPaging3Adapter(it)
        }
        //启动协程
        owner.lifecycleScope.launch {
            flow?.collectLatest { data ->
                pagedAdapter.submitData(data as Nothing)
            }

        }
    }

    private fun createPagingConfig(): PagingConfig {
        val config = PagingConfig(rv.pageSize, rv.prefetchDistance, rv.enablePlaceholders, rv.maxSize, rv.initialLoadSize)
        return config
    }
}