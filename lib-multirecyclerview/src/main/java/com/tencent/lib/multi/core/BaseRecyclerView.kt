package com.tencent.lib.multi.core

import android.content.Context
import android.util.AttributeSet
import androidx.recyclerview.widget.RecyclerView

/**

 * Author：岑胜德 on 2021/2/20 16:15

 * 说明：

 */
 abstract class BaseRecyclerView<T>(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : RecyclerView(context, attrs, defStyleAttr) {
    private lateinit var itemManager: ItemManager<*>
    private lateinit var checkManager: CheckManager
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

    fun getItemManager() = itemManager

    fun getCheckManager() = checkManager
    override fun setAdapter(adapter: Adapter<*>?) {
        if (adapter is ItemManager<*>) {
            itemManager = adapter
        }
        if (adapter is CheckManager) {
            checkManager = adapter
        }
        super.setAdapter(adapter)
    }
}