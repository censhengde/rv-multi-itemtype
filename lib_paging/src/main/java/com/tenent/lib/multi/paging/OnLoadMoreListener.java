package com.tenent.lib.multi.paging;

/**
 * Author：岑胜德 on 2021/1/29 17:39
 *
 * 说明：
 */
public interface OnLoadMoreListener {

    void onSuccess();

    void onNomore();//没有更多数据了

    void onFailure();

}
