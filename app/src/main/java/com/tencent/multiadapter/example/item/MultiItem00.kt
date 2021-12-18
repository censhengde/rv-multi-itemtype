package com.tencent.multiadapter.example.item

import android.view.View
import android.widget.Toast
import com.tencent.lib.multi.core.SimpleMultiItem
import com.tencent.lib.multi.core.MultiViewHolder
import com.tencent.lib.multi.core.annotation.BindItemViewClickEvent
import com.tencent.multiadapter.databinding.ItemA00Binding
import com.tencent.multiadapter.example.bean.ItemBean

/**

 * Author：岑胜德 on 2021/12/5 20:31

 * 说明：

 */
class MultiItem00 : SimpleMultiItem<ItemBean, ItemA00Binding>() {

    init {
        inject(this)
    }

    override fun isMatchForMe(bean: Any?, position: Int): Boolean {
        return bean is ItemBean && bean.viewType == ItemBean.TYPE_00
    }

    override fun onViewHolderCreated(holder: MultiViewHolder, binding: ItemA00Binding) {
        // 注册 item view 点击事件
        registerClickEvent(holder, binding.root, "onClickItem")
        // 注册 item view 长点击事件
        registerLongClickEvent(holder,binding.tvA,"onLongClickItemChildView")
    }

    override fun onBindViewHolder(vb: ItemA00Binding, bean: ItemBean, position: Int) {
        vb.tvA.text = bean.text
    }

    /**
     *item点击事件
     */
    @BindItemViewClickEvent("onClickItem")
    private fun onClickItem(view: View, itemBean: ItemBean, position: Int) {
        Toast.makeText(view.context, "点击事件：ItemBean:${itemBean.text},position:$position", Toast.LENGTH_SHORT).show()
    }

    /**
     * item 子View 长点击事件
     */
    @BindItemViewClickEvent("onLongClickItemChildView")
    private fun onLongClickItemChildView(view: View, itemBean: ItemBean, position: Int): Boolean {
        Toast.makeText(view.context, "长点击事件：ItemBean:${itemBean.text},position:$position", Toast.LENGTH_SHORT).show()
        return true
    }

}