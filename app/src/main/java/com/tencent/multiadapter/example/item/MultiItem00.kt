package com.tencent.multiadapter.example.item

import android.view.View
import android.widget.Toast
import androidx.annotation.Keep
import com.tencent.lib.multi.core.BindingMultiItem
import com.tencent.lib.multi.core.MultiViewHolder
import com.tencent.multiadapter.databinding.ItemA01Binding
import com.tencent.multiadapter.example.bean.AItemBean
import com.tencent.multiadapter.example.bean.ItemBean

/**

 * Author：岑胜德 on 2021/12/5 20:31

 * 说明：

 */
class MultiItem00 : BindingMultiItem<ItemBean, ItemA01Binding>() {

    init {
        inject(this)
    }
    override fun isMatchForMe(bean: Any?, position: Int): Boolean {
        return bean is ItemBean && bean.viewType == ItemBean.TYPE_00
    }

    override fun onViewHolderCreated(holder: MultiViewHolder, binding: ItemA01Binding) {
        registerItemViewClickListener(holder,binding.root,"onClickItem")
    }
    override fun onBindViewHolder(vb: ItemA01Binding, bean: ItemBean, position: Int) {
        vb.tvA.text = bean.text
    }

    //
    @Keep
    private fun onClickItem(view:View,itemBean: ItemBean,position: Int){
        Toast.makeText(view.context,"${itemBean.text} position:$position",Toast.LENGTH_SHORT).show()
    }
}