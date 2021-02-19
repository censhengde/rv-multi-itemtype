package com.tencent.lib.widget

import android.content.Context
import android.util.AttributeSet
import androidx.paging.PagingConfig
import androidx.recyclerview.widget.RecyclerView

/**

 * Author：岑胜德 on 2021/2/18 10:41

 * 说明：分页的RecyclerView，针对Page3框架进行封装

 */
class PagedRecyclerView @JvmOverloads constructor(context: Context, attrs: AttributeSet?=null, defStyleAttr: Int=0) : RecyclerView(context, attrs, defStyleAttr) {
    var checkable = false//是否开启列表单选、多选功能
    lateinit var pagingConfig: PagingConfig

    var pageSize=0
    var prefetchDistance=0
    var enablePlaceholders=false
    var initialLoadSize=0
    var maxSize=0
    var jumpThreshold=0



fun pagedBuilder():PagedBuilder{
    return PagedBuilder(this)
}

}