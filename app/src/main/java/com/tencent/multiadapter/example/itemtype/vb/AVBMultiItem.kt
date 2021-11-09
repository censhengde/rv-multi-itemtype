package com.tencent.multiadapter.example.itemtype.vb

import com.tencent.lib.multi.core.BindingMultiItem
import com.tencent.multiadapter.R
import com.tencent.multiadapter.databinding.ItemABinding
import com.tencent.multiadapter.example.bean.ItemBean

/**

 * Author：岑胜德 on 2021/8/30 12:03

 * 说明：

 */
class AVBMultiItem : BindingMultiItem<ItemBean, ItemABinding>(){

    override fun isMatchForMe(bean: Any?, position: Int): Boolean {
        return (bean as ItemBean).viewType==ItemBean.TYPE_A
    }


    override fun getItemLayoutRes(): Int {
        return R.layout.item_a
    }

    override fun onBindViewHolder(vb: ItemABinding, bean: ItemBean, position: Int) {

        vb.tvA.text=bean.text

    }


}