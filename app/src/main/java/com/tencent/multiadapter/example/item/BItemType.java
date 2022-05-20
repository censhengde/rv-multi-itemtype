package com.tencent.multiadapter.example.item;

import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.tencent.lib.multi.core.MultiViewHolder;
import com.tencent.lib.multi.core.SimpleItemType;
import com.tencent.multiadapter.databinding.ItemBBinding;
import com.tencent.multiadapter.example.bean.BeanB;

/**
 * Author：岑胜德 on 2021/1/6 18:04
 * <p>
 * 说明：
 */
public class BItemType extends SimpleItemType<BeanB, ItemBBinding> {

    public BItemType() {
        bind(this);
    }

    @Override
    public boolean isMatched(Object bean, int position) {
        return bean instanceof BeanB;
    }


    @Override
    public void onBindView(@NonNull ItemBBinding binding, @NonNull BeanB data, int position) {
        binding.tvB.setText(data.text);

    }

    /**
     * item点击事件
     * 注意 bean 类型，一定要与当前 ItemType 的 bean 类型对应。
     */
    private boolean onLongClick(View view, BeanB bean, int position) {
        Toast.makeText(view.getContext(), "长点击事件：" + bean.text, Toast.LENGTH_SHORT).show();
        return true;
    }

}
