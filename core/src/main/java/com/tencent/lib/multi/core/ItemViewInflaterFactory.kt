package com.tencent.lib.multi.core

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import java.lang.ref.WeakReference

/**

 * Author：岑胜德 on 2022/5/20 23:51

 * 说明：

 */
open class ItemViewInflaterFactory : LayoutInflater.Factory2 {

    private var viewClickRegistryRef: WeakReference<ViewClickRegistry>? = null

    fun setViewClickRegistry(viewClickRegistry: ViewClickRegistry) {
        if (viewClickRegistryRef?.get() != viewClickRegistry) {
            viewClickRegistryRef = WeakReference(viewClickRegistry)
        }
    }

    override fun onCreateView(
        parent: View?,
        name: String,
        context: Context,
        attrs: AttributeSet
    ): View? = createView(context, name, attrs)?.also {
        viewClickRegistryRef?.get()?.collectView(it, name, attrs)
    }

    override fun onCreateView(name: String, context: Context, attrs: AttributeSet): View? = null

    private fun createView(context: Context, name: String, attrs: AttributeSet): View? {
        var view: View? = null
        try {
            if (-1 == name.indexOf('.')) {
                if ("View" == name) {
                    view = LayoutInflater.from(context).createView(name, "android.view.", attrs)
                }
                if (view == null) {
                    view = LayoutInflater.from(context).createView(name, "android.widget.", attrs)
                }
                if (view == null) {
                    view = LayoutInflater.from(context).createView(name, "android.webkit.", attrs)
                }
            } else {
                view = LayoutInflater.from(context).createView(name, null, attrs)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            view = null
        }
        return view
    }
}