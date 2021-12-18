package com.tencent.lib.multi.core;

import static androidx.recyclerview.widget.RecyclerView.NO_ID;

import android.text.TextUtils;
import android.util.ArrayMap;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.CallSuper;
import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.tencent.lib.multi.core.annotation.OnClickItemView;
import com.tencent.lib.multi.core.listener.OnClickItemViewListener;
import com.tencent.lib.multi.core.listener.OnLongClickItemViewListener;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

import org.jetbrains.annotations.NotNull;

/**
 * Author：岑胜德 on 2021/2/22 16:23
 * <p>
 * 说明：某一种类型 item 的抽象。
 */
public abstract class MultiItemType<T, VH extends RecyclerView.ViewHolder> {

    private static final String TAG = "MultiItem";

    private Map<String, Method> mMethodCachePool;/*缓存反射获取的method对象，减少反射成本*/
    private OnClickItemViewListener<T> mOnClickItemViewListener;
    private OnLongClickItemViewListener<T> mOnLongClickItemViewListener;
    private Object mClickEventReceiver;/*item view 点击事件的接收者*/
    private MultiHelper mHelper;


    public final void setMethodCachePool(Map<String, Method> cachePool) {
        this.mMethodCachePool = cachePool;
    }

    @NotNull
    public final Map<String, Method> getMethodCachePool() {
        if (mMethodCachePool == null) {
            mMethodCachePool = new ArrayMap<>();
        }
        return mMethodCachePool;
    }

    @CallSuper
    public void onAttach(@NotNull MultiHelper helper) {
        mHelper = helper;
    }

    @NotNull
    public final MultiHelper getHelper() {
        if (mHelper == null) {
            throw new IllegalStateException("MultiItem " + this + " not attached to an MultiHelper.");
        }
        return mHelper;
    }


    public long getItemId(int position) {
        return NO_ID;
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
    public abstract VH onCreateViewHolder(@NonNull ViewGroup parent);

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

    public final void inject(Object receiver) {
        if (checkIsNull(receiver, "inject receiver is null")) {
            return;
        }
        mClickEventReceiver = receiver;
        if (mMethodCachePool == null) {
            mMethodCachePool = createMethodCachePool(receiver);
        }
    }

    @NotNull
    public static Map<String, Method> createMethodCachePool(Object clickEventReceiver) {
        final Map<String, Method> pool = new ArrayMap<>();
        try {
            Class<?> clazz = clickEventReceiver.getClass();
            Method[] methods = clazz.getDeclaredMethods();
            for (Method m : methods) {
                OnClickItemView annotation = m.getAnnotation(OnClickItemView.class);
                if (annotation != null) {
                    pool.put(annotation.value(), m);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return pool;
    }

    protected final Object getClickEventReceiver() {
        return mClickEventReceiver;
    }

    protected final void registerClickEvent(@NonNull VH holder,
                                            @IdRes int... viewIds) {
        registerClickEvent(holder, null, viewIds);

    }

    /**
     * Item view 点击事件注册。（包含item 子view）
     *
     * @param holder
     * @param method  目标方法名
     * @param viewIds view的id 集合；因为可能存在多个view响应同一套点击逻辑的情况。
     */
    protected final void registerClickEvent(@NonNull VH holder,
                                            @Nullable String method,
                                            @IdRes int... viewIds) {
        /*如果不传viewId，则默认是注册item根布局的点击事件监听*/
        if (viewIds.length == 0) {
            registerClickEvent(holder, holder.itemView, method);
            return;
        }

        for (int id : viewIds) {
            final View view = holder.itemView.findViewById(id);
            registerClickEvent(holder, view, method);
        }

    }


    protected final void registerClickEvent(VH holder, View view, @Nullable String method) {
        view.setClickable(true);
        view.setOnClickListener(v -> {
            final int position = holder.getAdapterPosition();
            if (position == RecyclerView.NO_POSITION) {
                Log.e(TAG, "item 点击异常: position=" + position);
                return;
            }

            final Object data = getHelper().getItem(position);
            if (checkIsNull(data, "item 点击异常:data is null")) {
                return;
            }
            //优先监听器
            if (mOnClickItemViewListener != null) {
                mOnClickItemViewListener.onClickItemView(v, this, (T) data, position);
                return;
            }
            /*不传入目标方法名，则表示不采用反射方式回调点击事件*/
            if (TextUtils.isEmpty(method)) {
                return;
            }
            callTagMethod(v, method, data, position, "item 点击异常");
        });

    }

    /**
     * Item view 长点击事件注册。（包含item 子view）
     *
     * @param holder
     * @param method  目标方法名
     * @param viewIds view的id 集合；因为可能存在多个view响应同一套点击逻辑的情况。
     */
    protected final void registerLongClickEvent(@NonNull VH holder,
                                                @Nullable String method,
                                                @IdRes int... viewIds) {
        /*如果不传viewId，则默认是注册item根布局的点击事件监听*/
        if (viewIds.length == 0) {
            registerLongClickEvent(holder, holder.itemView, method);
            return;
        }
        for (int id : viewIds) {
            final View view = holder.itemView.findViewById(id);
            registerLongClickEvent(holder, view, method);
        }

    }


    protected final void registerLongClickEvent(VH holder, View view, String method) {
        view.setLongClickable(true);
        view.setOnLongClickListener(v -> {
            boolean consume = false;
            final int position = holder.getAdapterPosition();
            if (position == RecyclerView.NO_POSITION) {
                Log.e(TAG, "item 长点击异常: position=" + position);
                return consume;
            }
            final Object data = getHelper().getItem(position);
            if (checkIsNull(data, "item 长点击异常:data is null")) {
                return consume;
            }
            //监听器优先
            if (mOnLongClickItemViewListener != null) {
                return mOnLongClickItemViewListener.onLongClickItemView(v, this, (T) data, position);
            }
            /*不传入目标方法名，则表示不采用反射方式回调点击事件*/
            if (TextUtils.isEmpty(method)) {
                return consume;
            }
            consume = callTagLongClickMethod(v, method, data, position, "item 长点击异常");
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
    private void callTagMethod(View v, String methodName, Object data, int position, String errMsg) {
        try {
            Method method = getMethodCachePool().get(methodName);
            if (checkIsNull(method, "callTagMethod method is null")) {
                return;
            }
            assert method != null;
            if (!method.isAccessible()) {
                method.setAccessible(true);
            }
            method.invoke(mClickEventReceiver, v, data, position);
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
    private boolean callTagLongClickMethod(View v, String target, Object data, int position, String errMsg) {
        boolean consume = false;
        try {
            Method method = getMethodCachePool().get(target);
            if (checkIsNull(method, "callTagLongClickMethod method is null")) {
                return consume;
            }
            assert method != null;
            if (!method.isAccessible()) {
                method.setAccessible(true);
            }
            consume = (boolean) method.invoke(mClickEventReceiver, v, data, position);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return consume;
    }

    private boolean checkIsNull(Object o, String msg) {
        if (o == null) {
            Log.e(TAG, "===>" + msg);
        }
        return o == null;
    }


}

