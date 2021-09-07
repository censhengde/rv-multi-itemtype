package com.tencent.multiadapter.example.itemtype;

import android.annotation.SuppressLint;
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
public class CItemType extends MultiItemType<ItemBean> {

    @Override
    public boolean matchItemType(Object bean, int position) {
        return ItemBean.TYPE_C == ((ItemBean)bean).viewType;
    }

    @Override
    public int getItemLayoutRes() {
        return R.layout.item_c;
    }

    @Override
    public void onViewHolderCreated(@NonNull MultiViewHolder holder,
            @NonNull MultiHelper<ItemBean, MultiViewHolder> helper) {
        registerItemViewLongClickListener(holder, helper, "onLongClickItemChildView", R.id.iv_c);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull MultiViewHolder holder,
            @NonNull ItemBean bean, int position,
            @NonNull @NotNull List<Object> payloads) {
        Log.e("===>", " C 类Item 局部刷新：" + position);
        for (Object payload : payloads) {
            if (payload instanceof Bundle) {
                Bundle bundle = (Bundle) payload;
                TextView tv = holder.getView(R.id.tv_c);
                tv.setText(bundle.getString("content"));
            }
        }
    }

    @Override
    public void onBindViewHolder(@NonNull MultiViewHolder holder,
            @NonNull ItemBean bean, int position) {

        Log.e("===>", " C类 Item 级别刷新：" + position);

        TextView tvC = holder.getView(R.id.tv_c);
        tvC.setText(bean.text);
    }

    @SuppressLint("LongLogTag")
    @Override
    public void onViewRecycled(MultiViewHolder holder) {
        Log.e("===> CItemType onViewRecycled",""+holder.getItemViewType());
    }

}
