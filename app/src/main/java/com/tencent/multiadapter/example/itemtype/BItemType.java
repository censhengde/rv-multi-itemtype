package com.tencent.multiadapter.example.itemtype;

import android.widget.TextView;
import androidx.annotation.NonNull;
import com.tencent.lib.multi.core.MultiHelper;
import com.tencent.lib.multi.core.MultiViewHolder;
import com.tencent.lib.multi.core.SimpleItemType;
import com.tencent.multiadapter.R;
import com.tencent.multiadapter.example.bean.ItemBean;

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
    public void onViewHolderCreated(@NonNull MultiViewHolder holder, @NonNull MultiHelper<ItemBean> helper) {
        /*注册监听器*/
        registClickItemChildViewListener(R.id.btn_b, holder, helper);

    }


    @Override
    public void onBindViewHolder(@NonNull MultiViewHolder holder, @NonNull MultiHelper<ItemBean> helper, int position) {
        ItemBean data = helper.getItem(position);
        if (data != null) {
            TextView tv = holder.getView(R.id.tv_b);
            tv.setText(data.text);
        }
    }


}
