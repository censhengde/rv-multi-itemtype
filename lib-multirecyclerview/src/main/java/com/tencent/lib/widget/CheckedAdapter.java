package com.tencent.lib.widget;

import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

/**
 * Author：岑胜德 on 2021/2/5 17:06
 *
 * 说明：
 */
public class CheckedAdapter<T extends CheckableItem> extends MultiAdapter<T> {

    private final CheckedDelegateAdapter<T> delegateAdapter = new CheckedDelegateAdapter<T>(this) {
        @Nullable
        @Override
        public T getItem(int position) {
            return datas.get(position);
        }
    };

    @NonNull
    @Override
    public MultiViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return delegateAdapter.onCreateViewHolder(parent, viewType);
    }

    @Override
    public void onBindViewHolder(@NonNull MultiViewHolder holder, int position) {
        delegateAdapter.onBindViewHolder(holder, position);
    }

    @Override
    public int getItemViewType(int position) {
        return delegateAdapter.getItemViewType(position);
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

    @NonNull//but maybe empty
    public List<T> getSelectedItem() {
        final List<T> selected = new ArrayList<>();
        if (datas != null && !datas.isEmpty()) {
            for (T data : datas) {
                if (data.isChecked()){
                    selected.add(data);
                }
            }
        }
        return selected;
    }

    public void setSingleSelection(boolean isSingleSelection) {
        delegateAdapter.setSingleSelection(isSingleSelection);
    }
}
