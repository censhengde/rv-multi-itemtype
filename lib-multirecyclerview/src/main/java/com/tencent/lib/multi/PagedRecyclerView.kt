package com.tencent.lib.multi

import android.content.Context
import android.util.AttributeSet
import androidx.paging.PagingConfig
import com.tencent.lib.multi.paged.PagingManager

/**

 * Author：岑胜德 on 2021/2/18 10:41

 * 说明：分页的RecyclerView，针对Page3框架进行封装

 */
class PagedRecyclerView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : MultiRecyclerView(context, attrs, defStyleAttr) {

    val pagingConfig: PagingConfig
    private lateinit var pagingManager: PagingManager

    init {
        val a = context.obtainStyledAttributes(attrs, R.styleable.PagedRecyclerView)
        val pageSize = a.getInt(R.styleable.PagedRecyclerView_pageSize, 0)
        val prefetchDistance = a.getInt(R.styleable.PagedRecyclerView_prefetchDistance, 0)
        val initialLoadSize = a.getInt(R.styleable.PagedRecyclerView_initialLoadSize, 0)
        val maxSize = a.getInt(R.styleable.PagedRecyclerView_maxSize, 0)
        val jumpThreshold = a.getInt(R.styleable.PagedRecyclerView_jumpThreshold, 0)
        val enablePlaceholders = a.getBoolean(R.styleable.PagedRecyclerView_enablePlaceholders, false)
        a.recycle()
        pagingConfig = PagingConfig(pageSize, prefetchDistance, enablePlaceholders, initialLoadSize, maxSize, jumpThreshold)
    }

    fun pagedBuilder(): PagingAdapterBuilder {
        return PagingAdapterBuilder(this)
}

    fun getPagedManager() = pagingManager
}