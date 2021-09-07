package com.tencent.multiadapter.example.itemtype.vb

import com.tencent.lib.multi.core.MultiVBItemType
import com.tencent.multiadapter.R
import com.tencent.multiadapter.databinding.ItemCBinding
import com.tencent.multiadapter.example.bean.ItemBean

/**

 * Author：岑胜德 on 2021/8/30 14:51

 * 说明：

 */
class CVBItemType : MultiVBItemType<ItemBean, ItemCBinding>() {

    override fun matchItemType(bean: Any?, position: Int): Boolean {
        return (bean as ItemBean)!!.viewType == ItemBean.TYPE_C
    }

    override fun onBindViewHolder(vb: ItemCBinding, bean: ItemBean, position: Int) {
        vb.tvC.text = bean.text
    }

    override fun getItemLayoutRes(): Int = R.layout.item_c
}