@file:JvmName("Utils")
package com.tencent.lib.multi.core

import java.lang.IllegalArgumentException

/**

 * Author：岑胜德 on 2021/2/20 15:15

 * 说明：

 */
fun assertNull(any: Any?,msg:String){
    if (any==null){
        throw IllegalArgumentException(msg)
    }
}

