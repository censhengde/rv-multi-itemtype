package com.tencent.lib.multi;

import androidx.recyclerview.widget.DiffUtil.ItemCallback;
import com.tencent.lib.multi.core.MultiViewHolder;

/**
 * Author：岑胜德 on 2021/1/6 14:57
 * <p>
 * 说明：未分页的Adapter
 */
public class MultiAdapter2<T> extends MultiAdapter<T, MultiViewHolder> {

    public MultiAdapter2(ItemCallback<T> callback) {
        super(callback);
    }

    public MultiAdapter2() {
    }


}
