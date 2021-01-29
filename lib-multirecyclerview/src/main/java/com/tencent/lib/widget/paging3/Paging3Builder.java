package com.tencent.lib.widget.paging3;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import com.tencent.lib.widget.MultiRecyclerView;
import com.tencent.lib.widget.MultiRecyclerView.Builder;

/**
 * Author：岑胜德 on 2021/1/27 15:50
 *
 * 说明：
 */
public class Paging3Builder extends Builder {

    private  MultiPaging3Adapter adapter;

    public Paging3Builder(MultiRecyclerView rv) {
        super(rv);
    }
    public Paging3Builder setItemDiffCallback(@NonNull DiffUtil.ItemCallback diffCallback){
        adapter=new MultiPaging3Adapter(diffCallback);
        return this;
    }


    @Override
    public void build() {

    }
}
