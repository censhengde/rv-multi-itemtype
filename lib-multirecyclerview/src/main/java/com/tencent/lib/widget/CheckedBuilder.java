package com.tencent.lib.widget;

import androidx.annotation.Nullable;

/**
 * Author：岑胜德 on 2021/2/5 16:11
 *
 * 说明：实现Item单选的Builder
 */
public class CheckedBuilder extends AdapterBuilder<CheckedBuilder> {
    private OnCheckedItemCallback onCheckedItemCallback;
    private boolean isSingleSelection = false;
    private CheckedAdapter adapter;
    CheckedBuilder(MultiRecyclerView rv) {
        super(rv);
    }

    private OnCompletedCheckedCallback onCompletedCheckedCallback;

    public CheckedBuilder setOnCompletedCheckedCallback(OnCompletedCheckedCallback callback) {
        this.onCompletedCheckedCallback = callback;
        return this;
    }

    public CheckedBuilder setOnCheckedItemCallbak(@Nullable OnCheckedItemCallback<?> callbak) {
        onCheckedItemCallback=callbak;
        return this;
    }
    public CheckedBuilder setSingleSelection(boolean singleSelection) {
        isSingleSelection = singleSelection;
        return this;
    }
    @Override
    public void build() {
        adapter=new CheckedAdapter<>();
        adapter.setSingleSelection(isSingleSelection);

        if (itemType != null) {
            adapter.setItemType(itemType);
        }
        if (itemTypes != null) {
            adapter.setItemTypes(itemTypes);
        }
        recyclerView.setAdapter(adapter);
    }

    @Override
    protected MultiAdapter getAdapter() {
        if (adapter==null){
            adapter=new CheckedAdapter();
        }
        return adapter;
    }
}
