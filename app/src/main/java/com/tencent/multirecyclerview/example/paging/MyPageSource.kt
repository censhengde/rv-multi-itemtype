package com.tencent.multirecyclerview.example.paging

import androidx.paging.PagingSource
import com.tencent.multirecyclerview.example.bean.ItemBean
import kotlin.coroutines.Continuation

/**
 * Author：岑胜德 on 2021/1/12 16:32
 *
 *
 * 说明：
 */
class MyPageSource : PagingSource<Int, ItemBean>() {


    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ItemBean> {
        val list=ArrayList<ItemBean>()
        return LoadResult.Page<Int,ItemBean>(list,1,2)
    }

}