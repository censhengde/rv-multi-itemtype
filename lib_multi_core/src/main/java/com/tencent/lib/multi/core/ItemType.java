package com.tencent.lib.multi.core;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import java.util.List;

/**
 * Author：岑胜德 on 2021/1/6 14:41
 * <p>
 * 说明：Item类型的抽象，内容有：
 */
public interface ItemType<T> {

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
     * 返回当前ItemType的布局文件id
     * @return
     */
    @LayoutRes
    int getItemLayoutRes();

    /**
     * 意义与Adapter相同
     * @param holder
     * @param position
     */
    void onBindViewHolder(@NonNull MultiViewHolder holder, @NonNull MultiHelper<T> helper, int position);

    void onBindViewHolder(@NonNull MultiViewHolder holder, @NonNull MultiHelper<T> helper, int position,
            @NonNull List<Object> payloads);

    /**
     * 初始化Item子View点击事件监听器,不要做数据的绑定。
     */
    void onCreateItemView(@NonNull MultiViewHolder holder, @NonNull MultiHelper<T> helper);

    /**
     * 条目点击事件回调
     */
    void onClickItem(@NonNull MultiViewHolder holder, @NonNull T data, int position);

    boolean onLongClickItem(@NonNull MultiViewHolder holder, @NonNull T data, int position);
}
