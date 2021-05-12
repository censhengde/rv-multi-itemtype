package com.tencent.lib.multi.core

import android.content.Context
import android.util.AttributeSet
import androidx.recyclerview.widget.RecyclerView
import com.tencent.lib.multi.R

/**

 * Author：岑胜德 on 2021/2/20 16:15

 * 说明：

 */
abstract class BaseRecyclerView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : RecyclerView(context, attrs, defStyleAttr) {
    var singleSelection: Boolean
    private var itemTypes: MutableList<ItemType<*>>? = null

    init {
        val a = context.obtainStyledAttributes(attrs, R.styleable.BaseRecyclerView)
        singleSelection = a.getBoolean(R.styleable.BaseRecyclerView_singleSelection, false)
        a.recycle()
    }

    fun linearBuilder(): LinearBuilder {
        return LinearBuilder(this)
    }

    fun gridBuilder(span: Int): GridBuilder {
        return GridBuilder(this, span)
    }

    fun staggeredGridBuilder(gapStrategy: Int): StaggeredGridBuilder {
        return StaggeredGridBuilder(this, gapStrategy)
    }


}