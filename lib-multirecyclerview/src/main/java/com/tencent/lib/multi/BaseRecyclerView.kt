package com.tencent.lib.multi

import android.content.Context
import android.util.AttributeSet
import androidx.recyclerview.widget.RecyclerView

/**

 * Author：岑胜德 on 2021/2/20 16:15

 * 说明：

 */
 abstract class BaseRecyclerView<T>(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : RecyclerView(context, attrs, defStyleAttr) {
    var checkable = false//是否开启列表单选、多选功能


    fun linearBuilder(): LinearBuilder {
        return LinearBuilder(this)
    }

    fun gridBuilder(span: Int): GridBuilder {
        return GridBuilder(this, span)
    }

    fun staggeredGridBuilder(gapStrategy: Int): StaggeredGridBuilder {
        return StaggeredGridBuilder(this, gapStrategy)
    }

    abstract fun adapterBuilder():T
}