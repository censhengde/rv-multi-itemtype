package com.tencent.multiadapter.example.bean;

import com.tencent.lib.multi.core.SimpleCheckable;

/**
 * Author：岑胜德 on 2021/5/12 17:24
 *
 * 说明：
 */
public class CheckableItem extends SimpleCheckable {
    public static final int VIEW_TYPE_ITEM=0;
    public static final int VIEW_TYPE_HEADER=1;
    public static final int VIEW_TYPE_FOOTER=2;
    public int viewType=VIEW_TYPE_ITEM;

    public String text="";

    public CheckableItem(int viewType, String text) {
        this.viewType = viewType;
        this.text = text;
    }
}
