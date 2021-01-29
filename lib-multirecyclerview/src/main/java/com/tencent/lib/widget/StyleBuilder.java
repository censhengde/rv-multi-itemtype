package com.tencent.lib.widget;

/**
 * Author：岑胜德 on 2021/1/29 16:18
 *
 * 说明：实现风格的Builder
 */
public abstract class StyleBuilder<T extends Builder> extends Builder<T> {

    private boolean isPaged = false;

    StyleBuilder(MultiRecyclerView rv) {
        super(rv);
    }

    //当调用这个方法后，链式调用开始走向PagedBuilder分支。
    public PagedBuilder isPaged() {
        isPaged = true;
        this.build();//如果分页，则意味着风格的构造已完毕
        return new PagedBuilder(recyclerView);
    }

    @Override
    public void build() {
        //如果不分页 则采用默认构造。
        if (!isPaged) {
            super.build();
        }

    }
}
