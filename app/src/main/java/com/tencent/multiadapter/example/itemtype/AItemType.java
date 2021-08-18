package com.tencent.multiadapter.example.itemtype;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
public class AItemType extends MultiItemType<ItemBean> {


    /**
     * @param bean 当前position对应的实体对象
     * @param position
     * @return true 表示成功匹配到对应的ItemType
     */
    @Override
    public boolean matchItemType(@Nullable ItemBean bean, int position) {
        return bean == null || ItemBean.TYPE_A == bean.viewType;//这句话的含义是：当前position 的ItemBean想要表现的item类型是哪一种，
        //以本例为例，会依次遍历A、B、C三个Item类型，直到返回true为止。（详见MultiHelper getItemViewType方法实现）
    }

    /**
     * @return 返回当前item类型的布局文件
     */
    @Override
    public int getItemLayoutRes() {
        return R.layout.item_a;
    }

    /**
     * 表示ViewHolder已经创建完成。本方法最终是在RecyclerView.Adapter onCreateViewHolder方法中被调用，
     * 所以所有的与item相关的点击事件监听器都应在这里注册。
     *
     * @param holder
     * @param helper
     */
    @Override
    public void onViewHolderCreated(@NonNull MultiViewHolder holder,
            @NonNull MultiHelper<ItemBean, MultiViewHolder> helper) {
        /*注册监听器，不传viewId则默认是给item根布局注册监听*/
        registerItemViewClickListener(holder, helper, "onClickItem");
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull MultiViewHolder holder,
            @NonNull @NotNull ItemBean bean,
            int position,
            @NonNull @NotNull List<Object> payloads) {
        Log.e("===>", " A 类Item 局部刷新：" + position);
        for (Object payload : payloads) {
            if (payload instanceof Bundle) {
                Bundle bundle = (Bundle) payload;
                TextView tv = holder.getView(R.id.tv_a);
                tv.setText(bundle.getString("content"));
            }
        }

    }

    /**
     * 给当前item类型布局视图设置数据，意义基本与RecyclerView.Adapter onBindViewHolder 相同。
     *
     * @param holder
     * @param position
     */

    @Override
    public void onBindViewHolder(@NonNull MultiViewHolder holder,
            @NotNull ItemBean itemBean,
            int position) {
        Log.e("===>", " A 类Item 级别刷新：" + position);
        TextView tv = holder.getView(R.id.tv_a);
        tv.setText(itemBean.text);
    }

}
