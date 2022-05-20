package com.tencent.lib.multi.core

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.collection.SimpleArrayMap
import androidx.core.view.LayoutInflaterCompat
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.tencent.lib.itemType.R
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
    protected var viewClickRegistry: ViewClickRegistry? = null
    private var factory: ItemViewInflaterFactory? = null

    fun bind(clickEventReceiver: Any) {
        this.clickEventReceiver = clickEventReceiver
    }

    final override fun onCreateViewHolder(parent: ViewGroup): MultiViewHolder {
        val vb = onCreateViewBinding(parent, getLayoutInflater(parent))
        val holder = MultiViewHolder(vb)
        onViewHolderCreated(holder, vb)
        return holder
    }

    /**
     * 将来有可能需要配合动态换肤框架使用，届时有可能需要重写此方法。
     */
    protected open fun getLayoutInflater(parent: ViewGroup): LayoutInflater =
        LayoutInflater.from(parent.context).also {
            if (viewClickRegistry == null) {
                viewClickRegistry =
                    ViewClickRegistry(this as ItemType<Any, RecyclerView.ViewHolder>)
            }
            // 如果已经设置了 ItemViewInflaterFactory，则更新 ViewClickRegistry
            if (it.factory2 is ItemViewInflaterFactory) {
                (it.factory2 as ItemViewInflaterFactory).setViewClickRegistry(viewClickRegistry!!)
            } else {
                forceSetFactory2(it, ItemViewInflaterFactory().also {
                    it.setViewClickRegistry(viewClickRegistry!!)
                })
            }
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
        clickEventReceiver ?: return
        viewClickRegistry?.register(clickEventReceiver!!, holder)
        viewClickRegistry?.clearAllNeedRegisterViews()
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


    @SuppressLint("DiscouragedPrivateApi")
    protected fun forceSetFactory2(inflater: LayoutInflater, factory2: LayoutInflater.Factory2) {
        val inflaterClass = LayoutInflater::class.java
        try {
            val mFactory2 = inflaterClass.getDeclaredField("mFactory2")
            mFactory2.isAccessible = true
            mFactory2.set(inflater, factory2)
        } catch (e: IllegalAccessException) {
            e.printStackTrace()
        } catch (e: NoSuchFieldException) {
            e.printStackTrace()
        }
    }

}