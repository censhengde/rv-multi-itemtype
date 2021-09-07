package com.tencent.multiadapter.example.itemtype.vb

import com.tencent.lib.multi.core.MultiVBItemType
import com.tencent.multiadapter.R
import com.tencent.multiadapter.databinding.ItemBBinding
import com.tencent.multiadapter.example.bean.ItemBean

/**

 * Author：岑胜德 on 2021/8/30 14:48

 * 说明：

 */
class BVBItemType : MultiVBItemType<ItemBean, ItemBBinding>() {
    override fun matchItemType(bean: Any?, position: Int): Boolean {
        return (bean as ItemBean).viewType == ItemBean.TYPE_B
    }

    override fun onBindViewHolder(vb: ItemBBinding, bean: ItemBean, position: Int) {
        vb.tvB.text = bean.text
    }

    override fun getItemLayoutRes(): Int = R.layout.item_b
}