package com.tencent.lib.multi;

import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;
import com.tencent.lib.multi.core.MultiHelper;
import com.tencent.lib.multi.core.checking.Checkable;
import com.tencent.lib.multi.core.checking.CheckingHelper;
import java.util.List;

/**
 * Author：岑胜德 on 2021/1/6 14:57
 * <p>
 * 说明：未分页的Adapter
 */
public class MultiAdapter<T, VH extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<VH> {

    private List<T> mData;

    private final MultiHelper<T, VH> mMultiHelper = new MultiHelper<T, VH>(this) {
        @Nullable
        @Override
        public T getItem(int position) {
            return MultiAdapter.this.getItem(position);
        }

    };
    private final CheckingHelper<T> mCheckingHelper = new CheckingHelper<T>(this) {
        @Nullable
        @Override
        protected T getItem(int position) {
            return MultiAdapter.this.getItem(position);
        }

        @Override
        protected int getDataSize() {
            return MultiAdapter.this.getItemCount();
        }


    };

    public MultiAdapter() {

    }

    public final boolean isInValidPosition(int position) {
        return position < 0 || mData == null || position >= mData.size();
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return mMultiHelper.onCreateViewHolder(parent, viewType);
    }

    @Override
    public void onBindViewHolder(@NonNull VH holder, int position) {

    }

    @Override
    public void onBindViewHolder(@NonNull VH holder, int position, @NonNull List<Object> payloads) {
        mMultiHelper.onBindViewHolder(holder, position, payloads);
    }


    @Override
    public int getItemViewType(int position) {
        return mMultiHelper.getItemViewType(position);
    }

    @Override
    public int getItemCount() {
        return mData == null ? 0 : mData.size();
    }

    @Nullable
    public T getItem(int position) {
        return isInValidPosition(position) ? null : mData.get(position);
    }

    public MultiAdapter<T, VH> setData(@NonNull List<T> data) {

        if (data == this.mData) {
            notifyDataSetChanged();
            return this;
        }
        this.mData = data;
        notifyDataSetChanged();
        return this;
    }


    public CheckingHelper<T> getCheckingHelper() {
        return mCheckingHelper;
    }

    public MultiHelper<T, VH> getMultiHelper() {
        return mMultiHelper;
    }

    public final void removeItem(int position) {
        if (!isInValidPosition(position)) {
            /*如果删除的Item是被选中的Item，则数量要减一*/
            final T item = mData.get(position);
            if (item instanceof Checkable) {
                final Checkable checkable = (Checkable) item;
                if (checkable.isChecked()) {
                    mCheckingHelper.setCheckedItemCount(mCheckingHelper.getCheckedItemCount() - 1);
                }
            }
            mData.remove(position);
            if (mMultiHelper.getItemTypeRecord() != null) {
                mMultiHelper.getItemTypeRecord().remove(position);
            }
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, mData.size() - position);
        }
    }

}
