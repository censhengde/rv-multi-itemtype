package com.tencent.lib.widget

import android.content.Context
import android.util.AttributeSet
import androidx.recyclerview.widget.RecyclerView

/**

 * Author：岑胜德 on 2021/2/20 16:15

 * 说明：

 */
abstract class BaseRecyclerView(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : RecyclerView(context, attrs, defStyleAttr) {
    var checkable = false//是否开启列表单选、多选功能
    protected fun commonAdapterBuilder() = /*实现单选、复选的Builder*/CommonAdapterBuilder(this);

}