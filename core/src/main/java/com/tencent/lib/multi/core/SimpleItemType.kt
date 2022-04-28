package com.tencent.lib.multi.core

import android.R
import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.collection.SimpleArrayMap
import androidx.viewbinding.ViewBinding
import java.lang.reflect.Method
import java.lang.reflect.ParameterizedType

/**
 * Author：岑胜德 on 2021/8/30 10:58
 *
 *
 * 说明：支持 ViewBinding用法。
 */
@Suppress("UNCHECKED_CAST")
abstract class SimpleItemType<T, VB : ViewBinding>(private var clickEventReceiver: Any? = null) :
    ItemType<T, MultiViewHolder>() {
    companion object {
        private const val TAG = "SimpleItemType"
    }

    private var mBindMethod: Method? = null
    private val targetViewIds by lazy {
        SimpleArrayMap<Int, String>()
    }
    private var layoutInflater: ItemLayoutInflater? = null

    fun bind(clickEventReceiver: Any) {
        this.clickEventReceiver = clickEventReceiver
    }

    final override fun onCreateViewHolder(parent: ViewGroup): MultiViewHolder {
        if (layoutInflater == null) {
            val original = LayoutInflater.from(parent.context)
            ItemLayoutInflater(original = original, parent.context).also {
                layoutInflater = it
            }
        }
        val vb = onCreateViewBinding(parent, layoutInflater!!)
        val holder = MultiViewHolder(vb)
        onViewHolderCreated(holder, vb)
        layoutInflater?.clearAllNeedRegisterViews()
        return holder
    }

    protected open fun onCreateViewBinding(parent: ViewGroup, inflater: LayoutInflater): VB {
        var vb: VB? = null
        try {
            if (mBindMethod == null) {
                val type = this.javaClass.genericSuperclass
                val p = type as ParameterizedType
                // 孙类以下如果不透传 VB 泛型参数到其父类就会获取Class对象失败,
                // 此时解决方案就是全盘重写 onCreateViewBinding(ViewGroup parent) 方法，手动创建
                // ViewBinding 实现类的实例
                val c = p.actualTypeArguments[1] as Class<VB>
                // Gradle 开启 ViewBinding 后会自动生成 ViewBinding 的实现类，其中就有 inflate 静态方法，
                // 该方法用于创建 ViewBinding 实现类的实例。(注意配置忽略 ViewBinding 混淆！！！)
                mBindMethod = c.getMethod(
                    "inflate",
                    LayoutInflater::class.java,
                    ViewGroup::class.java,
                    Boolean::class.javaPrimitiveType
                )
            }
            vb = mBindMethod!!.invoke(null, inflater, parent, false) as VB
        } catch (e: Exception) {
            e.printStackTrace()
            throw IllegalStateException("反射创建 ViewBinding 失败:" + e.message)
        }
        return vb
    }

    protected open fun onViewHolderCreated(
        holder: MultiViewHolder,
        vb: VB
    ) {
        layoutInflater ?: return
        clickEventReceiver ?: return
        // 注册点击事件
        repeat(layoutInflater!!.needRegisterClickEventViews?.size() ?: 0) {
            layoutInflater!!.needRegisterClickEventViews?.keyAt(it)?.apply {
                layoutInflater!!.needRegisterClickEventViews?.valueAt(it)?.let { name ->
                    registerClickEvent(clickEventReceiver!!, holder, this, name)
                }
            }

        }
        // 注册长点击事件
        repeat(layoutInflater!!.needRegisterLongClickEventViews?.size() ?: 0) {
            layoutInflater!!.needRegisterLongClickEventViews?.keyAt(it)?.apply {
                layoutInflater!!.needRegisterLongClickEventViews?.valueAt(it)?.let { name ->
                    registerLongClickEvent(clickEventReceiver!!, holder, this, name)
                }
            }
        }


    }

    /**
     * 加 final修饰向子类屏蔽此方法。
     *
     * @param holder
     * @param bean
     * @param position
     * @param payloads
     */
    final override fun onBindViewHolder(
        holder: MultiViewHolder, bean: T, position: Int,
        payloads: List<Any>
    ) {
        /*这里直接 将ViewHolder 转换成 ViewBinding，让子类获取控件代码更简洁！*/
        onBindView(holder.vb as VB, bean, position, payloads)
    }


    final override fun onBindViewHolder(holder: MultiViewHolder, bean: T, position: Int) {
    }

    protected open fun onBindView(vb: VB, bean: T, position: Int, payloads: List<Any>) {
        onBindView(vb, bean, position)
    }

    protected abstract fun onBindView(vb: VB, bean: T, position: Int)

}