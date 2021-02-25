package com.tencent.lib.multi

import android.content.Context
import android.util.AttributeSet

/**
 * Author：岑胜德 on 2021/1/6 14:51
 *
 *
 * 说明：
 */
open class MultiRecyclerView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) :
        BaseRecyclerView<MultiAdapter.Builder>(context, attrs, defStyleAttr) {
    private lateinit var itemManager: ItemManager<*>
    private lateinit var checkManager: CheckManager

   override fun adapterBuilder() = MultiAdapter.Builder(this)
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