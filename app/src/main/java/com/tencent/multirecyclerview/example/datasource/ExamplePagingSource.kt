package com.tencent.multirecyclerview.example.datasource

import androidx.paging.PagingSource
import com.tencent.multirecyclerview.example.bean.ItemBean
import java.lang.IllegalStateException

/**

 * Author：岑胜德 on 2021/1/29 17:17

 * 说明：

 */
class ExamplePagingSource: PagingSource<Int, ItemBean>() {
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ItemBean> {
        val list= listOf<ItemBean>()

        LoadResult.Error<Int,ItemBean>(IllegalStateException("  "))

        return LoadResult.Page(list,null,2);
    }
}