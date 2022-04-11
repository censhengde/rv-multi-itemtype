package com.tencent.lib.multi.core

import android.util.ArrayMap
import java.lang.reflect.Method

/**

 * Author：岑胜德 on 2022/4/7 11:13

 * 说明：

 */
internal class ReceiverWrapper(val receiver:Any) {
    val methods by lazy(LazyThreadSafetyMode.NONE) {
        ArrayMap<String, Method>()
    }
}
