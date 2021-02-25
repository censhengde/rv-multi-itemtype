package com.tencent.multirecyclerview.example.datasource

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.tencent.lib.http.HttpClient
import com.tencent.multirecyclerview.example.bean.PagedBean
import java.util.*

/**

 * Author：岑胜德 on 2021/1/29 17:17

 * 说明：

 */
class MyPagedSource : PagingSource<Int, PagedBean>() {
    val url = "http://web-dev.comic.douba.cn/api/v1.6.0/HomePage/getHomePageData"
    val commonHeader: MutableMap<String, String> = HashMap()

    init {
        commonHeader["Accept-Language"] = "zh_TW"
        commonHeader["Network"] = "Wi-Fi"
        commonHeader["Access-Token"] = "451a3681323640e785cef29bbba4c79c"
        commonHeader["Device-Id"] = "865124036264457"
        commonHeader["Time-Zone"] = "UTC +08:00"
        commonHeader["b-imei"] = "865124036264457"
        commonHeader["W-U-C"] = "2"
        commonHeader["User-Agent"] = "WeComics/1.6.0.3-dev"
        commonHeader["Resolution"] = "1080*1920"
        commonHeader["Version"] = "1.6.0.3-dev"
        commonHeader["Device-Model"] = "OnePlus ONEPLUS A5000"
        commonHeader["System-Version"] = "9"
        commonHeader["System-Name"] = "android"
        commonHeader["Terminal"] = "4"
        commonHeader["Stream-Type"] = "text2"
        commonHeader["Channel"] = "GOOGLE"
        commonHeader["SC"] = "f2ea59c35794f7109b711384c61966c2"
        HttpClient.setCommonHeader(commonHeader)
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, PagedBean> {
        val pageNum = params.key ?: 1
        val result = HttpClient
                .get(url)
                .addParam("lan", "zh_TW")
                .addParam("page", pageNum)
                .addParam("last_module_id", 24)
                .addParam("last_module_pos", 8)
                .addParam("cache_placeholder", 0)
                .execute()
        val list = result.parseArray(PagedBean::class.java)
                ?: return LoadResult.Error(IllegalStateException("  "))
        val nextKey = null

        return LoadResult.Page(list, null, nextKey);
    }

    override fun getRefreshKey(state: PagingState<Int, PagedBean>): Int? {
        return 0
    }
}