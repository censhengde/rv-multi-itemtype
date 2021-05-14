package com.tencent.lib.multi.core;

import android.text.TextUtils;
import android.util.ArrayMap;
import android.util.Log;
import android.view.View;
import androidx.annotation.NonNull;
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

    private Object mObserver;
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


    /*item 点击事件注册*/
    protected final void registClickItemListener(MultiViewHolder holder, MultiHelper<T> helper) {
        checkTag(holder.itemView.getTag());
        holder.itemView.setOnClickListener(v -> {
            int position = holder.getAdapterPosition();
            if (position==RecyclerView.NO_POSITION){
                Log.e(TAG, "item 点击异常: position="+position);
                return;
            }

            T data = helper.getItem(position);
            if (data == null) {
                Log.e(TAG, "item 点击异常:data="+data);
                return;
            }
            //优先监听器
            if (mItemListener != null) {
                mItemListener.onClickItem(v, getViewType(), data, position);
                return;
            }

            callTagMethod(v, data, position, "item 点击异常");

        });
    }

    /*item 长点击事件注册*/
    protected final void registLongClickItemListener(MultiViewHolder holder, MultiHelper<T> helper) {
        checkTag(holder.itemView.getTag());
        holder.itemView.setOnLongClickListener(v -> {
            boolean consume = false;
            int position = holder.getAdapterPosition();
            if (position==RecyclerView.NO_POSITION){
                Log.e(TAG, "item 长点击异常: position="+position);
                return consume;
            }
            T data = helper.getItem(position);
            if (data == null) {
                Log.e(TAG, "item 长点击异常:data="+data);
                return consume;
            }
            //监听器优先
            if (mOnLongClickItemListener != null) {
                return mOnLongClickItemListener.onLongClickItem(v, getViewType(), data, position);
            }
            consume = callTagLongClickMethod(v, data, position, "item 长点击异常");
            return consume;
        });
    }

    /*注册item child view 点击事件监听*/
    protected final void registClickItemChildViewListener(int viewId, MultiViewHolder holder, MultiHelper<T> helper) {
        final View view = holder.getView(viewId);
        checkTag(view.getTag());
        view.setOnClickListener(v -> {
            int position = holder.getAdapterPosition();
            if (position==RecyclerView.NO_POSITION){
                Log.e(TAG, "item child view 点击异常: position="+position);
                return;
            }
            T data = helper.getItem(position);
            if (data == null) {
                Log.e(TAG, "item child view 点击异常:data="+data);
                return;
            }
            //监听器优先
            if (mOnClickItemChildViewListener != null) {
                mOnClickItemChildViewListener.onClickItemChildView(v, getViewType(), data, position);
                return;
            }

            callTagMethod(v, data, position, "item child view 点击异常");

        });
    }

    /*注册item child view 长按点击事件监听*/
    protected final void registLongClickItemChildViewListener(int viewId, MultiViewHolder holder,
            MultiHelper<T> helper) {
        final View view = holder.getView(viewId);
        checkTag(view.getTag());
        view.setOnLongClickListener(v -> {
            boolean consume = false;
            int position = holder.getAdapterPosition();
            if (position==RecyclerView.NO_POSITION){
                Log.e(TAG, "item child view 长点击异常: position="+position);
                return consume;
            }
            T data = helper.getItem(position);
            if (data == null ) {
                Log.e(TAG, "item child view 长点击异常:data="+data);
                return consume;
            }

            //监听器优先
            if (mOnLongClickItemChildViewListener != null) {
                return mOnLongClickItemChildViewListener
                        .onClickItemChildView(v, getViewType(), data, position);
            }
            consume = callTagLongClickMethod(v, data, position, "item child view 长点击异常");
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
    private  void callTagMethod(View v, T data, int position, String errMsg) {
        try {
            Method method = resolveMethod((String) v.getTag());
            method.setAccessible(true);
            method.invoke(mObserver, v, data, position);
        } catch (Exception e) {
            Log.e(TAG, errMsg + ":" + e.getMessage());
        }
    }

    /**
     * 反射回调长点击方法
     * @param v
     * @param data
     * @param position
     * @param errMsg
     * @return
     */
    private boolean callTagLongClickMethod(View v, T data, int position, String errMsg) {
        boolean consume = false;
        try {
            Method method = resolveMethod((String) v.getTag());
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
     * 检查 tag值的合法性
     *
     * @param tag
     * @return
     */
    private void checkTag(Object tag) {
        if (!(tag instanceof String)) {
            throw new IllegalArgumentException(" Item sub view tag 不能为null、空格，且必须为 String 类型 ");
        }

        final String methodName = (String) tag;
        if (TextUtils.isEmpty(methodName)) {
            throw new IllegalArgumentException(" Item sub view tag 不能为空格");
        }

    }

    /**
     * 根据tag 提供的方法名反射获取目标方法
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
        final String key = clazz.getCanonicalName() + methodName;
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
        final Type type = this.getClass().getGenericSuperclass();
        final ParameterizedType p = (ParameterizedType) type;
        return (Class<?>) p.getActualTypeArguments()[0];
    }
}

