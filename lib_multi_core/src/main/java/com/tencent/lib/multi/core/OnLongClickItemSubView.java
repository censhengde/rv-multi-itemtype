package com.tencent.lib.multi.core;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Author：岑胜德 on 2021/4/6 15:02
 *
 * 说明：
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface OnLongClickItemSubView {
    /*用于标识是哪个RecyclerView控件的Item点击*/
    String key() default "";
    String[] tags();
}
