package com.tencent.multiadapter.example.item;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.tencent.lib.multi.core.MultiViewHolder;
import com.tencent.lib.multi.core.SimpleItemType;
import com.tencent.lib.multi.core.annotation.OnClickItemView;
import com.tencent.multiadapter.databinding.ItemABinding;
import com.tencent.multiadapter.example.bean.BeanA;
import java.util.List;
import org.jetbrains.annotations.NotNull;

/**
 * Author：岑胜德 on 2021/1/6 18:04
 * <p>
 * 说明：
 */
public class AItemType extends SimpleItemType<BeanA, ItemABinding> {

    public AItemType() {
        inject(this);
    }

    @Override
    public boolean isMatchForMe(@Nullable Object bean, int position) {
        return bean instanceof BeanA;
    }

    /**
     * 表示ViewHolder已经创建完成。本方法最终是在RecyclerView.Adapter onCreateViewHolder方法中被调用，
     * 所以所有的与item相关的点击事件监听器都应在这里注册。
     *
     * @param holder
     */
    @Override
    public void onViewHolderCreated(@NonNull MultiViewHolder holder, @NotNull ItemABinding binding) {
        /*注册监听器，不传view则默认是给item根布局注册监听*/
        registerClickEvent(holder, holder.itemView,"onClickItem");
    }

    @Override
    public void onBindViewHolder(@NonNull ItemABinding binding,
                                 @NotNull BeanA itemBean,
                                 int position) {
        binding.tvA.setText(itemBean.text);
    }

    @Override
    public void onViewRecycled(MultiViewHolder holder) {
        Log.e("AItemType ", "===> onViewRecycled" + holder.getItemViewType());
    }

    /**
     * item点击事件
     */
    @OnClickItemView("onClickItem")
    private void onClickItem(View view, BeanA bean, int position) {
        Toast.makeText(view.getContext(), "点击事件："+bean.text, Toast.LENGTH_SHORT).show();
    }
}
