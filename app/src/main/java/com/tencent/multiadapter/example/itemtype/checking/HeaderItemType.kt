package com.tencent.multiadapter.example.itemtype.checking

import com.tencent.lib.multi.core.MultiHelper
import com.tencent.lib.multi.core.MultiItemType
import com.tencent.lib.multi.core.MultiViewHolder
import com.tencent.multiadapter.R
import com.tencent.multiadapter.example.bean.CheckableItem

/**

 * Author：岑胜德 on 2021/5/12 18:17

 * 说明：

 */
class HeaderItemType:MultiItemType<CheckableItem>() {

    override fun getItemLayoutRes(): Int = R.layout.item_checking_header

    override fun matchItemType(data: CheckableItem, position: Int): Boolean
            =data.viewType== CheckableItem.VIEW_TYPE_HEADER

    override fun onBindViewHolder(holder: MultiViewHolder, helper: MultiHelper<CheckableItem,MultiViewHolder>, position: Int) {

    }
}