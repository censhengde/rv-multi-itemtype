package com.tencent.lib.widget;

import androidx.annotation.NonNull;

import java.util.List;

/**
 * Author：岑胜德 on 2021/1/6 17:26
 * <p>
 * 说明：
 */
@Deprecated
public interface IBuilder<T> {
  void  setDatas(@NonNull List<T> datas);

      void setItemTypes(@NonNull List<ItemType<T>> types);

     void setItemType(@NonNull ItemType<T> type);

}
