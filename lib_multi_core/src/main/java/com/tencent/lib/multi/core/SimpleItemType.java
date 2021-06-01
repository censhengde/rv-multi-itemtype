package com.tencent.lib.multi.core;

import android.text.TextUtils;
import android.util.ArrayMap;
import android.util.Log;
import android.view.View;
import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;
import com.tencent.lib.multi.core.listener.OnClickItemChildViewListener;
import com.tencent.lib.multi.core.listener.OnClickItemListener;
import com.tencent.lib.multi.core.listener.OnLongClickItemChildViewListener;
import com.tencent.lib.multi.core.listener.OnLongClickItemListener;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

/**
 * Author：岑胜德 on 2021/2/22 16:23
 *
 * 说明：简单的 ItemType 实现
 */
public abstract class SimpleItemType<T> implements ItemType<T> {

    private static final String TAG = "SimpleItemType";
    private static Map<String, Method> sMethodMap;/*缓存反射获取的method对象，减少反射成本*/
    private OnClickItemListener<T> mItemListener;
    private OnLongClickItemListener<T> mOnLongClickItemListener;
    private OnClickItemChildViewListener<T> mOnClickItemChildViewListener;
    private OnLongClickItemChildViewListener<T> mOnLongClickItemChildViewListener;
    private T t;
    private Object mObserver;
    private String mObserverName;
    private Class<?> mTClass;/*泛型参数T的Class*/



    public void setOnLongClickItemListener(OnLongClickItemListener<T> listener) {
        mOnLongClickItemListener = listener;
    }


    public void setOnClickItemListener(OnClickItemListener<T> itemListener) {
        mItemListener = itemListener;
    }

    public void setOnClickItemChildViewListener(
            OnClickItemChildViewListener<T> listener) {
        this.mOnClickItemChildViewListener = listener;
    }

    public void setOnLongClickItemChildViewListener(
            OnLongClickItemChildViewListener<T> listener) {
        this.mOnLongClickItemChildViewListener = listener;
    }

    public void bind(@NonNull Object listener) {
        mObserver = listener;
    }
    @Override
    public int getViewType() {
        return 0;
    }

    @Override
    public boolean matchItemType(@NonNull T data, int position) {
        return false;
    }



    @Override
    public void onViewHolderCreated(@NonNull MultiViewHolder holder, @NonNull MultiHelper<T> helper) {


    }

    @Override
    public void onBindViewHolder(@NonNull MultiViewHolder holder, @NonNull MultiHelper<T> helper, int position,
            @NonNull List<Object> payloads) {

    }


    /**
     * Item view 点击事件注册。（包含item 子view）
     *
     * @param holder
     * @param helper
     * @param target 目标方法名
     * @param viewIds view的id 集；因为存在多个view响应同一套点击逻辑的情况。
     */
    protected final void registerItemViewClickListener(@NonNull MultiViewHolder holder, @NonNull MultiHelper<T> helper,
            @Nullable String target,
            @IdRes int... viewIds) {
        /*如果不传viewId，则默认是注册item根布局的点击事件监听*/
        if (viewIds.length == 0) {
            register(holder, helper, holder.itemView, target);
            return;
        }

        for (int id : viewIds) {
            final View view = holder.getView(id);
            register(holder, helper, view, target);
        }

    }

    /**
     * registerItemViewClickListener重载。倘若不采用反射方式，则调用这个方法注册。
     * @param holder
     * @param helper
     * @param viewIds
     */
    protected final void registerItemViewClickListener(@NonNull MultiViewHolder holder, @NonNull MultiHelper<T> helper,
            @IdRes int... viewIds) {
        registerItemViewClickListener(holder, helper, null, viewIds);

    }

    private void register(MultiViewHolder holder, MultiHelper<T> helper, View view, @Nullable String target) {
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
            if (mItemListener != null) {
                mItemListener.onClickItem(v, getViewType(), data, position);
                return;
            }
            /*不传入目标方法名，则表示不采用反射方式回调点击事件*/
            if (TextUtils.isEmpty(target)) {
                return;
            }

            callTagMethod(v, target, data, position, "item 点击异常");
        });

    }

    protected final void registerItemViewLongClickListener(@NonNull MultiViewHolder holder,
            @NonNull MultiHelper<T> helper,
            @Nullable String target,
            @IdRes int... viewIds) {
        /*如果不传viewId，则默认是注册item根布局的点击事件监听*/
        if (viewIds.length == 0) {
            registerLong(holder, helper, holder.itemView, target);
            return;
        }
        for (int id : viewIds) {
            final View view = holder.getView(id);
            registerLong(holder, helper, view, target);
        }

    }

    private void registerLong(MultiViewHolder holder, MultiHelper<T> helper, View view, String target) {
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
            if (mOnLongClickItemListener != null) {
                return mOnLongClickItemListener.onLongClickItem(v, getViewType(), data, position);
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
     *  根据方法名反射获取目标方法
     *
     * @param methodName
     * @return
     */
    private Method resolveMethod(String methodName) {
        if (mObserver == null) {
            Log.e(TAG, "");
            return null;
        }
        if (sMethodMap == null) {
            sMethodMap = new ArrayMap<>();
        }
        final Class<?> clazz = mObserver.getClass();
        if (mObserverName == null) {
            mObserverName = clazz.getName();
        }
        final String key = mObserverName + "@" + methodName;
        Method method = sMethodMap.get(key);
        if (method == null) {
            try {
                if (mTClass == null) {
                    mTClass = resolveT();
                }
                method = clazz.getDeclaredMethod(methodName, View.class, mTClass, int.class);
                sMethodMap.put(key, method);
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }
        return method;
    }

    /**
     * 解析泛型参数T
     *
     * @return
     */
    private Class<?> resolveT() {
        final Type type;
        try {
            type = this.getClass().getGenericSuperclass();
            ParameterizedType p = (ParameterizedType) type;
            Log.e(TAG, "tv[0].getName():" + type);
            return (Class<?>) p.getActualTypeArguments()[0];
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}

