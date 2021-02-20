package com.tencent.lib.widget;

import androidx.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

/**
 * Author：岑胜德 on 2021/2/5 17:06
 *
 * 说明：
 */
class CheckedAdapter<T extends Checkable> extends MultiAdapter<T> {

    private OnCompletedCheckedCallback<T> onCompletedCheckedCallback;

    CheckedAdapter() {
        delegateAdapter = new CheckedDelegateAdapter<T>(this) {
            @Override
            public void complete(OnCompletedCheckedCallback<T> callback) {
                List<T> checked = new ArrayList<>();
                //筛选出被选中的Item
                if (datas != null && !datas.isEmpty()) {
                    for (T data : datas) {
                        if (data.isChecked()) {
                            checked.add(data);
                        }
                    }
                }
                if (callback != null) {
                    callback.onCompletedChecked(checked);
                }
            }

            @Nullable
            @Override
            public T getItem(int position) {
                return datas.get(position);
            }
        };
    }

    public void cancelAll() {
        if (datas != null && !datas.isEmpty()) {
            for (T data : datas) {
                data.setChecked(false);
            }
            notifyDataSetChanged();
        }
    }

    public void checkAll() {
        if (datas != null && !datas.isEmpty()) {
            for (T data : datas) {
                data.setChecked(true);
            }
            notifyDataSetChanged();
        }
    }


    void setSingleSelection(boolean isSingleSelection) {
        ((CheckedDelegateAdapter) delegateAdapter).setSingleSelection(isSingleSelection);
    }

    void setOnCompletedCheckedCallback(OnCompletedCheckedCallback<T> callback) {
        this.onCompletedCheckedCallback = callback;
    }

    public void checkable(boolean checkable) {
        ((CheckedDelegateAdapter) delegateAdapter).checkable = checkable;
    }
    public void complete() {
        if (onCompletedCheckedCallback != null) {
            ((CheckedDelegateAdapter<T>) delegateAdapter).complete(onCompletedCheckedCallback);
        }
    }
}
