package com.tencent.multiadapter.example.item;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;

import android.view.View;
import android.widget.Toast;
import androidx.annotation.NonNull;

import com.tencent.lib.multi.core.SimpleItemType;
import com.tencent.lib.multi.core.MultiViewHolder;
import com.tencent.lib.multi.core.annotation.BindItemViewClickEvent;
import com.tencent.multiadapter.databinding.ItemBBinding;
import com.tencent.multiadapter.example.bean.BeanA;
import com.tencent.multiadapter.example.bean.BeanB;

import java.util.List;
import org.jetbrains.annotations.NotNull;

/**
 * Author：岑胜德 on 2021/1/6 18:04
 * <p>
 * 说明：
 */
public class BItemType extends SimpleItemType<BeanB, ItemBBinding> {

    public BItemType() {
        // 注入点击事件接收者
        inject(this);
    }
    @Override
    public boolean isMatchForMe(Object bean, int position) {
        return bean instanceof BeanB;
    }

    @Override
    public void onViewHolderCreated(@NonNull MultiViewHolder holder, @NonNull ItemBBinding binding) {
        /*注册 item 点击事件*/
        registerClickEvent(holder, binding.getRoot(),"onClickItem");

    }

    @Override
    public void onBindViewBinding(@NonNull ItemBBinding binding, @NonNull BeanB data, int position) {
        Log.e("===>", " B类 Item 级别刷新：" + position);
        binding.tvB.setText(data.text);

    }

    /**
     * item点击事件
     * 注意 bean 类型，一定要与当前 ItemType 的 bean 类型对应。
     */
    @BindItemViewClickEvent("onClickItem")
    private void onClickItem(View view, BeanB bean, int position) {
        Toast.makeText(view.getContext(), "点击事件："+bean.text, Toast.LENGTH_SHORT).show();
    }

}
