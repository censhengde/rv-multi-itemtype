package com.tencent.lib.widget;

import android.os.Bundle;
import androidx.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

/**
 * Author：岑胜德 on 2021/2/5 17:06
 *
 * 说明：
 */
class CheckedAdapter<T extends Checkable> extends MultiAdapter<T> implements CheckManager {

    private OnCompletedCheckItemCallback<T> onCompletedCheckItemCallback;

    CheckedAdapter() {
        delegateAdapter = new CheckedDelegateAdapter<T>(this) {
            @Override
            public void complete(OnCompletedCheckItemCallback<T> callback) {
                if (callback != null) {
               final List<T> checked = new ArrayList<>();
                //筛选出被选中的Item
                if (datas != null && !datas.isEmpty()) {
                    for (T data : datas) {
                        if (data.isChecked()) {
                            checked.add(data);
                        }
                    }
                }
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

    @Override
    public void cancelCheckAll() {
        if (datas != null && !datas.isEmpty()) {
            for (T data : datas) {
                data.setChecked(false);
            }
            notifyDataSetChanged();
        }
    }

    @Override
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

    void setOnCompletedCheckItemCallback(OnCompletedCheckItemCallback<T> callback) {
        this.onCompletedCheckItemCallback = callback;
    }

    public void checkable(boolean checkable) {
        ((CheckedDelegateAdapter) delegateAdapter).checkable = checkable;
    }

    public void completeCheck() {
        if (onCompletedCheckItemCallback != null) {
            ((CheckedDelegateAdapter<T>) delegateAdapter).complete(onCompletedCheckItemCallback);
        }
    }


    @Override
    public void saveCheckedItem(Bundle out) {

    }

    @Override
    public void restoreCheckedItem(Bundle in) {

    }
}
