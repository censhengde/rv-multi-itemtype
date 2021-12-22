package com.tencent.multiadapter.example.item;

import android.annotation.SuppressLint;
import android.util.Log;

import androidx.annotation.NonNull;

import com.tencent.lib.multi.core.SimpleItemType;
import com.tencent.lib.multi.core.MultiViewHolder;
import com.tencent.multiadapter.databinding.ItemCBinding;
import com.tencent.multiadapter.example.bean.BeanC;

/**
 * Author：岑胜德 on 2021/1/6 18:04
 * <p>
 * 说明：
 */
public class CItemType extends SimpleItemType<BeanC, ItemCBinding> {

    @Override
    public boolean isMatchForMe(Object bean, int position) {
        return  bean instanceof BeanC;
    }

    @Override
    public void onBindViewBinding(@NonNull ItemCBinding binding,
                                 @NonNull BeanC bean, int position) {
        binding.tvC.setText(bean.text);
    }
}
