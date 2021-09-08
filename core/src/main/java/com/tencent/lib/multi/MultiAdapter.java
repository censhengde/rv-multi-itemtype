package com.tencent.lib.multi;

import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.AsyncListDiffer;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;
import com.tencent.lib.multi.core.ItemType;
import com.tencent.lib.multi.core.MultiHelper;
import com.tencent.lib.multi.core.checking.Checkable;
import com.tencent.lib.multi.core.checking.CheckingHelper;
import java.util.Collections;
import java.util.List;
import org.jetbrains.annotations.NotNull;

/**
 * Author：岑胜德 on 2021/1/6 14:57
 * <p>
 * 说明：未分页的Adapter
 */
public class MultiAdapter<T, VH extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<VH> {

    private List<T> mData;
    private AsyncListDiffer<T> mAsyncListDiffer;
    private final MultiHelper<T, VH> mDelegate = new MultiHelper<T, VH>() {
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

    public MultiAdapter(DiffUtil.ItemCallback<T> callback) {
        mAsyncListDiffer = new AsyncListDiffer<>(this, callback);
    }


    public MultiAdapter() {

    }


    public final boolean isInValidPosition(int position, List<T> data) {
        return position < 0 || data == null || position >= data.size();
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return mDelegate.onCreateViewHolder(parent, viewType);
    }

    @Override
    public void onBindViewHolder(@NonNull VH holder, int position) {

    }

    @Override
    public void onBindViewHolder(@NonNull VH holder, int position, @NonNull List<Object> payloads) {
        mDelegate.onBindViewHolder(holder, position, payloads);
    }

    @Override
    public void onViewRecycled(@NonNull @NotNull VH holder) {
        mDelegate.onViewRecycled(holder);
    }

    @Override
    public int getItemViewType(int position) {
        return mDelegate.getItemViewType(position);
    }

    @Override
    public long getItemId(int position) {
        return mDelegate.getItemId(position);
    }

    public boolean onFailedToRecycleView(@NonNull VH holder) {
        return mDelegate.onFailedToRecycleView(holder);
    }


    public void onViewAttachedToWindow(@NonNull VH holder) {
        mDelegate.onViewAttachedToWindow(holder);
    }

    public void onViewDetachedFromWindow(@NonNull VH holder) {
        mDelegate.onViewAttachedToWindow(holder);
    }

    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
      mDelegate.onAttachedToRecyclerView(recyclerView);

    }


    public void onDetachedFromRecyclerView(@NonNull RecyclerView recyclerView) {
        mDelegate.onDetachedFromRecyclerView(recyclerView);
    }
    @Override
    public int getItemCount() {
        return getDataList().size();
    }

    @Nullable
    public T getItem(int position) {
        final List<T> currentList = getDataList();
        return isInValidPosition(position, currentList) ? null : currentList.get(position);
    }

    public MultiAdapter<T, VH> setData(@NonNull List<T> data) {
        if (mAsyncListDiffer != null) {
            mAsyncListDiffer.submitList(data);
            return this;
        }

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


    public final void removeItem(int position) {
        final List<T> currentList = getDataList();
        if (!isInValidPosition(position, currentList)) {
            /*如果删除的Item是被选中的Item，则数量要减一*/
            final T item = currentList.get(position);
            if (item instanceof Checkable) {
                final Checkable checkable = (Checkable) item;
                if (checkable.isChecked()) {
                    mCheckingHelper.setCheckedItemCount(mCheckingHelper.getCheckedItemCount() - 1);
                }
            }
            currentList.remove(position);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, currentList.size() - position);
        }
    }

    public MultiAdapter<T, VH> addItemType(ItemType type) {
        mDelegate.addItemType(type);
        return this;
    }

    public final MultiHelper<T, VH> getMultiHelper() {
        return mDelegate;
    }

    @NotNull
    public final List<T> getDataList() {
        final List<T> currentList = mAsyncListDiffer == null ?
                mData : mAsyncListDiffer.getCurrentList();
        return currentList == null ? Collections.emptyList() : currentList;
    }
}
