package com.tencent.lib.multi;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Author：岑胜德 on 2021/2/22 15:36
 *
 * 说明：简单的 Checkable 实现类
 */
public abstract class SimpleCheckable<T> implements Checkable {

    public final Parcelable.Creator<T> CREATOR = new Parcelable.Creator<T>() {

        @Override
        public T createFromParcel(Parcel source) {
            return null;
        }

        @Override
        public T[] newArray(int size) {
            return null;
        }
    };

    private boolean isChecked;

    @Override
    public void setChecked(boolean checked) {
        this.isChecked = checked;
    }

    @Override
    public boolean isChecked() {
        return isChecked;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

    }
}
