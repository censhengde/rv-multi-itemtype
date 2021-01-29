package com.tencent.lib.widget

import androidx.paging.PagingConfig
import androidx.paging.PagingSource
import androidx.recyclerview.widget.DiffUtil
import com.tencent.lib.widget.paging3.MultiPaging3Adapter

/**
 * Author：岑胜德 on 2021/1/29 15:41
 *
 * 说明：实现分页的Builder
 */
class PagedBuilder internal constructor(rv: MultiRecyclerView) : Builder<PagedBuilder>(rv) {
    private lateinit var diffItemCallback: DiffUtil.ItemCallback<in Any>
    private lateinit var dataSource: PagingSource<*, *>
    private lateinit var pagingConfig: PagingConfig
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
    override fun build() {
        assertNull(diffItemCallback, "请先调用 setDiffCallback（） ")
        assertNull(dataSource, "请先调用 setDataSource（） ")
        assertNull(pagingConfig, "请先调用 setPagingConfig（） ")
        pagedAdapter = MultiPaging3Adapter(diffItemCallback)
        if (itemType != null) {
            pagedAdapter.setItemType(itemType as Nothing)
        }
        if (itemTypes != null) {
            pagedAdapter.setItemTypes(itemTypes as Nothing)
        }
        recyclerView.adapter = pagedAdapter
    }
}