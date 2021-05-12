package com.tencent.lib.multi.core;

import android.os.Parcel;
import android.os.Parcelable;
import com.tencent.lib.multi.core.Checkable;

/**
 * Author：岑胜德 on 2021/2/22 15:36
 *
 * 说明：简单的 Checkable 实现类
 */
public abstract class SimpleCheckable implements Checkable {

    private boolean isChecked;
    @Override
    public void setChecked(boolean checked) {
        this.isChecked = checked;
    }

    @Override
    public boolean isChecked() {
        return isChecked;
    }

}
