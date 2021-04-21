package com.tencent.lib.multi.core;

import android.util.ArrayMap;
import android.util.Log;
import android.view.View;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

/**
 * Author：岑胜德 on 2021/2/22 16:23
 *
 * 说明：简单的 ItemType 实现
 */
public abstract class SimpleItemType<T> implements ItemType<T> {

    private static final String TAG = "SimpleItemType";
    private OnClickItemListener<T> mItemListener;
    private OnLongClickItemListener<T> mOnLongClickItemListener;

    private Object mObserver;
    private Method mOnClickItemM;
    private Method mOnLongClickItemM;
    private Map<String, Method> mOnClickItemChildViewMethods;
    private Map<String, Method> mOnLongClickItemChildViewMethods;


    /*注册观察者*/
    public final void regist(Object observer, String rv, String it,
            boolean clickItem,/*true 表示对该点击事件感兴趣*/
            boolean clickItemChildView,
            boolean longClickItem,
            boolean longClickItemChildView) {
        mObserver = observer;
        rv = rv == null ? "" : rv;
        Class<?> clazz = observer.getClass();
        Method[] methods = clazz.getDeclaredMethods();
        for (Method method : methods) {
            if (mOnClickItemM == null && clickItem) {
                OnClickItem onClickItem = method.getAnnotation(OnClickItem.class);
                if (onClickItem != null && onClickItem.rv().equals(rv) && onClickItem.it().equals(it)) {
                    mOnClickItemM = method;
                    continue;
                }
            }

            if (mOnLongClickItemM == null && longClickItem) {
                OnLongClickItem onLongClickItem = method.getAnnotation(OnLongClickItem.class);
                if (onLongClickItem != null && onLongClickItem.rv().equals(rv) && onLongClickItem.it().equals(it)) {
                    mOnLongClickItemM = method;
                    continue;
                }
            }
            if (clickItemChildView) {
                OnClickItemChildView onClickItemChildView = method.getAnnotation(OnClickItemChildView.class);
                if (onClickItemChildView != null && onClickItemChildView.rv().equals(rv) && onClickItemChildView.it()
                        .equals(it)) {
                    if (mOnClickItemChildViewMethods == null) {
                        mOnClickItemChildViewMethods = new ArrayMap<>(onClickItemChildView.tags().length);
                    }
                    /*key 不同，但value同*/
                    for (String tag : onClickItemChildView.tags()) {
                        mOnClickItemChildViewMethods.put(tag, method);
                    }
                    continue;
                }
            }

            if (longClickItemChildView) {
                OnLongClickItemChildView onLongClickItemChildView = method
                        .getAnnotation(OnLongClickItemChildView.class);
                if (onLongClickItemChildView != null && onLongClickItemChildView.rv().equals(rv)
                        && onLongClickItemChildView.it().equals(it)) {
                    if (mOnLongClickItemChildViewMethods == null) {
                        mOnLongClickItemChildViewMethods = new ArrayMap<>(onLongClickItemChildView.tags().length);
                    }
                    for (String tag : onLongClickItemChildView.tags()) {
                        mOnLongClickItemChildViewMethods.put(tag, method);
                    }
                }

            }
        }
    }



    public void setOnLongClickItemListener(OnLongClickItemListener<T> listener) {
        mOnLongClickItemListener = listener;
    }


    public void setOnClickItemListener(OnClickItemListener<T> itemListener) {
        mItemListener = itemListener;
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
    protected final void registOnClickItemListener(MultiViewHolder holder, MultiHelper<T> helper) {
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
            if (mItemListener != null) {
                mItemListener.onClickItem(v, data, position);
            }
            if (mOnClickItemM != null) {
                mOnClickItemM.setAccessible(true);
                try {
                    mOnClickItemM.invoke(mObserver, v, data, position);
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.e(TAG, "item 点击异常，请检查注解方法声明是否正确："+e);
                }
            }

        });
    }

    /*item 长点击事件注册*/
    protected final void registOnLongClickItemListener(MultiViewHolder holder, MultiHelper<T> helper) {
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
            if (mOnLongClickItemListener != null) {
                consume = mOnLongClickItemListener.onLongClickItem(v, data, position);
            }
            if (mOnLongClickItemM != null) {
                mOnLongClickItemM.setAccessible(true);
                try {
                    consume = (boolean) mOnLongClickItemM.invoke(mObserver, v, data, position);
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.e(TAG, "item 长点击异常，请检查注解方法声明是否正确："+e);
                }
            }

            return consume;
        });
    }

    /*注册item child view 点击事件监听*/
    protected final void registOnClickItemChildViewListener(int viewId, MultiViewHolder holder, MultiHelper<T> helper) {
        View view = holder.getView(viewId);
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

            Object tag = v.getTag();
            if (tag == null) {
                throw new IllegalArgumentException("tag 不能为null");
            }

            if (!(tag instanceof String)) {
                throw new IllegalArgumentException(" Item sub view tag 必须为 String 类型");
            }
            String tagStr = (String) tag;
            if (mOnClickItemChildViewMethods == null) {
                Log.e(TAG, "请检查 regist（）方法是否正确调用：mOnClickItemSubViewMethods == null");
                return;
            }
            Method method = mOnClickItemChildViewMethods.get(tagStr);
            if (method == null) {
                Log.e(TAG, "未找到目标方法");
                return;
            }
            method.setAccessible(true);
            try {
                method.invoke(mObserver, v, data, position);
            } catch (IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
                Log.e(TAG, "item child view 点击异常，请检查注解方法声明是否正确："+e);
            }
        });
    }

    /*注册item child view 长按点击事件监听*/
    protected final void registOnLongClickItemChildViewListener(int viewId, MultiViewHolder holder,
            MultiHelper<T> helper) {
        View view = holder.getView(viewId);
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

            Object tag = v.getTag();
            if (tag == null) {
                throw new IllegalArgumentException("tag 不能为null");
            }

            if (!(tag instanceof String)) {
                throw new IllegalArgumentException(" Item sub view tag 必须为 String 类型");
            }
            String tagStr = (String) tag;
            if (mOnLongClickItemChildViewMethods == null) {
                Log.e(TAG, "请检查 regist（）方法是否正确调用：mOnLongClickItemSubViewMethods == null");
                return consume;
            }
            Method method = mOnLongClickItemChildViewMethods.get(tagStr);
            if (method == null) {
                Log.e(TAG, "未找到目标方法");
                return consume;
            }
            method.setAccessible(true);
            try {
                consume = (boolean) method.invoke(mObserver, v, data, position);
            } catch (IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
                Log.e(TAG, "item child view 长点击异常，请检查注解方法声明是否正确："+e);
            }
            return consume;
        });
    }
}
