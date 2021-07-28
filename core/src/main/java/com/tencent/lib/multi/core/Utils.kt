@file:JvmName("Utils")

package com.tencent.lib.multi.core

/**

 * Author：岑胜德 on 2021/2/20 15:15

 * 说明：

 */
internal fun assertNull(any: Any?, msg: String) {
    if (any == null) {
        throw IllegalArgumentException(msg)
    }

}

