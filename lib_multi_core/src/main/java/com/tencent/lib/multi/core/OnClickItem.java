package com.tencent.lib.multi.core;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Author：岑胜德 on 2021/4/6 14:21
 *
 * 说明：
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface OnClickItem {
    /*用于标识是哪个控件的Item点击*/
    String rv() default "";
    String it() default "";
}
