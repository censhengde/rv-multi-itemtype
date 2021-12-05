package com.tencent.multiadapter.example.item

import com.tencent.lib.multi.core.BindingMultiItem
import com.tencent.multiadapter.databinding.ItemA01Binding
import com.tencent.multiadapter.example.bean.AItemBean
import com.tencent.multiadapter.example.bean.ItemBean

/**

 * Author：岑胜德 on 2021/12/5 20:31

 * 说明：

 */
class MultiItem01 : BindingMultiItem<ItemBean, ItemA01Binding>() {
    override fun isMatchForMe(bean: Any?, position: Int): Boolean {
        return bean is ItemBean && bean.viewType == ItemBean.TYPE_01
    }

    override fun onBindViewHolder(vb: ItemA01Binding, bean: ItemBean, position: Int) {
        vb.tvA.text = bean.text
    }
}