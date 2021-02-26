package com.tenent.lib.multi.paging;

import java.util.List;

/**
 * Author：岑胜德 on 2021/1/18 21:07
 *
 * 说明：
 */
public interface OnLoadMoreCallback {
    void onResult(List<?> data);

}
