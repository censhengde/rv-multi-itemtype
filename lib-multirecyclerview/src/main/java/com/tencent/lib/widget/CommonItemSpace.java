package com.tencent.lib.widget;

import android.graphics.Rect;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Author：岑胜德 on 2021/1/6 17:03
 * <p>
 * 说明：
 */
public class CommonItemSpace extends RecyclerView.ItemDecoration {
    int letfSpace;
    int topSpace;
    int rightSpace;
    int bottomSpace;

    public CommonItemSpace(int letfSpace, int topSpace, int rightSpace, int bottomSpace) {
        this.letfSpace = letfSpace;
        this.topSpace = topSpace;
        this.rightSpace = rightSpace;
        this.bottomSpace = bottomSpace;
    }

    @Override
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        outRect.left=letfSpace;
        outRect.top=topSpace;
        outRect.right=rightSpace;
        outRect.bottom=bottomSpace;
    }
}
