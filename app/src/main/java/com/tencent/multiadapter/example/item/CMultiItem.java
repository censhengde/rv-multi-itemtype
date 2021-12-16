package com.tencent.multiadapter.example.item;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;

import com.tencent.lib.multi.core.SimpleMultiItem;
import com.tencent.lib.multi.core.MultiViewHolder;
import com.tencent.multiadapter.databinding.ItemCBinding;
import com.tencent.multiadapter.example.bean.CItemBean;

import java.util.List;
import org.jetbrains.annotations.NotNull;

/**
 * Author：岑胜德 on 2021/1/6 18:04
 * <p>
 * 说明：
 */
public class CMultiItem extends SimpleMultiItem<CItemBean, ItemCBinding> {

    @Override
    public boolean isMatchForMe(Object bean, int position) {
        return  bean instanceof CItemBean;
    }

    @Override
    public void onViewHolderCreated(@NonNull MultiViewHolder holder,
            @NonNull ItemCBinding binding) {
        registerLongClickEvent(holder, binding.ivC,"onLongClickItemChildView");
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ItemCBinding binding,
                                 @NonNull CItemBean bean, int position,
                                 @NonNull @NotNull List<Object> payloads) {
        Log.e("===>", " C 类Item 局部刷新：" + position);
        for (Object payload : payloads) {
            if (payload instanceof Bundle) {
                Bundle bundle = (Bundle) payload;
               binding.tvC.setText(bundle.getString("content"));
            }
        }
    }

    @Override
    public void onBindViewHolder(@NonNull ItemCBinding binding,
                                 @NonNull CItemBean bean, int position) {
        Log.e("===>", " C类 Item 级别刷新：" + position);
        binding.tvC.setText(bean.text);
    }

    @SuppressLint("LongLogTag")
    @Override
    public void onViewRecycled(MultiViewHolder holder) {
        Log.e("===> CItemType onViewRecycled",""+holder.getItemViewType());
    }

}
