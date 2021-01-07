package com.tencent.mutilrecyclerview.example.itemtype;

import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.tencent.mutilrecyclerview.R;
import com.tencent.mutilrecyclerview.example.bean.ItemBean;
import com.tencent.mutilrecyclerview.mutilrecyclerview.ItemType;
import com.tencent.mutilrecyclerview.mutilrecyclerview.MutilViewHolder;

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
    public void onBindViewHolder(@NonNull MutilViewHolder holder, @NonNull ItemBean data, int position) {
          TextView tv= holder.getView(R.id.tv_b);
          tv.setText(data.text);
    }


    @Override
    public void onInitItemSubViewListener(MutilViewHolder holder) {
        TextView tv = holder.getView(R.id.tv_b);
        tv.setOnClickListener(v -> {
            Log.e("Item子View点击事件=====>", (String) tv.getText());
        });
    }

    @Override
    public void onClickItemView(MutilViewHolder holder, ItemBean data, int position) {
        Log.e("Item点击事件=====>", data.text);
    }
}
