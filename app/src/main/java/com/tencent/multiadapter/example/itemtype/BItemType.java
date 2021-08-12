package com.tencent.multiadapter.example.itemtype;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import androidx.annotation.NonNull;
import com.tencent.lib.multi.core.MultiHelper;
import com.tencent.lib.multi.core.MultiItemType;
import com.tencent.lib.multi.core.MultiViewHolder;
import com.tencent.multiadapter.R;
import com.tencent.multiadapter.example.bean.ItemBean;
import java.util.List;
import org.jetbrains.annotations.NotNull;

/**
 * Author：岑胜德 on 2021/1/6 18:04
 * <p>
 * 说明：
 */
public class BItemType extends MultiItemType<ItemBean> {


    @Override
    public boolean matchItemType(ItemBean data, int position) {
        return ItemBean.TYPE_B==data.viewType;
    }

    @Override
    public int getItemLayoutRes() {
        return R.layout.item_b;
    }

    @Override
    public void onViewHolderCreated(@NonNull MultiViewHolder holder, @NonNull MultiHelper<ItemBean,MultiViewHolder> helper) {
        /*注册监听器，不传viewId则默认是给item根布局注册监听*/
        registerItemViewLongClickListener(holder,helper,"onLongClickItem");
        registerItemViewClickListener(holder,helper,"onClickItemChildView",R.id.btn_b,R.id.tv_b);

    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull MultiViewHolder holder,
            @NonNull @NotNull MultiHelper<ItemBean, MultiViewHolder> helper, int position,
            @NonNull @NotNull List<Object> payloads) {
        Log.e("===>", " B 类Item 局部刷新：" + position);
        for (Object payload : payloads) {
            if (payload instanceof Bundle){
                Bundle bundle= (Bundle) payload;
                TextView tv = holder.getView(R.id.tv_b);
                tv.setText(bundle.getString("content"));
            }
        }
    }

    @Override
    public void onBindViewHolder(@NonNull MultiViewHolder holder, @NonNull MultiHelper<ItemBean,MultiViewHolder> helper, int position) {
        Log.e("===>", " B类 Item 级别刷新：" + position);
        ItemBean data = helper.getItem(position);
        if (data != null) {
            TextView tv = holder.getView(R.id.tv_b);
            tv.setText(data.text);
        }
    }


}
