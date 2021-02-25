package com.tencent.lib.multi;

import java.util.List;

/**
 * Author：岑胜德 on 2021/2/5 16:11
 *
 * 说明：普通的、不分页的AdapterBuilder
 */
public class CommonAdapterBuilder extends AdapterBuilder<CommonAdapterBuilder> {

    private List<?> datas;
    private BaseRecyclerView recyclerView;
    private MultiAdapter adapter;

    public CommonAdapterBuilder setDatas(List<?> datas) {
        this.datas = datas;
        return this;
    }

    CommonAdapterBuilder(BaseRecyclerView rv) {
        recyclerView = rv;
    }

    public void build() {
        ParamUtils.assertNull(datas, "datas 不允许 为 null");
        recyclerView.setCheckable(checkable);
        adapter = new MultiAdapter<>();
        adapter.setOnCompletedCheckItemCallback(onCompletedCheckItemCallback);
        adapter.setSingleSelection(isSingleSelection);
        adapter.checkable(checkable);
        adapter.setDatas(datas);
        if (itemType != null) {
            adapter.setItemType(itemType);
        }
        if (itemTypes != null) {
            adapter.setItemTypes(itemTypes);
        }
        recyclerView.setAdapter(adapter);
    }


}
