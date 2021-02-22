package com.tencent.multirecyclerview.example.datasource

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.tencent.lib.http.HttpClient
import com.tencent.multirecyclerview.example.bean.ItemBean

/**

 * Author：岑胜德 on 2021/1/29 17:17

 * 说明：

 */
class MyPagedSource : PagingSource<Int, ItemBean>() {
    val url = "api/v1.6.0/HomePage/getHomePageData"

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ItemBean> {
        val list= listOf<ItemBean>()
        val result=HttpClient.get(url).execute()
        LoadResult.Error<Int,ItemBean>(IllegalStateException("  "))

        return LoadResult.Page(list,null,2);
    }

    override fun getRefreshKey(state: PagingState<Int, ItemBean>): Int? {
        return 0
    }
}