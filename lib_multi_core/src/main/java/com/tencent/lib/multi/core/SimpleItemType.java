package com.tencent.lib.multi.core;

import androidx.annotation.NonNull;

/**
 * Author：岑胜德 on 2021/2/22 16:23
 *
 * 说明：简单的 ItemType 实现
 */
public abstract class SimpleItemType<T> implements ItemType<T> {
     private OnClickItemListener<T> mItemListener;

    public void setItemListener(OnClickItemListener<T> itemListener) {
        mItemListener = itemListener;
    }

    @Override
    public int getViewType() {
        return 0;
    }

    @Override
    public boolean matchItemType(@NonNull T data, int position) {
        return false;
    }



    @Override
    public void onInitItemSubViewListener(@NonNull MultiViewHolder holder) {

    }

    @Override
    public void onClickItemView(@NonNull MultiViewHolder holder, @NonNull T data, int position) {
              if (mItemListener!=null){
                  mItemListener.onClickItem(data,position);
              }
    }


}
