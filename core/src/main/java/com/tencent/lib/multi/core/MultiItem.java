package com.tencent.lib.multi.core;

import static androidx.recyclerview.widget.RecyclerView.NO_ID;

import android.text.TextUtils;
import android.util.ArrayMap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.IdRes;
import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;
import androidx.recyclerview.widget.RecyclerView;

import com.tencent.lib.multi.core.listener.OnClickItemViewListener;
import com.tencent.lib.multi.core.listener.OnLongClickItemViewListener;

import java.lang.ref.WeakReference;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

import org.jetbrains.annotations.NotNull;

/**
 * Author：岑胜德 on 2021/2/22 16:23
 * <p>
 * 说明：某一种类型 item 的抽象。
 */
public abstract class MultiItem<T, VH extends RecyclerView.ViewHolder> {

    private static final String TAG = "MultiItem";

    private Map<String, Method> mMethodMap;/*缓存反射获取的method对象，减少反射成本*/
    private OnClickItemViewListener<T> mOnClickItemViewListener;
    private OnLongClickItemViewListener<T> mOnLongClickItemViewListener;
    private Object mObserver;/*item view 点击事件的接收者*/
    private String mObserverName;
    private Class<?> mEntityClass;/*泛型参数T的Class*/
    private Constructor<VH> mVHConstructor;
    // 我们希望本类型item相关的点击事件也集中到这里处理，故需要外部提供Activity和Fragment环境。
    private WeakReference<FragmentActivity> mActivityRef;
    private WeakReference<Fragment> mFragmentRef;


    public void onAttach(@Nullable FragmentActivity activity, @Nullable Fragment fragment) {

        mActivityRef = activity != null ? new WeakReference<>(activity) : null;
        mFragmentRef = fragment != null ? new WeakReference<>(fragment) : null;

        if (mActivityRef == null && mFragmentRef != null && mFragmentRef.get() != null) {
            mActivityRef = new WeakReference<>(mFragmentRef.get().requireActivity());
        }
    }

    @Nullable
    public final FragmentActivity getActivity() {
        FragmentActivity activity = null;
        if (mActivityRef != null) {
            activity = mActivityRef.get();
        }
        return activity;
    }

    @Nullable
    public final Fragment getFragment() {
        Fragment fragment = null;
        if (mFragmentRef != null) {
            fragment = mFragmentRef.get();
        }
        return fragment;
    }

    @NotNull
    public final FragmentActivity requireActivity() {
        final FragmentActivity activity = getActivity();
        if (activity == null) {
            throw new IllegalStateException("MultiItem " + this + " not attached to an activity.");
        }
        return activity;
    }

    @NotNull
    public final Fragment requireFragment() {
        final Fragment fragment = getFragment();
        if (fragment == null) {
            throw new IllegalStateException("MultiItem " + this + " not attached to an fragment.");
        }
        return fragment;
    }


    /**
     * @return 返回当前ItemType的布局文件id
     */
    @LayoutRes
    protected abstract int getItemLayoutRes();

    public long getItemId(int position) {
        return NO_ID;
    }

    final int getItemType() {
        return this.getClass().hashCode();
    }

    /**
     * 当前 position 是否匹配当前 MultiItem。这个方法是实现多样式 item 的关键！
     * 如若此方法实现错误，那将导致某position上匹配不到ItemType，进而引发程序崩溃！
     *
     * @param bean     当前 position 对应的 实体对象
     * @param position 当前 position
     * @return true 表示匹配；否则不匹配。
     */
    public boolean isMatchForMe(Object bean, int position) {
        return true;
    }

    /**
     * 创建 ViewHolder
     *
     * @param parent
     * @return
     */
    @NonNull
    public VH onCreateViewHolder(@NonNull ViewGroup parent) {
        final View itemView = LayoutInflater.from(parent.getContext()).inflate(getItemLayoutRes(), parent, false);
        return resolveVH(itemView);
    }

    /**
     * 反射实例化ViewHolder
     *
     * @param itemView
     * @return
     */
    private VH resolveVH(View itemView) {
        final VH vh;
        try {
            if (mVHConstructor == null) {
                final Type multi = this.getClass().getGenericSuperclass();
                final ParameterizedType p = (ParameterizedType) multi;
                /*AbstractItemType 的孙类以下如果不透传 VH 泛型参数到其父类则获取其Class对象失败，
                 *此时解决方案是全盘重写 onCreateViewHolder(@NonNull ViewGroup parent)方法，手动创建 ViewHolder
                 */
                final Class<VH> c = (Class<VH>) p.getActualTypeArguments()[1];
                mVHConstructor = c.getConstructor(View.class);
            }
            vh = mVHConstructor.newInstance(itemView);//要求所有VH必需开放参数为View的构造函数
        } catch (Exception e) {
            e.printStackTrace();
            throw new IllegalStateException("反射创建 ViewHolder 异常：" + e.getMessage());
        }
        return vh;
    }

