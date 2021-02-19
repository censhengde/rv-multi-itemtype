package com.tencent.lib.widget;

import androidx.annotation.NonNull;
import java.util.List;

/**
 * Author：岑胜德 on 2021/2/5 16:02
 *
 * 说明：
 */
public interface OnCompletedCheckedCallback<T extends Checkable> {

    /**
     *
     * @param datas 被选中的Item集合
     */
    void onCompletedChecked(@NonNull List<T> datas);

}
