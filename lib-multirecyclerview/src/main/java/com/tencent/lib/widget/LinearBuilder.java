package com.tencent.lib.widget;

import android.content.Context;
import android.util.AttributeSet;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Author：岑胜德 on 2021/1/6 16:22
 * <p>
 * 说明：实现线性效果的Builder
 */
public class LinearBuilder extends LayoutBuilder<LinearBuilder> {


    LinearBuilder(MultiRecyclerView rv) {
        super(rv);
    }

    public LinearBuilder setItemSpace(int l, int t, int r, int b) {
        recyclerView.addItemDecoration(new CommonItemSpace(l, t, r, b));
        return this;
    }

    public LinearBuilder buildLayoutManager(Context context, @RecyclerView.Orientation int orientation, boolean reverseLayout) {
        layoutManager = new LinearLayoutManager(context, orientation, reverseLayout);
        return this;
    }

    public LinearBuilder buildLayoutManager(Context context, @RecyclerView.Orientation int orientation) {
        return buildLayoutManager(context, orientation, false);
    }
    public LinearBuilder buildLayoutManager(Context context) {
        return buildLayoutManager(context, RecyclerView.VERTICAL);
    }

    public LinearBuilder buildLayoutManager(Context context, AttributeSet attrs, int defStyleAttr,
                                             int defStyleRes){
        layoutManager = new LinearLayoutManager(context, attrs, defStyleAttr,defStyleRes);
        return this;
    }

    @Override
    public void build() {
        if (layoutManager==null){
            layoutManager=new LinearLayoutManager(recyclerView.getContext(),RecyclerView.VERTICAL,false);
        }
        super.build();
    }
}
