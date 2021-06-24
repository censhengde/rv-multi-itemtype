package com.tencent.lib.multi.core;

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
import androidx.recyclerview.widget.RecyclerView;
import com.tencent.lib.multi.core.listener.OnClickItemViewListener;
import com.tencent.lib.multi.core.listener.OnLongClickItemViewListener;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;
import org.jetbrains.annotations.NotNull;

/**
 * Author：岑胜德 on 2021/2/22 16:23
 *
 * 说明：简单的 ItemType 实现
 */
public abstract class ItemTypeImpl<T,VH extends RecyclerView.ViewHolder> implements ItemType<T,VH> {

    private static final String TAG = "SimpleItemType";
    private static Map<String, Method> sMethodMap;/*缓存反射获取的method对象，减少反射成本*/
    private OnClickItemViewListener<T> mOnClickItemViewListener;
    private OnLongClickItemViewListener<T> mOnLongClickItemViewListener;

    private Object mObserver;
    private String mObserverName;
    private Class<T> mTClass;/*泛型参数T的Class*/
    private Constructor<VH> mVHConstructor;

    /**
     * 返回当前ItemType的布局文件id
     * @return
     */
    @LayoutRes
   protected abstract int getItemLayoutRes();

    @NonNull
    @NotNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent) {
        final View itemView= LayoutInflater.from(parent.getContext()).inflate(getItemLayoutRes(),parent,false);
        return resolveVH(itemView);
    }

    /**
     * 反射实例化ViewHolder
     * @param itemView
     * @return
     */
    private VH resolveVH(View itemView){
        final VH vh;
        try {
            if (mVHConstructor==null){
             final Type  type=this.getClass().getGenericSuperclass();
             final   ParameterizedType p = (ParameterizedType) type;
             final Class<VH> c=(Class<VH>) p.getActualTypeArguments()[1];
             mVHConstructor= c.getConstructor(View.class);
            }
          vh= (VH) mVHConstructor.newInstance(itemView);//要求所有VH必需开放参数为View的构造函数
        } catch (Exception e) {
            e.printStackTrace();
            return (VH) MultiViewHolder.createInvalid(itemView.getContext());
        }
        return vh;
    }
    public void setOnLongClickItemViewListener(OnLongClickItemViewListener<T> listener) {
        mOnLongClickItemViewListener = listener;
    }


    public void setOnClickItemViewListener(OnClickItemViewListener<T> itemListener) {
        mOnClickItemViewListener = itemListener;
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
    public void onViewHolderCreated(@NonNull VH holder, @NonNull MultiHelper<T,VH> helper) {


    }

    @Override
    public void onBindViewHolder(@NonNull VH holder, @NonNull MultiHelper<T,VH> helper, int position,
            @NonNull List<Object> payloads) {

    }


    /**
     * Item view 点击事件注册。（包含item 子view）
     *
     * @param holder
     * @param helper
     * @param target 目标方法名
     * @param viewIds view的id 集合；因为可能存在多个view响应同一套点击逻辑的情况。
     */
    protected final void registerItemViewClickListener(@NonNull VH holder,
            @NonNull MultiHelper<T,VH> helper,
            @Nullable String target,
            @IdRes int... viewIds)
    {
        /*如果不传viewId，则默认是注册item根布局的点击事件监听*/
        if (viewIds.length == 0) {
            register(holder, helper, holder.itemView, target);
            return;
        }

        for (int id : viewIds) {
            final View view = holder.itemView.findViewById(id);
            register(holder, helper, view, target);
        }

    }

    /**
     * registerItemViewClickListener重载。倘若不采用反射方式，则调用这个方法注册。
     * @param holder
     * @param helper
     * @param viewIds
     */
    protected final void registerItemViewClickListener(@NonNull VH holder,
            @NonNull MultiHelper<T,VH> helper,
            @IdRes int... viewIds)
    {
        registerItemViewClickListener(holder, helper, null, viewIds);

    }

    private void register(VH holder, MultiHelper<T,VH> helper, View view, @Nullable String target) {
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
                mOnClickItemViewListener.onClickItemView(v, getViewType(), data, position);
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
     * @param target 目标方法名
     * @param viewIds view的id 集合；因为可能存在多个view响应同一套点击逻辑的情况。
     */
    protected final void registerItemViewLongClickListener(@NonNull VH holder,
            @NonNull MultiHelper<T,VH> helper,
            @Nullable String target,
            @IdRes int... viewIds) {
        /*如果不传viewId，则默认是注册item根布局的点击事件监听*/
        if (viewIds.length == 0) {
            registerLong(holder, helper, holder.itemView, target);
            return;
        }
        for (int id : viewIds) {
            final View view = holder.itemView.findViewById(id);
            registerLong(holder, helper, view, target);
        }

    }

    protected final void registerItemViewLongClickListener(@NonNull VH holder,
            @NonNull MultiHelper<T,VH> helper,
            @IdRes int... viewIds) {
        registerItemViewLongClickListener(holder, helper, null, viewIds);

    }

    private void registerLong(VH holder, MultiHelper<T,VH> helper, View view, String target) {
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
                return mOnLongClickItemViewListener.onLongClickItemView(v, getViewType(), data, position);
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
    private Class<T> resolveT() {
        final Type type;
        try {
            type = this.getClass().getGenericSuperclass();
            ParameterizedType p = (ParameterizedType) type;
            Log.e(TAG, "tv[0].getName():" + type);
            return (Class<T>) p.getActualTypeArguments()[0];
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}

