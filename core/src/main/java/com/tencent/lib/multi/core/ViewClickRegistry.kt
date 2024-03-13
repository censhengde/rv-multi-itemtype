package com.tencent.lib.multi.core

import android.util.AttributeSet
import android.util.Log
import android.view.View
import androidx.collection.SimpleArrayMap
import androidx.recyclerview.widget.RecyclerView
import com.tencent.lib.itemType.R

/**

 * Author：岑胜德 on 2022/5/20 23:32

 * 说明：

 */
class ViewClickRegistry(private val itemType: ItemType<Any, RecyclerView.ViewHolder>) {
    companion object {
        private const val TAG = "ViewClickRegistry"
    }
    // xml文件中声明了 linkClick 属性的View
    private var needRegisterClickEventViews: SimpleArrayMap<View, String>? = null

    // xml文件中声明了 linkLongClick 属性的View
    private var needRegisterLongClickEventViews: SimpleArrayMap<View, String>? = null

    fun register(clickEventReceiver: Any, holder: RecyclerView.ViewHolder) {
        // 注册点击事件
        repeat(needRegisterClickEventViews?.size() ?: 0) {
            needRegisterClickEventViews?.keyAt(it)?.apply {
                needRegisterClickEventViews?.valueAt(it)?.let { name ->
                    itemType.registerClickEvent(clickEventReceiver, holder, this, name)
                }
            }

        }
        // 注册长点击事件
        repeat(needRegisterLongClickEventViews?.size() ?: 0) {
            needRegisterLongClickEventViews?.keyAt(it)?.apply {
                needRegisterLongClickEventViews?.valueAt(it)?.let { name ->
                    itemType.registerLongClickEvent(clickEventReceiver, holder, this, name)
                }
            }
        }
    }

     fun collectView(view: View, name: String, attrs: AttributeSet) {
        val a = view.context.obtainStyledAttributes(attrs, R.styleable.ItemView)
        a.getString(R.styleable.ItemView_linkClick)?.apply {
            if (needRegisterClickEventViews == null) {
                needRegisterClickEventViews = SimpleArrayMap()
            }
            needRegisterClickEventViews?.put(view, this) // 将需要注册点击事件的 View 收集起来。

        }
        a.getString(R.styleable.ItemView_linkLongClick)?.apply {
            if (needRegisterLongClickEventViews == null) {
                needRegisterLongClickEventViews = SimpleArrayMap()
            }
            needRegisterLongClickEventViews?.put(view, this)
        }
        a.recycle()
    }

    fun clearAllNeedRegisterViews() {
        needRegisterClickEventViews?.clear()
        needRegisterLongClickEventViews?.clear()
    }
}