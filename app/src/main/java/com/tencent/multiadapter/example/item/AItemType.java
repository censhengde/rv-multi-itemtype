package com.tencent.multiadapter.example.item;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.tencent.lib.multi.core.SimpleItemType;
import com.tencent.lib.multi.core.MultiViewHolder;
import com.tencent.lib.multi.core.annotation.BindItemViewClickEvent;
import com.tencent.multiadapter.databinding.ItemABinding;
import com.tencent.multiadapter.example.bean.AItemBean;

import java.util.List;

import org.jetbrains.annotations.NotNull;

/**
 * Author：岑胜德 on 2021/1/6 18:04
 * <p>
 * 说明：
 */
public class AItemType extends SimpleItemType<AItemBean, ItemABinding> {

    public AItemType() {
        inject(this);
    }

    @Override
    public boolean isMatchForMe(@Nullable Object bean, int position) {
        return bean instanceof AItemBean;
    }

    /**
     * 表示ViewHolder已经创建完成。本方法最终是在RecyclerView.Adapter onCreateViewHolder方法中被调用，
     * 所以所有的与item相关的点击事件监听器都应在这里注册。
     *
     * @param holder
     */
    @Override
    public void onViewHolderCreated(@NonNull MultiViewHolder holder, ItemABinding binding) {
        /*注册监听器，不传view则默认是给item根布局注册监听*/
        registerClickEvent(holder, "onClickItem");
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ItemABinding binding,
                                 @NonNull @NotNull AItemBean bean,
                                 int position,
                                 @NonNull @NotNull List<Object> payloads) {
        if (payloads.isEmpty()){
            onBindViewHolder(binding,bean,position);
            return;
        }
        Log.e("===>", " A 类Item 局部刷新：" + position);
        for (Object payload : payloads) {
            if (payload instanceof Bundle) {
                Bundle bundle = (Bundle) payload;
                binding.tvA.setText(bundle.getString("content"));
            }
        }


    }

    @Override
    public void onBindViewHolder(@NonNull ItemABinding binding,
                                 @NotNull AItemBean itemBean,
                                 int position) {
        Log.e("===>", " A 类Item 级别刷新：" + position);
        binding.tvA.setText(itemBean.text);
    }

    @Override
    public void onViewRecycled(MultiViewHolder holder) {
        Log.e("AItemType ", "===> onViewRecycled" + holder.getItemViewType());
    }

    /**
     * item点击事件
     */
    @BindItemViewClickEvent("onClickItem")
    private void onClickItem(View view, AItemBean bean, int position) {
        Toast.makeText(view.getContext(), "点击事件："+bean.text, Toast.LENGTH_SHORT).show();
    }
}
