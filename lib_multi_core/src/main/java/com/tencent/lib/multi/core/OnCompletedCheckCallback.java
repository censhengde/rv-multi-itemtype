package com.tencent.lib.multi.core;

import androidx.annotation.NonNull;
import java.util.List;

/**
 * Author：岑胜德 on 2021/2/5 16:02
 *
 * 说明：把列表中被选中的Item回调出去
 */
public interface OnCompletedCheckCallback<T> {

    /**
     *
     * @param datas 被选中的Item集合
     */
    void onCompletedChecked(@NonNull List<T> datas);

}
