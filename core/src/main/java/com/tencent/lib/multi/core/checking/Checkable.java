package com.tencent.lib.multi.core.checking;

import android.os.Parcelable;

/**
 * Author：岑胜德 on 2021/2/5 15:51
 *
 * 说明：标识该Item是可被选中的
 */
public interface Checkable  {
    /*设置是否被选中，注意，复杂的item是否被选中规则一定要注意此方法的实现，
      *不要局限于单纯搞个boolean 变量做判断。
     */
    void setChecked(boolean checked);
    /*判断是否被选中*/
    boolean isChecked();
}
