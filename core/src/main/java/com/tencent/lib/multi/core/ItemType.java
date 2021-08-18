package com.tencent.lib.multi.core;

import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

/**
 * Author：岑胜德 on 2021/1/6 14:41
 * <p>
 * 说明：某一种Item类型的抽象。
 */
public interface ItemType<T, VH extends RecyclerView.ViewHolder> {

    /**
     * 当前position 是否匹配当前的ItemType
     *
     * @param bean 当前position对应的实体对象,当是依赖paging3 getItem()方法返回时，有可能为null。
     * @param position adapter position
     * @return true 表示匹配，false：不匹配。
     */
    boolean matchItemType(@Nullable T bean, int position);


    /**
     * 创建当前ItemType的ViewHolder
     *
     * @param parent parent
     * @return ViewHolder
     */
    @NonNull
    VH onCreateViewHolder(@NonNull ViewGroup parent);

    /**
     * ViewHolder已经创建完成，在这里可以注册Item及其子View点击事件监听器,但不要做数据的绑定。
     */
    void onViewHolderCreated(@NonNull VH holder, @NonNull MultiHelper<T, VH> helper);

    /**
     * 意义与Adapter onBindViewHolder 基本相同，表示当前ItemType的数据绑定过程。
     *
     * @param holder
     * @param position
     */
    void onBindViewHolder(@NonNull VH holder, @NonNull T bean, int position,
            @NonNull List<Object> payloads) throws Exception;


    void onBindViewHolder(@NonNull VH holder, @NonNull T bean, int position) throws Exception;

}
