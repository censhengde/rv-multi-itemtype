package com.tencent.lib.widget

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingSource
import androidx.paging.cachedIn
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.tencent.lib.widget.paging3.MultiPaging3Adapter
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

/**
 * Author：岑胜德 on 2021/1/6 14:51
 *
 *
 * 说明：
 */
class MultiRecyclerView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : RecyclerView(context, attrs, defStyleAttr) {
    private val isPaged: Boolean
    private val diffItemCallbackName: String?
    private val diffItemCallback: DiffUtil.ItemCallback<*>? by lazy { createDiffItemCallback() }
    private val dataSourceName: String?
    private val dataSource: PagingSource<*, *>? by lazy { createDataSource() }
    private lateinit var pagingSource: PagingSource<*, *>
    private val pagingConfig: PagingConfig? by lazy { null }
    private lateinit var owner: LifecycleOwner

    init {
        val a = context.obtainStyledAttributes(attrs, R.styleable.MultiRecyclerView)
        isPaged = a.getBoolean(R.styleable.MultiRecyclerView_isPaged, false)
        diffItemCallbackName = a.getString(R.styleable.MultiRecyclerView_diffItemCallback)
        dataSourceName = a.getString(R.styleable.MultiRecyclerView_dataSource)
        a.recycle()


    }

    /*创建 DiffUtil.ItemCallback*/
    private fun createDiffItemCallback(): DiffUtil.ItemCallback<*>? {
        try {
            diffItemCallbackName?.apply {
                val clazz = Class.forName(this)
                return clazz.newInstance() as DiffUtil.ItemCallback<*>
            }
        } catch (e: Exception) {
            throw RuntimeException("createDiffItemCallback 异常：${e.message}")
        }
        return null
    }

    /*创建 PagingSource*/
    private fun createDataSource(): PagingSource<*, *>? {
        try {
            dataSourceName?.apply {
                val clazz = Class.forName(this)
                return clazz.newInstance() as PagingSource<*, *>?
            }
        } catch (e: Exception) {
            throw RuntimeException("createDataSource 异常：${e.message}")
        }
        return null
    }

    fun linearBuilder(): LinearBuilder {
        return LinearBuilder(this)
    }

    fun gridBuilder(span: Int): GridBuilder {
        return GridBuilder(this, span)
    }

    fun staggeredGridBuilder(gapStrategy: Int): StaggeredGridBuilder {
        return StaggeredGridBuilder(this, gapStrategy)
    }



    /*实现单选、复选的Builder*/
    fun checkedBuilder() = CheckedBuilder(this);
    fun loadData() {
        if (!isPaged){
            Log.e("MultiRecyclerView","未启用分页功能")
            return
        }
        diffItemCallback?.let {
            val adapter = MultiPaging3Adapter(it)
            this.setAdapter(adapter)
            pagingConfig?.let {
                val flow = Pager(it) {
                    dataSource as PagingSource<Any, Any>
                }.flow.cachedIn(owner.lifecycleScope)
                //启动协程
                owner.lifecycleScope.launch {
                    flow.collectLatest { data ->
                        adapter.submitData(data as Nothing)
                    }

                }

            }
        }
    }

    fun bindLifecycleOwner(owner: LifecycleOwner) {
        this.owner = owner
    }
    fun getItemManager() =adapter as ItemManager<*>
}