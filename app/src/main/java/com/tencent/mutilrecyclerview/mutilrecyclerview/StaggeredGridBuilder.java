package com.tencent.mutilrecyclerview.mutilrecyclerview;

import android.content.Context;
import android.util.AttributeSet;

import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

/**
 * Author：岑胜德 on 2021/1/6 16:30
 * <p>
 * 说明：实现瀑布效果的Builder
 */
public class StaggeredGridBuilder extends MutilRecyclerView.Builder<StaggeredGridBuilder> {
    private StaggeredGridLayoutManager mLayoutManager;
    private int spanCount;
    private int gapStrategy;
    @RecyclerView.Orientation
    private int orientation = RecyclerView.VERTICAL;//默认垂直

    StaggeredGridBuilder(MutilRecyclerView rv,int gapStrategy) {
        super(rv);
        this.gapStrategy=gapStrategy;
    }

    public StaggeredGridBuilder buildLayoutManager(int spanCount, @RecyclerView.Orientation int orientation) {
        mLayoutManager = new StaggeredGridLayoutManager(spanCount, orientation);
        return this;
    }

    public StaggeredGridBuilder buildLayoutManager(Context context, AttributeSet attrs, int defStyleAttr,
                                                   int defStyleRes) {
        mLayoutManager = new StaggeredGridLayoutManager(context, attrs, defStyleAttr, defStyleRes);
        return this;
    }

    public StaggeredGridBuilder setItemSpace(int space){
        recyclerView.addItemDecoration(new StaggeredGridItemDecoration(space));
        return this;
    }
    public StaggeredGridBuilder setItemSpace(int l,int t,int r,int b){
        recyclerView.addItemDecoration(new CommonItemSpace(l,t,r,b));
        return this;
    }
    @Override
    public void build() {
        mLayoutManager.setGapStrategy(gapStrategy);
        recyclerView.setLayoutManager(mLayoutManager);
    }
}
