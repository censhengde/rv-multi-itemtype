package com.tencent.multiadapter.example.bean;

import android.text.TextUtils;
import java.util.Objects;

/**
 * Author：岑胜德 on 2021/1/6 18:05
 * <p>
 * 说明：
 */
public class ItemBean {

    //所有Item类型都在这里定义
    public static final int TYPE_A = 0;
    public static final int TYPE_B = 1;
    public static final int TYPE_C = 2;

    public int id;
    //Item类型标识（很关键！）
    public int viewType;


    //item具体业务数据字段
    public String text = "";


    public ItemBean(int viewType, String text) {
        this.viewType = viewType;
        this.text = text;
    }

    public ItemBean(int id, int viewType, String text) {
        this.viewType = viewType;
        this.text = text;
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {

        ItemBean itemBean = (ItemBean) o;
        return id == itemBean.id &&
                viewType == itemBean.viewType &&
                TextUtils.equals(text, itemBean.text);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, viewType, text);
    }
}
