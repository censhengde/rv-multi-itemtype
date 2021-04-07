package com.tencent.multirecyclerview.example.itemtype;

import android.widget.TextView;
import androidx.annotation.NonNull;
import com.tencent.lib.multi.core.MultiHelper;
import com.tencent.lib.multi.core.MultiViewHolder;
import com.tencent.lib.multi.core.SimpleItemType;
import com.tencent.multirecyclerview.R;
import com.tencent.multirecyclerview.example.bean.ItemBean;

/**
 * Author：岑胜德 on 2021/1/6 18:04
 * <p>
 * 说明：
 */
public class AItemType extends SimpleItemType<ItemBean> {

    /**
     * 返回当前item类型的标识
     * @return
     */
    @Override
    public int getViewType() {
        return ItemBean.TYPE_A;
    }

    @Override
    public boolean matchItemType(ItemBean data, int position) {
        return getViewType() == data.viewType;//这句话的含义是：当前位置item想要呈现的item类型是哪一种，
        //以A、B、C三类为例，进行遍历，直到返回true为止。（详见MutilAdapter getItemViewType方法实现）
    }

    @Override
    public int getItemLayoutRes() {
        return R.layout.item_a;
    }

    @Override
    public void onCreateItemView(@NonNull MultiViewHolder holder, @NonNull MultiHelper<ItemBean> helper) {
        super.onCreateItemView(holder, helper);

        registItemSubViewClickListener(R.id.tv_a, holder, helper);
        registItemSubViewLongClickListener(R.id.tv_a, holder, helper);

    }

    /**
     * 绑定数据
     * @param holder
     * @param position
     */

    @Override
    public void onBindViewHolder(@NonNull MultiViewHolder holder, @NonNull MultiHelper<ItemBean> helper, int position) {
        ItemBean itemBean = helper.getItem(position);
        if (itemBean != null) {
            TextView tv = holder.getView(R.id.tv_a);
            tv.setText(itemBean.text);
        }
    }

    /**
     * 设置item子View的监听
     * @param holder
     */

}
