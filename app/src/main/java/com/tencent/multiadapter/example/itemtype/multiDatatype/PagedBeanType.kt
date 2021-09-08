package com.tencent.multiadapter.example.itemtype.multiDatatype

import com.tencent.lib.multi.core.MultiHelper
import com.tencent.lib.multi.core.MultiVBItemType
import com.tencent.lib.multi.core.MultiViewHolder
import com.tencent.multiadapter.R
import com.tencent.multiadapter.databinding.ItemCBinding
import com.tencent.multiadapter.example.bean.PagedBean

/**

 * Author：岑胜德 on 2021/9/1 10:25

 * 说明：

 */
class PagedBeanType( observer: Any) : MultiVBItemType<PagedBean, ItemCBinding>() {

    init {
        inject(observer)
    }
    override fun matchItemType(bean: Any?, position: Int): Boolean {
        return bean is PagedBean
    }

    override fun onViewHolderCreated(holder: MultiViewHolder, helper: MultiHelper<PagedBean, MultiViewHolder>) {
        registerItemViewClickListener(holder,helper,"onClickPagedItem")
    }
    override fun onBindViewHolder(vb: ItemCBinding, bean: PagedBean, position: Int) {
        vb.tvC.text = bean.text
    }

    override fun getItemLayoutRes(): Int {
        return R.layout.item_c
    }
}