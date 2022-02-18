package com.tencent.multiadapter.example.item

import com.tencent.lib.multi.core.SimpleItemType
import com.tencent.multiadapter.databinding.Item02Binding
import com.tencent.multiadapter.example.bean.ItemBean

/**

 * Author：岑胜德 on 2021/12/5 20:31

 * 说明：

 */
class ItemType02 : SimpleItemType<ItemBean, Item02Binding>() {

    override fun isMatchForMe(bean: Any?, position: Int): Boolean {
        return bean is ItemBean && bean.viewType == ItemBean.TYPE_02
    }

    override fun onBindView(vb: Item02Binding, bean: ItemBean, position: Int) {
        vb.tvA.text = bean.text
    }
}