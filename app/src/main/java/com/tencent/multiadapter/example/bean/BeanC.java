package com.tencent.multiadapter.example.bean;

/**
 * Author：岑胜德 on 2021/5/12 17:24
 *
 * 说明：简单的列表单选/多选直接继承 SimpleCheckable 即可，
 *      更复杂的列表选择请实现Checkable接口
 * */
public class BeanC {

    public static final int VIEW_TYPE_HEADER=1;/*头布局标识位*/
    public static final int VIEW_TYPE_CHECKABLE = 0;/*可选中的Item标识位*/
    public static final int VIEW_TYPE_FOOTER=2;/*脚布局标识位*/
    public int viewType = VIEW_TYPE_CHECKABLE;/*默认是可选中item*/

    public String text="";
    public BeanC(int viewType, String text) {
        this.viewType = viewType;
        this.text = text;
    }

}
