package com.tencent.lib.multi.core;

import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

/**
 * Author：岑胜德 on 2021/1/6 14:41
 * <p>
 * 说明：某一种Item类型的抽象。
 */
public interface ItemType<T,VH extends RecyclerView.ViewHolder> {

    /**
     * @return 返回当前ItemType 的viewtype
     */
    int getViewType();

    /**
     * 根据当前位置Item匹配它的布局类型
     * @param data 当前position对应的实体对象
     * @param position
     * @return true 表示匹配成功，然后以position为key，ItemType为value存进Map，意味着后面
     *        在Adapter的getViewType方法中可根据position拿到对应的ItemType，进而拿到viewType值。
     */
    boolean matchItemType(@NonNull T data, int position);



    /**
     * 创建当前ItemType的ViewHolder
     * @return
     * @param parent
     */
    @NonNull
    VH onCreateViewHolder(@NonNull ViewGroup parent);

    /**
     * ViewHolder已经创建完成，在这里可以注册Item及其子View点击事件监听器,但不要做数据的绑定。
     */
    void onViewHolderCreated(@NonNull VH holder, @NonNull MultiHelper<T,VH> helper);

    /**
     * 意义与Adapter相同
     * @param holder
     * @param position
     */
    void onBindViewHolder(@NonNull VH holder, @NonNull MultiHelper<T,VH> helper, int position);

    void onBindViewHolder(@NonNull VH holder, @NonNull MultiHelper<T,VH> helper, int position,
            @NonNull List<Object> payloads);



}
