package com.tencent.multiadapter.example.itemtype.checking

import com.tencent.lib.multi.core.MultiItemType
import com.tencent.lib.multi.core.MultiViewHolder
import com.tencent.multiadapter.R
import com.tencent.multiadapter.example.bean.CheckableBean

/**

 * Author：岑胜德 on 2021/5/12 18:17

 * 说明：

 */
class FooterItemType : MultiItemType<CheckableBean>() {


    override fun getItemLayoutRes(): Int = R.layout.item_checking_footer

    override fun matchItemType(bean: Any?, position: Int): Boolean = (bean as CheckableBean).viewType == CheckableBean.VIEW_TYPE_FOOTER

    override fun onBindViewHolder(holder: MultiViewHolder, bean: CheckableBean, position: Int) {

    }
}