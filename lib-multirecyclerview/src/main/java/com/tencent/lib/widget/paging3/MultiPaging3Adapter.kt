package com.tencent.lib.widget.paging3

import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import com.tencent.lib.widget.DelegateAdapter
import com.tencent.lib.widget.ItemType
import com.tencent.lib.widget.MultiViewHolder
import java.lang.reflect.ParameterizedType

/**

 * Author：岑胜德 on 2021/1/27 15:51

 * 说明：

 */
open class MultiPaging3Adapter<T : Any>(diffCallback: DiffUtil.ItemCallback<T>) : PagingDataAdapter<T, MultiViewHolder>(diffCallback) {
    private lateinit var delegateAdapter: DelegateAdapter<T>


//    protected open fun getDelegateAdapter(): DelegateAdapter {
//
//        return object : DelegateAdapter<T>() {
//            override fun getItem(position: Int): T? {
//                return this@MultiPaging3Adapter.getItem(position)
//            }
//        }
//    }

    init {

    }

    private fun analysisT(){
        val clz=this.javaClass
        val type=clz.genericSuperclass
        val parameterizedType= type as ParameterizedType
        val tClaz=parameterizedType.actualTypeArguments[0]
//        tClaz.typeName.equals("")

    }
    override fun getItemViewType(position: Int): Int {
       return delegateAdapter.getItemViewType(position)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MultiViewHolder {

        return delegateAdapter.onCreateViewHolder(parent, viewType)
    }

    override fun onBindViewHolder(holder: MultiViewHolder, position: Int) {
        delegateAdapter.onBindViewHolder(holder, position)
    }

    fun setItemTypes(types: MutableList<ItemType<T>>) {
        delegateAdapter.setItemTypes(types)
    }

    fun setItemType(type: ItemType<T>) {
        delegateAdapter.setItemType(type)
    }
}