package com.tencent.lib.multi.core.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Author：岑胜德 on 2021/12/15 03:17
 * <p>
 * 说明：绑定 item view 点击事件（包括长点击事件）。
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface BindItemViewClickEvent {
    String value() default "";
}
