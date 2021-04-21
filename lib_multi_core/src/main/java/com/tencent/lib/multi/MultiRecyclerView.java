package com.tencent.lib.multi;

import android.content.Context;
import android.util.AttributeSet;
import androidx.annotation.NonNull;
import com.tencent.lib.multi.core.BaseRecyclerView;
import com.tencent.lib.multi.core.ItemType;
import java.util.List;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Author：岑胜德 on 2021/4/15 19:46
 *
 * 说明：
 */
public class MultiRecyclerView extends BaseRecyclerView {


    public MultiRecyclerView(@NonNull Context context) {
        this(context,null);
    }

    public MultiRecyclerView(@NotNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public MultiRecyclerView(@NotNull Context context,
            @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Nullable
    public MultiAdapter getMultiAdapter(){
        if (getAdapter() instanceof MultiAdapter){
            return (MultiAdapter)getAdapter();
        }
        return null;
    }
    @Override
    public void setItemTypes(@NotNull List<ItemType<?>> types) {
           if (getMultiAdapter()!=null){
               getMultiAdapter().setItemTypes(types);
           }
    }

    @NotNull
    @Override
    public MultiRecyclerView addItemType(@NotNull ItemType<?> type) {
        if (getMultiAdapter()!=null){
            getMultiAdapter().addItemType(type);
        }
        return this;
    }
}
