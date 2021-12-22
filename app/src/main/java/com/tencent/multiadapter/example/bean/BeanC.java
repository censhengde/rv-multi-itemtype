package com.tencent.multiadapter.example.bean;

/**
 * Author：岑胜德 on 2021/5/12 17:24
 *
 * 说明：简单的列表单选/多选直接继承 SimpleCheckable 即可，
 *      更复杂的列表选择请实现Checkable接口
 * */
public class BeanC {

    public String text="";

    public BeanC( String text) {
        this.text = text;
    }
}