    /**
     * 表示 ViewHolder 创建完成。这里适合进行item view事件监听注册。
     *
     * @param holder
     * @param helper
     */
    public void onViewHolderCreated(@NonNull VH holder, @NonNull MultiHelper<T, VH> helper) {

    }

    /**
     * 绑定数据
     *
     * @param holder
     * @param bean
     * @param position
     * @param payloads
     */
    public void onBindViewHolder(@NonNull VH holder, @NonNull T bean, int position,
                                 @NonNull List<Object> payloads) {
        onBindViewHolder(holder, bean, position);
    }

    /**
     * 绑定数据
     *
     * @param holder
     * @param bean
     * @param position
     */
    public abstract void onBindViewHolder(@NonNull VH holder, @NonNull T bean, int position);

    /**
     * View 已经回收。
     *
     * @param holder
     */
    public void onViewRecycled(@NotNull VH holder) {

    }

    public boolean onFailedToRecycleView(@NonNull VH holder) {
        return false;
    }

    public void onViewAttachedToWindow(@NonNull VH holder) {
    }

    public void onViewDetachedFromWindow(@NonNull VH holder) {
    }

    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
    }

    public void onDetachedFromRecyclerView(@NonNull RecyclerView recyclerView) {
    }

    public void setOnLongClickItemViewListener(OnLongClickItemViewListener<T> listener) {
        mOnLongClickItemViewListener = listener;
    }

    public void setOnClickItemViewListener(OnClickItemViewListener<T> itemListener) {
        mOnClickItemViewListener = itemListener;
    }

    /**
     * 注入观察者对象
     *
     * @param observer
     */
    public final void inject(@NonNull Object observer) {
        mObserver = observer;
    }


    protected final Object getObserver() {
        return mObserver;
    }

    /**
     * registerItemViewClickListener重载。倘若不采用反射方式，则调用这个方法注册。
     *
     * @param holder
     * @param helper
     * @param viewIds
     */
    protected final void registerItemViewClickListener(@NonNull VH holder,
                                                       @NonNull MultiHelper<T, VH> helper,
                                                       @IdRes int... viewIds) {
        registerItemViewClickListener(holder, helper, null, viewIds);

    }

    /**
     * Item view 点击事件注册。（包含item 子view）
     *
     * @param holder
     * @param helper
     * @param target  目标方法名
     * @param viewIds view的id 集合；因为可能存在多个view响应同一套点击逻辑的情况。
     */
    protected final void registerItemViewClickListener(@NonNull VH holder,
                                                       @NonNull MultiHelper<T, VH> helper,
                                                       @Nullable String target,
                                                       @IdRes int... viewIds) {
        /*如果不传viewId，则默认是注册item根布局的点击事件监听*/
        if (viewIds.length == 0) {
            registerInternal(holder, helper, holder.itemView, target);
            return;
        }

        for (int id : viewIds) {
            final View view = holder.itemView.findViewById(id);
            registerInternal(holder, helper, view, target);
        }

    }


    private void registerInternal(VH holder, MultiHelper<T, VH> helper, View view, @Nullable String target) {
        view.setClickable(true);
        view.setOnClickListener(v -> {
            final int position = holder.getAdapterPosition();
            if (position == RecyclerView.NO_POSITION) {
                Log.e(TAG, "item 点击异常: position=" + position);
                return;
            }

            final T data = helper.getItem(position);
            if (data == null) {
                Log.e(TAG, "item 点击异常:data=" + data);
                return;
            }
            //优先监听器
            if (mOnClickItemViewListener != null) {
                mOnClickItemViewListener.onClickItemView(v, this.getItemType(), data, position);
                return;
            }
            /*不传入目标方法名，则表示不采用反射方式回调点击事件*/
            if (TextUtils.isEmpty(target)) {
                return;
            }
            callTagMethod(v, target, data, position, "item 点击异常");
        });

    }

    /**
     * Item view 长点击事件注册。（包含item 子view）
     *
     * @param holder
     * @param helper
     * @param target  目标方法名
     * @param viewIds view的id 集合；因为可能存在多个view响应同一套点击逻辑的情况。
     */
    protected final void registerItemViewLongClickListener(@NonNull VH holder,
                                                           @NonNull MultiHelper<T, VH> helper,
                                                           @Nullable String target,
                                                           @IdRes int... viewIds) {
        /*如果不传viewId，则默认是注册item根布局的点击事件监听*/
        if (viewIds.length == 0) {
            registerLongInternal(holder, helper, holder.itemView, target);
            return;
        }
        for (int id : viewIds) {
            final View view = holder.itemView.findViewById(id);
            registerLongInternal(holder, helper, view, target);
        }

    }

    protected final void registerItemViewLongClickListener(@NonNull VH holder,
                                                           @NonNull MultiHelper<T, VH> helper,
                                                           @IdRes int... viewIds) {
        registerItemViewLongClickListener(holder, helper, null, viewIds);

    }

    private void registerLongInternal(VH holder, MultiHelper<T, VH> helper, View view, String target) {
        view.setLongClickable(true);
        view.setOnLongClickListener(v -> {
            boolean consume = false;
            final int position = holder.getAdapterPosition();
            if (position == RecyclerView.NO_POSITION) {
                Log.e(TAG, "item 长点击异常: position=" + position);
                return consume;
            }
            final T data = helper.getItem(position);
            if (data == null) {
                Log.e(TAG, "item 长点击异常:data=" + data);
                return consume;
            }
            //监听器优先
            if (mOnLongClickItemViewListener != null) {
                return mOnLongClickItemViewListener.onLongClickItemView(v, this.getItemType(), data, position);
            }
            /*不传入目标方法名，则表示不采用反射方式回调点击事件*/
            if (TextUtils.isEmpty(target)) {
                return consume;
            }
            consume = callTagLongClickMethod(v, target, data, position, "item 长点击异常");
            return consume;

        });
    }


    /**
     * 反射回调点击方法
     *
     * @param v
     * @param data
     * @param position
     * @param errMsg
     */
    private void callTagMethod(View v, String target, T data, int position, String errMsg) {
        try {
            Method method = resolveMethod(target);
            if (!method.isAccessible()) {
                method.setAccessible(true);
            }
            method.invoke(mObserver, v, data, position);
        } catch (Exception e) {
            Log.e(TAG, errMsg + ":" + e.getMessage());
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
    private boolean callTagLongClickMethod(View v, String target, T data, int position, String errMsg) {
        boolean consume = false;
        try {
            Method method = resolveMethod(target);
            if (!method.isAccessible()) {
                method.setAccessible(true);
            }
            consume = (boolean) method.invoke(mObserver, v, data, position);
        } catch (Exception e) {
            Log.e(TAG, errMsg + ":" + e.getMessage());
        }
        return consume;
    }


    /**
     * 根据方法名反射获取目标方法
     *
     * @param methodName
     * @return
     */
    private Method resolveMethod(String methodName) {
        if (mObserver == null) {
            Log.e(TAG, "===> mObserver = null， 请先调用ItemType inject 方法注入 Observer！");
            return null;
        }

        final Class<?> clazz = mObserver.getClass();
        if (mObserverName == null) {
            mObserverName = clazz.getName();
        }
        final String key = mObserverName + "@" + methodName;
        Method method = getMethodMap().get(key);
        if (method == null) {
            try {
                if (mEntityClass == null) {
                    mEntityClass = getEntityClass();
                }
                method = clazz.getDeclaredMethod(methodName, View.class, mEntityClass, int.class);
                getMethodMap().put(key, method);
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }
        return method;
    }

    private Map<String, Method> getMethodMap() {
        if (mMethodMap == null) {
            mMethodMap = new ArrayMap<>();
        }
        return mMethodMap;
    }

    /**
     * 针对某些无法解析泛型参数 T 的情况，给子类提供重写方法。
     *
     * @return
     */
    protected Class<?> getEntityClass() {
        return resolveT();
    }

    /**
     * 解析泛型参数T
     *
     * @return
     */
    private Class<T> resolveT() {
        final Type type;
        try {
            type = this.getClass().getGenericSuperclass();
            ParameterizedType p = (ParameterizedType) type;
            return (Class<T>) p.getActualTypeArguments()[0];
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
            return null;
        }
    }

}

