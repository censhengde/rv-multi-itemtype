package com.tencent.multiadapter.example.itemtype.multiDatatype

import com.tencent.lib.multi.core.MultiHelper
import com.tencent.lib.multi.core.MultiVBItemType
import com.tencent.lib.multi.core.MultiViewHolder
import com.tencent.multiadapter.R
import com.tencent.multiadapter.databinding.ItemBBinding
import com.tencent.multiadapter.databinding.ItemCBinding
import com.tencent.multiadapter.example.bean.ItemBean

/**

 * Author：岑胜德 on 2021/9/1 14:59

 * 说明：

 */
class ItemBeanType:MultiVBItemType<ItemBean,ItemBBinding>() {


    override fun matchItemType(bean: Any?, position: Int): Boolean {
        return bean is ItemBean
    }

    override fun onViewHolderCreated(holder: MultiViewHolder, helper: MultiHelper<ItemBean, MultiViewHolder>) {
        registerItemViewClickListener(holder,helper,"onClickItemBean")
    }
    override fun onBindViewHolder(vb: ItemBBinding, bean: ItemBean, position: Int) {
        vb.tvB.text=bean.text
    }

    override fun getItemLayoutRes(): Int {
      return R.layout.item_b
    }
}