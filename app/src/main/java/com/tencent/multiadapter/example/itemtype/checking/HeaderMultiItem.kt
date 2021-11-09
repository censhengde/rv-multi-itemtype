package com.tencent.multiadapter.example.itemtype.checking

import com.tencent.lib.multi.core.SimpleMultiItem
import com.tencent.lib.multi.core.MultiViewHolder
import com.tencent.multiadapter.R
import com.tencent.multiadapter.example.bean.CheckableBean

/**

 * Author：岑胜德 on 2021/5/12 18:17

 * 说明：

 */
class HeaderMultiItem: SimpleMultiItem<CheckableBean>() {

    override fun getItemLayoutRes(): Int = R.layout.item_checking_header

    override fun isMatchForMe(bean: Any?, position: Int): Boolean
            =(bean as CheckableBean).viewType== CheckableBean.VIEW_TYPE_HEADER

    override fun onBindViewHolder(holder: MultiViewHolder, bean: CheckableBean, position: Int) {

    }
}