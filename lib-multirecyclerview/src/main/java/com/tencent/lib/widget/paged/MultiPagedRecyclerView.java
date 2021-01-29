package com.tencent.lib.widget.paged;

import android.content.Context;
import android.util.AttributeSet;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.tencent.lib.widget.MultiRecyclerView;

/**
 * Author：岑胜德 on 2021/1/18 20:30
 *
 * 说明：
 */
public class MultiPagedRecyclerView extends MultiRecyclerView {

    public MultiPagedRecyclerView(@NonNull Context context) {
        super(context);
    }

    public MultiPagedRecyclerView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public MultiPagedRecyclerView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }



   public void refresh(){
        getItemManager().refresh();
    }

   public void loadMore(){
        getItemManager().loadMore();
    }

    @Override
    public PagedItemManager getItemManager() {
        return(PagedItemManager)getAdapter();
    }
}
