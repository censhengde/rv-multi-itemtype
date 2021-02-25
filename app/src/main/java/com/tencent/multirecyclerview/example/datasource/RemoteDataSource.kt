package com.tencent.multirecyclerview.example.datasource

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import com.tencent.multirecyclerview.example.bean.PagedBean

/**

 * Author：岑胜德 on 2021/2/22 20:35

 * 说明：

 */
@ExperimentalPagingApi
class RemoteDataSource : RemoteMediator<Int, PagedBean>() {
    override suspend fun load(loadType: LoadType, state: PagingState<Int, PagedBean>): MediatorResult {
        return MediatorResult.Success(true)
    }
}