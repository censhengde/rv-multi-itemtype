package com.tencent.lib.multi.core

import android.util.ArrayMap
import android.util.Log
import android.view.View
import android.view.ViewGroup
import androidx.annotation.CallSuper
import androidx.annotation.IdRes
import androidx.recyclerview.widget.RecyclerView
import com.tencent.lib.multi.core.annotation.BindItemViewClickEvent
import java.lang.reflect.Method

/**
 * Author：岑胜德 on 2021/2/22 16:23
 * 说明：某一种类型 item 的抽象。
 */

abstract class ItemType<T, VH : RecyclerView.ViewHolder> {

    private var mMethodCachePool: Map<String, Method>? = null /*缓存反射获取的method对象，减少反射成本*/
    private var clickEventReceiver: Any? = null /*item view 点击事件的接收者*/
    private var mManager: MultiItemManager? = null

    fun inject(receiver: Any) = this.inject(receiver, null)


    fun inject(receiver: Any, methodCachePool: Map<String, Method>? = null) {
        if (checkIsNull(receiver, "inject receiver is null")) {
            return
        }
        clickEventReceiver = receiver
        mMethodCachePool = methodCachePool
        if (mMethodCachePool == null) {
            mMethodCachePool = createMethodCachePool(receiver)
        }
    }

    @CallSuper
    open fun onAttach(manager: MultiItemManager) {
        mManager = manager
    }

    protected val manager: MultiItemManager
        get() {
            checkNotNull(mManager) { "ItemType $this not attached to an MultiItemManager." }
            return mManager as MultiItemManager
        }


    /**
     * 当前 position 是否与当前 ItemType 匹配。这个方法是实现多样式 item 的关键！
     * 如若此方法实现错误，那将导致某position上匹配不到ItemType 而抛异常！
     *
     * @param bean     当前 position 对应的实体对象
     * @param position 当前 position
     * @return true 表示匹配；否则不匹配。
     */
    open fun isMatchForMe(bean: Any?, position: Int): Boolean = true

    /*=========以下是代理 RecyclerView.Adapter 中的方法============================*/

    open fun getItemId(position: Int): Long = RecyclerView.NO_ID

    abstract fun onCreateViewHolder(parent: ViewGroup): VH

    open fun onBindViewHolder(holder: VH, bean: T, position: Int,
                              payloads: List<Any>) {
        onBindViewHolder(holder, bean, position)
    }

    abstract fun onBindViewHolder(holder: VH, bean: T, position: Int)

    open fun onViewRecycled(holder: VH) {}

    open fun onFailedToRecycleView(holder: VH): Boolean = false

    open fun onViewAttachedToWindow(holder: VH) {}

    open fun onViewDetachedFromWindow(holder: VH) {}

    open fun onAttachedToRecyclerView(recyclerView: RecyclerView) {}

    open fun onDetachedFromRecyclerView(recyclerView: RecyclerView) {}

    /*=============================================================================*/

    /**
     * 注册 item view 点击事件。
     */
    protected fun registerClickEvent(holder: VH, @IdRes id: Int, event: String) {
        registerClickEvent(holder, holder.itemView.findViewById(id), event)
    }

    protected fun registerClickEvent(holder: VH, view: View, event: String) {
        view.isClickable = true
        view.setOnClickListener { v: View ->
            val position = holder.adapterPosition
            if (position !in 0 until manager.adapter.itemCount) {
                Log.e(TAG, "item 点击异常: position=$position")
                return@setOnClickListener
            }
            val data = manager.getItem(position)
            if (checkIsNull(data, "item 点击异常:data is null")) {
                return@setOnClickListener
            }
            callTargetMethod(v, event, data!!, position, false)
        }
    }

    /**
     * 注册 item view 长点击事件。
     */
    protected fun registerLongClickEvent(holder: VH, @IdRes id: Int, event: String) {
        registerLongClickEvent(holder, holder.itemView.findViewById(id), event)
    }

    protected fun registerLongClickEvent(holder: VH, view: View, event: String) {
        view.isLongClickable = true
        view.setOnLongClickListener { v: View ->
            var consume = false
            val position = holder.adapterPosition
            if (position !in 0 until manager.adapter.itemCount) {
                Log.e(TAG, "item 长点击异常: position=$position")
                return@setOnLongClickListener consume
            }
            val data = manager.getItem(position)
            checkIsNull(data, "item 长点击异常:data is null")
            data?.let {
                consume = callTargetMethod(v, event, it, position, true)
                        ?: false
            }
            return@setOnLongClickListener consume
        }
    }

    /**
     * 反射回调点击方法
     *
     * @param v
     * @param data
     * @param position
     * @param errMsg
     */
    private fun callTargetMethod(v: View, event: String, data: Any, position: Int, long: Boolean): Boolean? {
        var result: Boolean? = null
        try {
            checkIsNull(mMethodCachePool, "callTagMethod mMethodCachePool is null")
            mMethodCachePool?.let { pool ->
                val method = pool[event]
                checkIsNull(method, "callTagMethod method is null")
                method?.let { m ->
                    if (!m.isAccessible) {
                        method.isAccessible = true
                    }
                    result = if (long) {
                        m.invoke(clickEventReceiver, v, data, position) as Boolean
                    } else {
                        m.invoke(clickEventReceiver, v, data, position)
                        null
                    }
                }
            }
        } catch (e: Exception) {
            Log.e(TAG, "callTargetMethod: ${e.message}")
        }
        return result
    }

    private fun checkIsNull(o: Any?, msg: String): Boolean {
        if (o == null) {
            Log.e(TAG, "===>$msg")
        }
        return o == null
    }

    companion object {

        private const val TAG = "ItemType"

        @JvmStatic
        fun createMethodCachePool(clickEventReceiver: Any): Map<String, Method> {
            val pool: MutableMap<String, Method> = ArrayMap()
            try {
                val clazz = clickEventReceiver.javaClass
                val methods = clazz.declaredMethods
                for (m in methods) {
                    val annotation = m.getAnnotation(BindItemViewClickEvent::class.java)
                    annotation?.let {
                        pool[it.value] = m
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
            return pool
        }
    }
}