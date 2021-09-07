package com.tencent.multiadapter.example.itemtype.multiDatatype

import com.tencent.lib.multi.core.MultiHelper
import com.tencent.lib.multi.core.MultiVBItemType
import com.tencent.lib.multi.core.MultiViewHolder
import com.tencent.multiadapter.R
import com.tencent.multiadapter.databinding.ItemCheckingCheckableBinding
import com.tencent.multiadapter.example.bean.CheckableBean

/**

 * Author：岑胜德 on 2021/9/1 14:59

 * 说明：

 */
class CheckableBeanType:MultiVBItemType<CheckableBean,ItemCheckingCheckableBinding>() {


    override fun matchItemType(bean: Any?, position: Int): Boolean {
        return bean is CheckableBean
    }

    override fun onViewHolderCreated(holder: MultiViewHolder, helper: MultiHelper<CheckableBean, MultiViewHolder>) {
        registerItemViewClickListener(holder,helper,"onClickCheckableItem")
    }
    override fun onBindViewHolder(vb: ItemCheckingCheckableBinding, bean: CheckableBean, position: Int) {
        vb.tv.text=bean.text
    }

    override fun getItemLayoutRes(): Int {
      return R.layout.item_checking_checkable
    }
}