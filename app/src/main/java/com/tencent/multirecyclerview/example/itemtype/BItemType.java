package com.tencent.multirecyclerview.example.itemtype;

import android.util.Log;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.tencent.lib.multi.core.MultiHelper;
import com.tencent.lib.multi.core.SimpleItemType;
import com.tencent.multirecyclerview.R;
import com.tencent.multirecyclerview.example.bean.ItemBean;
import com.tencent.lib.multi.core.ItemType;
import com.tencent.lib.multi.core.MultiViewHolder;

/**
 * Author：岑胜德 on 2021/1/6 18:04
 * <p>
 * 说明：
 */
public class BItemType extends SimpleItemType<ItemBean> {

    @Override
    public int getViewType() {
        return ItemBean.TYPE_B;
    }

    @Override
    public boolean matchItemType(ItemBean data, int position) {

        return getViewType()==data.viewType;
    }

    @Override
    public int getItemLayoutRes() {
        return R.layout.item_b;
    }

    @Override
    public void onBindViewHolder(@NonNull MultiViewHolder holder, @NonNull MultiHelper<ItemBean> helper, int position) {

    }


}
