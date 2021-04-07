package com.tencent.lib.multi.core;

import android.content.Context;
import android.content.res.Resources;
import android.util.ArrayMap;
import android.util.Log;
import android.view.View;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
    private Resources mResources;
    private Context mContext;
    private Object mObserver;
    private Method mOnClickItemM;
    private Method mOnLongClickItemM;
    private Map<String, Method> mOnClickItemSubViewMethods;
    private Map<String, Method> mOnLongClickItemSubViewMethods;
    private String mKey;

    public void regist(String key, Object observer, boolean clickItem, boolean clickItemSubView, boolean longClickItem,
            boolean longClickItemSubView) {
        mObserver = observer;
        mKey = key == null ? "" : key;
        Class<?> clazz = observer.getClass();
        Method[] methods = clazz.getDeclaredMethods();
        for (Method method : methods) {
            if (mOnClickItemM == null && clickItem) {
                OnClickItem onClickItem = method.getAnnotation(OnClickItem.class);
                if (onClickItem != null && onClickItem.key().equals(mKey)) {
                    mOnClickItemM = method;
                    continue;
                }
            }

            if (mOnLongClickItemM == null && longClickItem) {
                OnLongClickItem onLongClickItem = method.getAnnotation(OnLongClickItem.class);
                if (onLongClickItem != null && onLongClickItem.key().equals(mKey)) {
                    mOnLongClickItemM = method;
                    continue;
                }
            }
            if (mOnClickItemSubViewMethods == null && clickItemSubView) {
                OnClickItemSubView onClickItemSubView = method.getAnnotation(OnClickItemSubView.class);
                if (onClickItemSubView != null && onClickItemSubView.key().equals(mKey)) {
                    mOnClickItemSubViewMethods = new ArrayMap<>(onClickItemSubView.tags().length);
                    for (String tag : onClickItemSubView.tags()) {
                        mOnClickItemSubViewMethods.put(tag, method);
                    }
                    continue;
                }
            }

            if (mOnLongClickItemSubViewMethods == null && longClickItemSubView) {
                OnLongClickItemSubView onLongClickItemSubView = method.getAnnotation(OnLongClickItemSubView.class);
                if (onLongClickItemSubView != null && onLongClickItemSubView.key().equals(mKey)) {
                    mOnLongClickItemSubViewMethods = new ArrayMap<>(onLongClickItemSubView.tags().length);
                    for (String tag : onLongClickItemSubView.tags()) {
                        mOnLongClickItemSubViewMethods.put(tag, method);
                    }
                }

            }
        }
    }

    @Nullable
    public Resources getResources() {
        return mResources;
    }

    @Nullable
    public Context getContext() {
        return mContext;
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
    public void onCreateItemView(@NonNull MultiViewHolder holder, @NonNull MultiHelper<T> helper) {
        mContext = holder.itemView.getContext();
        mResources = holder.itemView.getResources();

    }

    @Override
    public void onBindViewHolder(@NonNull MultiViewHolder holder, @NonNull MultiHelper<T> helper, int position,
            @NonNull List<Object> payloads) {

    }

    @Override
    public void onClickItem(@NonNull MultiViewHolder holder, @NonNull T data, int position) {
              if (mItemListener!=null){
                  mItemListener.onClickItem(holder.itemView, data, position);
              }
        invokeOnClickItem(holder.itemView, data, position);
    }

    @Override
    public boolean onLongClickItem(@NonNull MultiViewHolder holder, @NonNull T data, int position) {
        if (mOnLongClickItemListener != null) {
            return mOnLongClickItemListener.onLongClickItem(holder.itemView, data, position);
        }
        return false;
    }

    protected void invokeOnClickItem(View view, T item, int position) {
        if (mOnClickItemM != null) {
            mOnClickItemM.setAccessible(true);
            try {
                mOnClickItemM.invoke(mObserver, view, item, position);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    protected boolean invokeOnLongClickItem(View view, T item, int position) {
        boolean consume = false;
        if (mOnLongClickItemM != null) {
            mOnLongClickItemM.setAccessible(true);
            try {
                consume = (boolean) mOnLongClickItemM.invoke(mObserver, view, item, position);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return consume;

    }

    /*注册item sub view 点击事件监听*/
    protected final void registItemSubViewClickListener(int viewId, MultiViewHolder holder, MultiHelper<T> helper) {
        View view = holder.getView(viewId);
        view.setOnClickListener(v -> {
            int position = holder.getAdapterPosition();
            T item = helper.getItem(position);
            if (item == null) {
                Log.e(TAG, "registItemSubViewListener getItem 为null");
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
            Method method = mOnClickItemSubViewMethods.get(tagStr);
            if (method == null) {
                Log.e(TAG, "未找到目标方法");
                return;
            }
            method.setAccessible(true);
            try {
                method.invoke(mObserver, v, item, position);
            } catch (IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }
        });
    }

    /*注册item sub view 长按点击事件监听*/
    protected final void registItemSubViewLongClickListener(int viewId, MultiViewHolder holder, MultiHelper<T> helper) {
        View view = holder.getView(viewId);
        view.setOnLongClickListener(v -> {
            boolean consume = false;
            int position = holder.getAdapterPosition();
            T item = helper.getItem(position);
            if (item == null) {
                Log.e(TAG, "registItemSubViewListener getItem 为null");
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
            Method method = mOnLongClickItemSubViewMethods.get(tagStr);
            if (method == null) {
                Log.e(TAG, "未找到目标方法");
                return consume;
            }
            method.setAccessible(true);
            try {
                consume = (boolean) method.invoke(mObserver, v, item, position);
            } catch (IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }
            return consume;
        });
    }
}
