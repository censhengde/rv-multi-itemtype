package com.tencent.multiadapter.example.itemtype.checking

import android.widget.CheckBox
import android.widget.TextView
import com.tencent.lib.multi.core.MultiHelper
import com.tencent.lib.multi.core.MultiViewHolder
import com.tencent.lib.multi.core.SimpleItemType
import com.tencent.multiadapter.R
import com.tencent.multiadapter.example.bean.CheckableItem

/**

 * Author：岑胜德 on 2021/5/12 18:17

 * 说明：

 */
class CheckableItemType : SimpleItemType<CheckableItem>() {
    override fun getViewType(): Int {
        return CheckableItem.VIEW_TYPE_ITEM
    }

    override fun getItemLayoutRes(): Int = R.layout.item_checking_checkable
    override fun matchItemType(data: CheckableItem, position: Int): Boolean = data.viewType == CheckableItem.VIEW_TYPE_ITEM
    override fun onViewHolderCreated(holder: MultiViewHolder, helper: MultiHelper<CheckableItem>) {
        registClickItemListener(holder, helper)
    }

    override fun onBindViewHolder(holder: MultiViewHolder, helper: MultiHelper<CheckableItem>, position: Int, payloads: MutableList<Any>) {
        payloads.forEach {
            if (it is Int)
                if (it == R.id.checkbox) {
                    val item = helper.getItem(position) ?: return
                    val checkbox = holder.getView<CheckBox>(R.id.checkbox)
                    checkbox.isChecked = item.isChecked
                }
        }
    }

    override fun onBindViewHolder(holder: MultiViewHolder, helper: MultiHelper<CheckableItem>, position: Int) {
        val item = helper.getItem(position) ?: return
        val tv = holder.getView<TextView>(R.id.tv)
        tv.text = item.text
        val checkbox = holder.getView<CheckBox>(R.id.checkbox)
        checkbox.isChecked = item.isChecked

    }
}