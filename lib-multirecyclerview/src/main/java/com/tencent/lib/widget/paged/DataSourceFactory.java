package com.tencent.lib.widget.paged;

import androidx.annotation.NonNull;
import androidx.paging.DataSource;

/**
 * Author：岑胜德 on 2021/1/18 20:08
 *
 * 说明：
 */
public interface DataSourceFactory {
    @NonNull
    DataSource create();
}
