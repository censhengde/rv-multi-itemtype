package com.tencent.multiadapter.example.item

import android.view.View
import android.widget.Toast
import com.tencent.lib.multi.core.MultiViewHolder
import com.tencent.lib.multi.core.SimpleItemType
import com.tencent.lib.multi.core.annotation.OnClickItemView
import com.tencent.multiadapter.databinding.ItemA01Binding
import com.tencent.multiadapter.example.bean.ItemBean

/**

 * Author：岑胜德 on 2021/12/5 20:31

 * 说明：

 */
class ItemType01 : SimpleItemType<ItemBean, ItemA01Binding>() {
    init {
        inject(this)
    }

    override fun isMatchForMe(bean: Any?, position: Int): Boolean {
        return bean is ItemBean && bean.viewType == ItemBean.TYPE_01
    }

    override fun onViewHolderCreated(holder: MultiViewHolder, binding: ItemA01Binding) {
        registerClickEvent(holder, binding.tvA, "onClickItemChildView")
    }

    override fun onBindViewHolder(vb: ItemA01Binding, bean: ItemBean, position: Int) {
        vb.tvA.text = bean.text
    }

    @OnClickItemView("onClickItemChildView")
    private fun onClickItemChildView(view: View, itemBean: ItemBean, position: Int) {
        Toast.makeText(view.context, "ItemBean:${itemBean.text},position:$position", Toast.LENGTH_SHORT).show()
    }
}