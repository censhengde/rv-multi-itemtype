package com.tencent.lib.multi.core;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.viewbinding.ViewBinding;

import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;

import org.jetbrains.annotations.NotNull;

/**
 * Author：岑胜德 on 2021/8/30 10:58
 * <p>
 * 说明：支持 ViewBinding用法的  MultiItem。
 */
public abstract class SimpleItemType<T, VB extends ViewBinding> extends MultiItemType<T, MultiViewHolder> {


    private Method mBindMethod;

    @NotNull
    protected VB onCreateViewBinding(ViewGroup parent, @NotNull LayoutInflater inflater) {
        VB vb = null;
        try {
            if (mBindMethod == null) {
                final Type type = this.getClass().getGenericSuperclass();
                final ParameterizedType p = (ParameterizedType) type;
                //MultiVBItemType的孙类以下如果不透传 VB 泛型参数到其父类就会获取Class对象失败,
                //此时解决方案就是全盘重写 onCreateViewBinding(ViewGroup parent) 方法，手动创建
                //ViewBinding 实现类的实例
                final Class<VB> c = (Class<VB>) p.getActualTypeArguments()[1];
                //Gradle 开启 viewBinding 后会自动生成 ViewBinding 的实现类，其中就有 bind(View root) 静态方法，
                // 该方法用于创建 ViewBinding 实现类的实例。
                mBindMethod = c.getMethod("inflate", LayoutInflater.class, ViewGroup.class, boolean.class);
            }
            vb = (VB) mBindMethod.invoke(null, inflater, parent, false);
        } catch (Exception e) {
            e.printStackTrace();
            throw new IllegalStateException("反射创建 ViewBinding 失败:" + e.getMessage());
        }
        if (vb == null) {
            throw new IllegalStateException("反射创建 ViewBinding 失败：vb==" + vb);
        }
        return vb;
    }


    /**
     * 这里调用 ViewHolder 的构造方法不一样，所以需要重写此方法手动创建 ViewHolder。
     *
     * @param parent
     * @return
     */
    @NotNull
    @Override
    public MultiViewHolder onCreateViewHolder(@NotNull ViewGroup parent) {
        final LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        final VB vb = onCreateViewBinding(parent, inflater);
        final MultiViewHolder holder = new MultiViewHolder(vb);
        onViewHolderCreated(holder, vb);
        return holder;
    }

    protected void onViewHolderCreated(@NonNull MultiViewHolder holder,
                                       @NotNull VB binding) {

    }

    /**
     * 加 final修饰 让子类无法重写此方法。
     *
     * @param holder
     * @param bean
     * @param position
     * @param payloads
     */
    @Override
    public final void onBindViewHolder(@NotNull MultiViewHolder holder, @NonNull @NotNull T bean, int position,
                                       @NonNull @NotNull List<Object> payloads) {
        /*这里直接 将ViewHolder 转换成 ViewBinding，让子类获取控件代码更简洁！*/
        onBindViewHolder((VB) holder.vb, bean, position, payloads);

    }

    @Override
    public final void onBindViewHolder(@NonNull @NotNull MultiViewHolder holder, @NonNull @NotNull T bean,
                                       int position) {
    }

    protected void onBindViewHolder(@NotNull VB vb, @NotNull T bean, int position, @NotNull List<Object> payloads) {
        onBindViewHolder(vb, bean, position);
    }

    protected abstract void onBindViewHolder(@NotNull VB vb, @NotNull T bean, int position);
}
