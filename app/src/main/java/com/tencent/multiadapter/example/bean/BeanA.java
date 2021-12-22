package com.tencent.multiadapter.example.bean;

import android.text.TextUtils;
import java.util.Objects;

/**
 * Author：岑胜德 on 2021/1/6 18:05
 * <p>
 * 说明：
 */
public class BeanA {

    //item具体业务数据字段
    public int id;
    public String text = "";

    public BeanA( String text) {
        this.text = text;
    }

}
