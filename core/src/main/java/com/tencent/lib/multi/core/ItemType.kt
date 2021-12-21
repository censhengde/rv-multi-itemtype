package com.tencent.lib.multi.core

import android.util.ArrayMap
import android.util.Log
import android.view.View
import android.view.ViewGroup
import androidx.annotation.CallSuper
import androidx.recyclerview.widget.RecyclerView
import com.tencent.lib.multi.core.annotation.BindItemViewClickEvent
import com.tencent.lib.multi.core.listener.OnClickItemViewListener
import com.tencent.lib.multi.core.listener.OnLongClickItemViewListener
import java.lang.reflect.Method

/**
 * Author：岑胜德 on 2021/2/22 16:23
 *
 *
 * 说明：某一种类型 item 的抽象。
 */
abstract class ItemType<T, VH : RecyclerView.ViewHolder> {

    private var mMethodCachePool: Map<String, Method>? = null /*缓存反射获取的method对象，减少反射成本*/
    private var mOnClickItemViewListener: OnClickItemViewListener<T?>? = null
    private var mOnLongClickItemViewListener: OnLongClickItemViewListener<T?>? = null
    private var clickEventReceiver: Any? = null /*item view 点击事件的接收者*/

    private var mManager: MultiItemManager? = null
    private val methodCachePool: Map<String, Method>
        get() {
            if (mMethodCachePool == null) {
                mMethodCachePool = ArrayMap()
            }
            return mMethodCachePool!!
        }

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

    val manager: MultiItemManager
        get() {
            checkNotNull(mManager) { "ItemType $this not attached to an MultiHelper." }
            return mManager as MultiItemManager
        }

    open fun getItemId(position: Int): Long {
        return RecyclerView.NO_ID
    }

    /**
     * 当前 position 是否与当前 ItemType 匹配。这个方法是实现多样式 item 的关键！
     * 如若此方法实现错误，那将导致某position上匹配不到ItemType，进而引发程序崩溃！
     *
     * @param bean     当前 position 对应的实体对象
     * @param position 当前 position
     * @return true 表示匹配；否则不匹配。
     */
    open fun isMatchForMe(bean: Any?, position: Int): Boolean = true

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

    fun setOnLongClickItemViewListener(listener: OnLongClickItemViewListener<T?>?) {
        mOnLongClickItemViewListener = listener
    }

    fun setOnClickItemViewListener(itemListener: OnClickItemViewListener<T?>?) {
        mOnClickItemViewListener = itemListener
    }

    /**
     * 注册 item view 点击事件。
     */
    protected fun registerClickEvent(holder: VH, view: View, method: String?) {
        view.isClickable = true
        view.setOnClickListener { v: View ->
            val position = holder.adapterPosition
            if (position == RecyclerView.NO_POSITION) {
                Log.e(TAG, "item 点击异常: position=$position")
                return@setOnClickListener
            }
            val data = manager.getItem(position)
            if (checkIsNull(data, "item 点击异常:data is null")) {
                return@setOnClickListener
            }
            //优先监听器
            if (mOnClickItemViewListener != null) {
                mOnClickItemViewListener!!.onClickItemView(v, this, data as T, position)
                return@setOnClickListener
            }
            callTagMethod(v, method, data, position, "item 点击异常")
        }
    }

    /**
     * 注册 item view 长点击事件。
     */
    protected fun registerLongClickEvent(holder: VH, view: View, method: String?) {
        view.isLongClickable = true
        view.setOnLongClickListener { v: View ->
            var consume = false
            val position = holder.adapterPosition
            if (position == RecyclerView.NO_POSITION) {
                Log.e(TAG, "item 长点击异常: position=$position")
                return@setOnLongClickListener consume
            }
            val data = manager.getItem(position)
            if (checkIsNull(data, "item 长点击异常:data is null")) {
                return@setOnLongClickListener consume
            }
            //监听器优先
            if (mOnLongClickItemViewListener != null) {
                return@setOnLongClickListener mOnLongClickItemViewListener!!.onLongClickItemView(v, this, data as T, position)
            }
            consume = callTagLongClickMethod(v, method, data, position, "item 长点击异常")
            consume
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
    private fun callTagMethod(v: View, methodName: String?, data: Any?, position: Int, errMsg: String) {
        try {
            val method = methodCachePool[methodName]
            if (checkIsNull(method, "callTagMethod method is null")) {
                return
            }
            assert(method != null)
            if (!method!!.isAccessible) {
                method.isAccessible = true
            }
            method.invoke(clickEventReceiver, v, data, position)
        } catch (e: Exception) {
            Log.e(TAG, errMsg + ":" + e.message)
        }
    }

    /**
     * 反射回调长点击方法
     *
     * @param v
     * @param data
     * @param position
     * @param errMsg
     * @return
     */
    private fun callTagLongClickMethod(v: View, target: String?, data: Any?, position: Int, errMsg: String): Boolean {
        var consume = false
        try {
            val method = methodCachePool[target]
            if (checkIsNull(method, "callTagLongClickMethod method is null")) {
                return consume
            }
            assert(method != null)
            if (!method!!.isAccessible) {
                method.isAccessible = true
            }
            consume = method.invoke(clickEventReceiver, v, data, position) as Boolean
        } catch (e: Exception) {
            Log.e(TAG, "===>callTagLongClickMethod $errMsg:${e.message} ")
        }
        return consume
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