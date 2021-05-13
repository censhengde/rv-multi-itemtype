package com.tencent.lib.multi.core.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.List;

/**
 * Author：岑胜德 on 2021/4/6 14:29
 *
 * 说明：
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface OnClickItemChildView {

        String rv() default "";/*用于标识是哪个RecyclerView控件的Item点击*/
        String it() default "";/*用于标识是哪一ItemType的Item点击*/
        String[] tags();
}
