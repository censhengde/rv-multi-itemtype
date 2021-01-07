package com.tencent.mutilrecyclerview.mutilrecyclerview;

import android.content.Context;
import android.util.SparseArray;
import android.util.SparseIntArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Author：岑胜德 on 2021/1/6 14:58
 * <p>
 * 说明：
 */
public class MutilViewHolder extends RecyclerView.ViewHolder {
    //由于findViewById频繁调用比较消耗性能，所以要缓存
    private final SparseArray<View> id_view_map;

    public static @NonNull MutilViewHolder newInstance(Context context, ViewGroup parent,int layout){
     View itemView=   LayoutInflater.from(context).inflate(layout,parent,false);
     return new MutilViewHolder(itemView);
    }

    private MutilViewHolder(@NonNull View itemView) {
        super(itemView);
        id_view_map=new SparseArray<>(5);
    }
    public <T extends View> T getView(int id){
        T v= (T) id_view_map.get(id);
        if (v==null){
            v=itemView.findViewById(id);
            if (v==null){
                throw new IllegalStateException("未找到View");
            }
            id_view_map.put(id,v);
        }
        return v;
    }
}
