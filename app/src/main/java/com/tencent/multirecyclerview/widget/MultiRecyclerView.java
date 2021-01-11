package com.tencent.multirecyclerview.widget;

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
public class MultiRecyclerView extends RecyclerView implements IBuilder,ItemManager{
    public MultiRecyclerView(@NonNull Context context) {
        super(context);
    }

    public MultiRecyclerView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public MultiRecyclerView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void setDatas(@NonNull List datas) {
        ((MultiAdapter) getAdapter()).setDatas(datas);
    }

    @Override
    public void setItemType(@NonNull ItemType type) {
        ((MultiAdapter) getAdapter()).setItemType(type);
    }

    @Override
    public void setItemTypes(@NonNull List<ItemType<?>> types) {
        ((MultiAdapter) getAdapter()).setItemTypes(types);
    }

    @Override
    public void removeItem(int position) {

    }

    @Override
    public void updateItem(int position) {

    }

    @Override
    public void updateAll() {

    }

    @Override
    public void addItem(Object data) {

    }

    @Override
    public void insertItem(int position, Object data) {

    }

    public static abstract class Builder<T extends Builder >{
       final MultiRecyclerView recyclerView;

        Builder(MultiRecyclerView rv) {
            recyclerView = rv;
            recyclerView.setAdapter(new MultiAdapter());
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


        public T  addItemDecoration(@NonNull ItemDecoration decor, int index){
            recyclerView.addItemDecoration(decor,index);
            return (T) this;
        }
        public T  addItemDecoration(@NonNull ItemDecoration decor){
            return addItemDecoration(decor,-1);
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
