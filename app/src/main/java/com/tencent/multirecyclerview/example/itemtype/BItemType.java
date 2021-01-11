package com.tencent.multirecyclerview.example.itemtype;

import android.util.Log;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.tencent.multirecyclerview.R;
import com.tencent.multirecyclerview.example.bean.ItemBean;
import com.tencent.multirecyclerview.widget.ItemType;
import com.tencent.multirecyclerview.widget.MultiViewHolder;

/**
 * Author：岑胜德 on 2021/1/6 18:04
 * <p>
 * 说明：
 */
public class BItemType implements ItemType<ItemBean> {

    @Override
    public int getViewType() {
        return ItemBean.TYPE_B;
    }

    @Override
    public boolean matchItemType(ItemBean data, int position) {

        return getViewType()==data.viewType;
    }

    @Override
    public int getItemLayoutRes() {
        return R.layout.item_b;
    }


    @Override
    public void onBindViewHolder(@NonNull MultiViewHolder holder, @NonNull ItemBean data, int position) {
          TextView tv= holder.getView(R.id.tv_b);
          tv.setText(data.text);
    }


    @Override
    public void onInitItemSubViewListener(MultiViewHolder holder) {
        TextView tv = holder.getView(R.id.tv_b);
        tv.setOnClickListener(v -> {
            Log.e("Item子View点击事件=====>", (String) tv.getText());
        });
    }

    @Override
    public void onClickItemView(MultiViewHolder holder, ItemBean data, int position) {
        Log.e("Item点击事件=====>", data.text);
    }
}
