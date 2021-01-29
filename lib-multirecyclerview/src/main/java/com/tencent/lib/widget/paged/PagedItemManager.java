package com.tencent.lib.widget.paged;

import com.tencent.lib.widget.ItemManager;

/**
 * Author：岑胜德 on 2021/1/18 20:35
 *
 * 说明：
 */
public interface PagedItemManager<T> extends ItemManager<T> {
     void refresh();

     void loadMore();

}
