package com.tencent.mutilrecyclerview.mutilrecyclerview;

import android.content.Context;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

/**
 * Author：岑胜德 on 2021/1/6 14:51
 * <p>
 * 说明：
 */
public class MutilRecyclerView extends RecyclerView implements IBuilder{
    public MutilRecyclerView(@NonNull Context context) {
        super(context);
    }

    public MutilRecyclerView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public MutilRecyclerView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void setDatas(@NonNull List datas) {
        ((MutilAdapter) getAdapter()).setDatas(datas);
    }

    @Override
    public void setItemType(@NonNull ItemType type) {
        ((MutilAdapter) getAdapter()).setItemType(type);
    }

    @Override
    public void setItemTypes(@NonNull List<ItemType<?>> types) {
        ((MutilAdapter) getAdapter()).setItemTypes(types);
    }

    public static abstract class Builder<T extends Builder >{
       final MutilRecyclerView recyclerView;

        Builder(MutilRecyclerView rv) {
            recyclerView = rv;
            recyclerView.setAdapter(new MutilAdapter());
        }


        public T setItemSpace(int l,int t,int r,int b){
            recyclerView.addItemDecoration(new CommonItemSpace(l,t,r,b));
            return (T) this;
        }
        public abstract void build();


        public T setDatas(@NonNull List<?> data) {
            recyclerView.setDatas(data);
            return (T) this;
        }

        public  T setItemTypes(@NonNull List<ItemType<?>> itemTypes) {
               recyclerView.setItemTypes(itemTypes);
               return (T) this;
        }

        public  T setItemType(@NonNull ItemType<?> type) {
           recyclerView.setItemType(type);
           return (T) this;
        }
    }

    public LinearBuilder linearBuilder() {
        return new LinearBuilder(this);
    }

    public GridBuilder gridBuilder(int span) {
        return new GridBuilder(this,span);
    }

    public StaggeredGridBuilder staggeredGridBuilder(int gapStrategy) {
        return new StaggeredGridBuilder(this,gapStrategy);
    }


}
