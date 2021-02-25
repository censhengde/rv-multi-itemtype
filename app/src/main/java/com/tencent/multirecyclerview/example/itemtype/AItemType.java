package com.tencent.multirecyclerview.example.itemtype;

import android.util.Log;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.tencent.multirecyclerview.R;
import com.tencent.multirecyclerview.example.bean.ItemBean;
import com.tencent.lib.multi.ItemType;
import com.tencent.lib.multi.MultiViewHolder;

/**
 * Author：岑胜德 on 2021/1/6 18:04
 * <p>
 * 说明：
 */
public class AItemType implements ItemType<ItemBean> {

    /**
     * 返回当前item类型的标识
     * @return
     */
    @Override
    public int getViewType() {
        return ItemBean.TYPE_A;
    }

    @Override
    public boolean matchItemType(ItemBean data, int position) {
        return getViewType() == data.viewType;//这句话的含义是：当前位置item想要呈现的item类型是哪一种，
        //以A、B、C三类为例，进行遍历，直到返回true为止。（详见MutilAdapter getItemViewType方法实现）
    }

    @Override
    public int getItemLayoutRes() {
        return R.layout.item_a;
    }

    /**
     * 绑定数据
     * @param holder
     * @param data 当前position对应的实体对象
     * @param position
     */
    @Override
    public void onBindViewHolder(@NonNull MultiViewHolder holder, @NonNull ItemBean data, int position) {
        TextView tvA = holder.getView(R.id.tv_a);
        tvA.setText(data.text);
    }

    /**
     * 设置item子View的监听
     * @param holder
     */
    @Override
    public void onInitItemSubViewListener(MultiViewHolder holder) {
        TextView tv = holder.getView(R.id.tv_a);
        tv.setOnClickListener(v -> {
            Log.e("Item子View点击事件=====>", (String) tv.getText());
        });
    }

    /**
     * item点击事件回调
     * @param holder
     * @param data
     * @param position
     */
    @Override
    public void onClickItemView(MultiViewHolder holder, ItemBean data, int position) {
        Log.e("Item点击事件=====>", data.toString());
    }


}
