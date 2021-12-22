package com.tencent.multiadapter.example.item;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;

import com.tencent.lib.multi.core.SimpleItemType;
import com.tencent.lib.multi.core.MultiViewHolder;
import com.tencent.multiadapter.databinding.ItemBBinding;
import com.tencent.multiadapter.example.bean.BeanB;

import java.util.List;
import org.jetbrains.annotations.NotNull;

/**
 * Author：岑胜德 on 2021/1/6 18:04
 * <p>
 * 说明：
 */
public class BItemType extends SimpleItemType<BeanB, ItemBBinding> {


    @Override
    public boolean isMatchForMe(Object bean, int position) {

        return bean instanceof BeanB;
    }

    @Override
    public void onViewHolderCreated(@NonNull MultiViewHolder holder, @NonNull ItemBBinding binding) {
        /*注册监听器，不传viewId则默认是给item根布局注册监听*/
        registerLongClickEvent(holder, binding.getRoot(),"onLongClickItem");
        registerClickEvent(holder, binding.btnB, "onClickItemChildView");
        registerClickEvent(holder, binding.tvB, "onClickItemChildView");

    }

    @Override
    public void onBindViewHolder(@NotNull ItemBBinding binding,
            @NotNull BeanB bean, int position,
            @NotNull List<?> payloads) {
        Log.e("===>", " B 类Item 局部刷新：" + position);
        for (Object payload : payloads) {
            if (payload instanceof Bundle) {
                Bundle bundle = (Bundle) payload;
                binding.tvB.setText(bundle.getString("content"));
            }
        }
    }

    @Override
    public void onBindViewHolder(@NonNull ItemBBinding binding, @NonNull BeanB data, int position) {
        Log.e("===>", " B类 Item 级别刷新：" + position);
        binding.tvB.setText(data.text);

    }

    @SuppressLint("LongLogTag")
    @Override
    public void onViewRecycled(MultiViewHolder holder) {
        Log.e("===> BItemType onViewRecycled", "" + holder.getItemViewType());
    }

}
