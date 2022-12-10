package com.tencent.lib.multi.core

import android.util.Log
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.NO_ID
import java.lang.IllegalArgumentException
import java.lang.reflect.InvocationTargetException
import java.lang.reflect.Method

/**
 * Author：岑胜德 on 2021/2/22 16:23
 * 说明：某一种类型 item 的抽象。
 */

abstract class ItemType<T, VH : RecyclerView.ViewHolder>() {

    companion object {
        private const val TAG = "ItemType"
    }

    private var mManager: ItemManager? = null


    internal fun onAttach(manager: ItemManager) {
        mManager = manager
    }

    protected val manager: ItemManager
        get() {
            checkNotNull(mManager) { "ItemType $this not attached to an ItemManager." }
            return mManager as ItemManager
        }


    /**
     * 当前 position 是否与当前 ItemType 匹配。这个方法是实现多样式 item 的关键！
     * 如若此方法实现错误，那将导致某position上匹配不到ItemType 而抛异常！
     *
     * @param bean     当前 position 对应的实体对象
     * @param position 当前 position
     * @return true 表示匹配；否则不匹配。
     */
    open fun isMatched(bean: Any?, position: Int): Boolean = true

    /*=========以下是代理 RecyclerView.Adapter 中的方法============================*/

    open fun getItemId(bean: T, position: Int) = NO_ID

    abstract fun onCreateViewHolder(parent: ViewGroup): VH

    open fun onBindViewHolder(
        holder: VH, bean: T, position: Int,
        payloads: List<Any>
    ) {
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
    fun registerClickEvent(receiver: Any, holder: VH, view: View, method: String) {
        view.setOnClickListener { v: View ->
            val position = holder.adapterPosition
            callTargetMethod(receiver, v, method, position, false)
        }
    }

    fun registerClickEvent(receiver: Any, position: Int, view: View, method: String) {
        view.setOnClickListener { v: View ->
            callTargetMethod(receiver, v, method, position, false)
        }
    }

    /**
     * 注册 item view 长点击事件。
     */
    fun registerLongClickEvent(receiver: Any, holder: VH, view: View, method: String) {
        view.setOnLongClickListener { v: View ->
            return@setOnLongClickListener callTargetMethod(
                receiver,
                v,
                method,
                holder.adapterPosition,
                true
            )
                ?: false
        }
    }

    fun registerLongClickEvent(receiver: Any, position: Int, view: View, method: String) {
        view.setOnLongClickListener { v: View ->
            return@setOnLongClickListener callTargetMethod(
                receiver,
                v, method, position, true
            ) ?: false
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
    private fun callTargetMethod(
        receiver: Any,
        v: View,
        methodName: String,
        position: Int,
        long: Boolean
    ): Boolean? = try {
        var result: Boolean? = null
        val data = manager.getItem(position)
        findWrapper(receiver).also {
            Log.i(TAG, "===> receiver:$receiver ReceiverWrapper:$it")
            findTargetMethod(it, methodName)?.run {
                if (!this.isAccessible) {
                    this.isAccessible = true
                }
                if (long) {
                    result = this.invoke(receiver, v, data, position) as Boolean
                } else {
                    this.invoke(receiver, v, data, position)
                }
            }
        }
        result
    } catch (e: InvocationTargetException) {
        e.printStackTrace()
        null
    } catch (e: IllegalAccessException) {
        e.printStackTrace()
        null
    } catch (e: IllegalAccessException) {
        e.printStackTrace()
        null
    }


    private fun findWrapper(receiver: Any): ReceiverWrapper {
        var wrapper: ReceiverWrapper?
        receiver.javaClass.also {
            wrapper = manager.cachePool[it]
            if (wrapper == null) {
                wrapper = ReceiverWrapper(receiver)
                manager.cachePool[it] = wrapper
            }
        }
        return wrapper!!
    }

    private fun findTargetMethod(wrapper: ReceiverWrapper, methodName: String): Method? {
        var method = wrapper.methods[methodName]
        if (method == null) {
            wrapper.receiver.javaClass.declaredMethods.forEach {
                // 需要用户确保目标方法不能被混淆。
                if (it.name == methodName) {
                    method = it
                    wrapper.methods[methodName] = method
                    return method
                }
            }
        }
        return method
    }

    fun <T> updateItem(position: Int, block: (bean: T) -> Any?) {
        manager.updateItem(position, block)
    }
}