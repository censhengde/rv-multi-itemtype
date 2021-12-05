package com.tencent.multiadapter.example.item;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.tencent.lib.multi.core.BindingMultiItem;
import com.tencent.lib.multi.core.MultiViewHolder;
import com.tencent.multiadapter.databinding.ItemABinding;
import com.tencent.multiadapter.example.bean.AItemBean;

import java.util.List;

import org.jetbrains.annotations.NotNull;

/**
 * Author：岑胜德 on 2021/1/6 18:04
 * <p>
 * 说明：
 */
public class AMultiItem extends BindingMultiItem<AItemBean, ItemABinding> {

    /**
     * @param bean     当前position对应的实体对象
     * @param position
     * @return true 表示成功匹配到对应的ItemType
     */
    @Override
    public boolean isMatchForMe(@Nullable Object bean, int position) {
        if (bean instanceof  AItemBean){
            return true;
        }
        return  AItemBean.TYPE_A_00 == ((AItemBean) bean).viewType;//这句话的含义是：当前position 的ItemBean想要表现的item类型是哪一种，
        //以本例为例，会依次遍历A、B、C三个Item类型，直到返回true为止。（详见MultiHelper getItemViewType方法实现）
    }

    /**
     * 表示ViewHolder已经创建完成。本方法最终是在RecyclerView.Adapter onCreateViewHolder方法中被调用，
     * 所以所有的与item相关的点击事件监听器都应在这里注册。
     *
     * @param holder
     */
    @Override
    public void onViewHolderCreated(@NonNull MultiViewHolder holder, ItemABinding binding) {
        /*注册监听器，不传viewId则默认是给item根布局注册监听*/
        registerItemViewClickListener(holder, "onClickItem");
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ItemABinding binding,
                                 @NonNull @NotNull AItemBean bean,
                                 int position,
                                 @NonNull @NotNull List<Object> payloads) {
        Log.e("===>", " A 类Item 局部刷新：" + position);
        for (Object payload : payloads) {
            if (payload instanceof Bundle) {
                Bundle bundle = (Bundle) payload;
                binding.tvA.setText(bundle.getString("content"));
            }
        }

    }

    /**
     * 给当前item类型布局视图设置数据，意义基本与RecyclerView.Adapter onBindViewHolder 相同。
     *
     * @param position
     */

    @Override
    public void onBindViewHolder(@NonNull ItemABinding binding,
                                 @NotNull AItemBean itemBean,
                                 int position) {
        Log.e("===>", " A 类Item 级别刷新：" + position);
        binding.tvA.setText(itemBean.text);
    }

    @SuppressLint("LongLogTag")
    @Override
    public void onViewRecycled(MultiViewHolder holder) {
        Log.e("===> AItemType onViewRecycled", "" + holder.getItemViewType());
    }
}
