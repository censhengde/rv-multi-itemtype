package com.tencent.multirecyclerview.widget;

import androidx.annotation.NonNull;

import java.util.List;

/**
 * Author：岑胜德 on 2021/1/6 17:26
 * <p>
 * 说明：
 */
public interface IBuilder {
  void  setDatas(@NonNull List<?> datas);

      void setItemTypes(@NonNull List<ItemType<?>> types);

     void setItemType(@NonNull ItemType<?> type);

}
