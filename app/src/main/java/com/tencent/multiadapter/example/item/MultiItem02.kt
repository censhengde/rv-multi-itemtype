package com.tencent.multiadapter.example.item

import com.tencent.lib.multi.core.SimpleMultiItem
import com.tencent.multiadapter.databinding.ItemA02Binding
import com.tencent.multiadapter.example.bean.ItemBean

/**

 * Author：岑胜德 on 2021/12/5 20:31

 * 说明：

 */
class MultiItem02 : SimpleMultiItem<ItemBean, ItemA02Binding>() {

    override fun isMatchForMe(bean: Any?, position: Int): Boolean {
        return bean is ItemBean && bean.viewType == ItemBean.TYPE_02
    }

    override fun onBindViewHolder(vb: ItemA02Binding, bean: ItemBean, position: Int) {
        vb.tvA.text = bean.text
    }
}