package com.tencent.lib.widget

import android.content.Context
import android.util.AttributeSet

/**
 * Author：岑胜德 on 2021/1/6 14:51
 *
 *
 * 说明：
 */
open class MultiRecyclerView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : BaseRecyclerView(context, attrs, defStyleAttr) {

    fun linearBuilder(): LinearBuilder {
        return LinearBuilder(this)
    }

    fun gridBuilder(span: Int): GridBuilder {
        return GridBuilder(this, span)
    }

    fun staggeredGridBuilder(gapStrategy: Int): StaggeredGridBuilder {
        return StaggeredGridBuilder(this, gapStrategy)
    }

   open fun adapterBuilder() = super.commonAdapterBuilder()
    fun getItemManager() =adapter as ItemManager<*>


    fun completed(){
        ( adapter as CheckedAdapter<*>).complete()
    }
}