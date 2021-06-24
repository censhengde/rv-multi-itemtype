package com.tencent.multiadapter.example.itemtype;

import android.widget.TextView;
import androidx.annotation.NonNull;
import com.tencent.lib.multi.core.MultiHelper;
import com.tencent.lib.multi.core.MultiItemType;
import com.tencent.lib.multi.core.MultiViewHolder;
import com.tencent.multiadapter.R;
import com.tencent.multiadapter.example.bean.ItemBean;

/**
 * Author：岑胜德 on 2021/1/6 18:04
 * <p>
 * 说明：
 */
public class CItemType extends MultiItemType<ItemBean> {

    @Override
    public int getViewType() {
        return ItemBean.TYPE_C;
    }

    @Override
    public boolean matchItemType(ItemBean data, int position) {
        return getViewType()==data.viewType;
    }

    @Override
    public int getItemLayoutRes() {
        return R.layout.item_c;
    }

    @Override
    public void onViewHolderCreated(@NonNull MultiViewHolder holder, @NonNull MultiHelper<ItemBean,MultiViewHolder> helper) {
        registerItemViewLongClickListener(holder, helper, "onLongClickItemChildView", R.id.iv_c);
    }

    @Override
    public void onBindViewHolder(@NonNull MultiViewHolder holder, @NonNull MultiHelper<ItemBean,MultiViewHolder> helper, int position) {
        ItemBean bean = helper.getItem(position);
        if (bean == null) {
            return;
        }
        TextView tvC = holder.getView(R.id.tv_c);
        tvC.setText(bean.text);
    }


}
