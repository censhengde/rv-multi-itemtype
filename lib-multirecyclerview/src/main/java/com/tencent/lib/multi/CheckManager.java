package com.tencent.lib.multi;

import android.os.Bundle;

/**
 * Author：岑胜德 on 2021/2/22 10:15
 *
 * 说明：
 */
public interface CheckManager {
    void checkAll();

    void cancelAll();

    void complete();

    void saveCheckedItem(Bundle out);

    void restoreCheckedItem(Bundle in);

}
