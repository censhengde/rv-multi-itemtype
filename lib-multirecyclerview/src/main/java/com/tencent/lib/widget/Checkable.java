package com.tencent.lib.widget;

/**
 * Author：岑胜德 on 2021/2/5 15:51
 *
 * 说明：标识该对象是可被选中的
 */
public interface Checkable {
    /*设置是否被选中*/
    void setChecked(boolean checked);
    /*获取选择状态*/
    boolean isChecked();
}
