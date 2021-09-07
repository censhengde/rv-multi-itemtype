package com.tencent.multiadapter.example.bean;

import com.tencent.lib.multi.core.checking.Checkable;

/**
 * Author：岑胜德 on 2021/5/12 17:24
 *
 * 说明：简单的列表单选/多选直接继承 SimpleCheckable 即可，
 *      更复杂的列表选择请实现Checkable接口
 * */
public class CheckableBean implements Checkable {

    public static final int VIEW_TYPE_HEADER=1;/*头布局标识位*/
    public static final int VIEW_TYPE_CHECKABLE = 0;/*可选中的Item标识位*/
    public static final int VIEW_TYPE_FOOTER=2;/*脚布局标识位*/
    public int viewType = VIEW_TYPE_CHECKABLE;/*默认是可选中item*/

    private boolean mIsChecked;/*判断当前item是否被选中*/

    public String text="";
    public CheckableBean(int viewType, String text) {
        this.viewType = viewType;
        this.text = text;
    }

    @Override
    public void setChecked(boolean checked) {
        /*头布局和脚布局是不可选的。注意，只有这里的被选中规则定义得准确，
         *后面调用CheckingHelper finishedChecking才能准确甄选出被选中的item
         */
        mIsChecked = checked && viewType == VIEW_TYPE_CHECKABLE;
    }

    @Override
    public boolean isChecked() {
        return mIsChecked;
    }
}
