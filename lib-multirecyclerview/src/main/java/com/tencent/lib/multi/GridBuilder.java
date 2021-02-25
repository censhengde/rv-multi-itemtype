package com.tencent.lib.multi;

import android.content.Context;
import android.util.AttributeSet;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Author：岑胜德 on 2021/1/6 16:27
 * <p>
 * 说明：实现网格风格的Builder
 */
public class GridBuilder extends LayoutBuilder<GridBuilder> {
    private final int spanCount;
    GridBuilder(MultiRecyclerView rv, int spanCount) {
        super(rv);
        this.spanCount = spanCount;

    }

    public GridBuilder setItemSpace(int space, boolean includeEdge) {
        recyclerView.addItemDecoration(new GridItemDecoration(spanCount, space, includeEdge));
        return this;
    }

    public GridBuilder buildLayoutManager(Context context) {
        layoutManager = new GridLayoutManager(context, spanCount);
        return this;
    }

    public GridBuilder buildLayoutManager(Context context, @RecyclerView.Orientation int orientation, boolean reverseLayout) {
        layoutManager = new GridLayoutManager(context, spanCount, orientation, reverseLayout);
        return this;
    }

    public GridBuilder buildLayoutManager(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        layoutManager = new GridLayoutManager(context, attrs, defStyleAttr, defStyleRes);
        return this;
    }

    @Override
    public void build() {
        if (layoutManager==null){
            buildLayoutManager(recyclerView.getContext());
        }
        super.build();
    }
}
