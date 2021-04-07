package com.tenent.lib.multi.paging

import android.content.Context
import android.util.AttributeSet
import androidx.paging.PagingConfig
import androidx.paging.PagingSource
import com.tencent.lib.multi.R
import com.tencent.lib.multi.core.BaseRecyclerView

/**

 * Author：岑胜德 on 2021/2/18 10:41

 * 说明：分页的RecyclerView，针对Page3框架进行封装

 */
open class PagingRecyclerView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : BaseRecyclerView(context, attrs, defStyleAttr) {

    val pagingConfig: PagingConfig
    private lateinit var pagingManager: PagingManager

    init {
        val a = context.obtainStyledAttributes(attrs, R.styleable.PagingRecyclerView)
        val pageSize = a.getInt(R.styleable.PagingRecyclerView_pageSize, 0)
        val prefetchDistance = a.getInt(R.styleable.PagingRecyclerView_prefetchDistance, pageSize)
        val initialLoadSize = a.getInt(R.styleable.PagingRecyclerView_initialLoadSize, pageSize*3)
        val maxSize = a.getInt(R.styleable.PagingRecyclerView_maxSize, PagingConfig.MAX_SIZE_UNBOUNDED)
        val jumpThreshold = a.getInt(R.styleable.PagingRecyclerView_jumpThreshold, PagingSource.LoadResult.Page.COUNT_UNDEFINED)
        val enablePlaceholders = a.getBoolean(R.styleable.PagingRecyclerView_enablePlaceholders, true)
        a.recycle()
        pagingConfig = PagingConfig(pageSize, prefetchDistance, enablePlaceholders, initialLoadSize, maxSize, jumpThreshold)
    }

    fun getPagingManager() = pagingManager


    override fun setAdapter(adapter: Adapter<*>?) {
        if (adapter is PagingManager){
            pagingManager=adapter
        }
        super.setAdapter(adapter)
    }
}