package com.tencent.lib.multi.core

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import androidx.collection.SimpleArrayMap
import com.tencent.lib.itemType.R

/**

 * Author：岑胜德 on 2022/4/28 16:32

 * 说明：

 */
internal class ItemLayoutInflater @JvmOverloads constructor(
    original: LayoutInflater,
    newContext: Context?
) :
    LayoutInflater(original, newContext) {
    companion object {
        private const val TAG = "ItemLayoutInflater"
        private val sOnClickAttrs = intArrayOf(android.R.attr.onClick, R.attr.onLongClick)
    }

     var needRegisterClickEventViews: SimpleArrayMap<View, String>? = null


     var needRegisterLongClickEventViews: SimpleArrayMap<View, String>? = null

    override fun onCreateView(name: String?, attrs: AttributeSet?): View {
        return super.onCreateView(name, attrs).also {
            // 获取 android:onClick 属性值
            var a = it.context.obtainStyledAttributes(attrs, sOnClickAttrs)
            a.getString(0)?.apply {
                "===>$name android:onClick=$this".logI(TAG)
                if (needRegisterClickEventViews == null) {
                    needRegisterClickEventViews = SimpleArrayMap()
                }
                needRegisterClickEventViews?.put(it, this) // 将需要注册点击事件的 View 收集起来。
            }
            a.recycle()
            // 获取 app:onLongClick 属性值
            a = it.context.obtainStyledAttributes(attrs, R.styleable.ItemView)
            a.getString(R.styleable.ItemView_onLongClick)?.apply {
                "===> $name app:onLongClick=$this".logI(TAG)
                if (needRegisterLongClickEventViews == null) {
                    needRegisterLongClickEventViews = SimpleArrayMap()
                }
                needRegisterLongClickEventViews?.put(it, this)
            }
            a.recycle()
        }
    }

    fun clearAllNeedRegisterViews() {
        needRegisterClickEventViews?.clear()
        needRegisterLongClickEventViews?.clear()
    }

    override fun cloneInContext(newContext: Context?): LayoutInflater =
        ItemLayoutInflater(this, newContext = newContext)


}