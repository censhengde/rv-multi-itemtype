package com.tencent.multiadapter.example.itemtype.checking

import android.widget.CheckBox
import android.widget.TextView
import com.tencent.lib.multi.core.MultiHelper
import com.tencent.lib.multi.core.MultiItemType
import com.tencent.lib.multi.core.MultiViewHolder
import com.tencent.multiadapter.R
import com.tencent.multiadapter.example.bean.CheckableBean

/**

 * Author：岑胜德 on 2021/5/12 18:17

 * 说明：

 */
class CheckableItemType : MultiItemType<CheckableBean>() {


    override fun getItemLayoutRes(): Int = R.layout.item_checking_checkable

    override fun matchItemType(bean: Any?, position: Int): Boolean {
        return (bean as CheckableBean).viewType== CheckableBean.VIEW_TYPE_CHECKABLE
    }

    override fun onViewHolderCreated(holder: MultiViewHolder, helper: MultiHelper<CheckableBean, MultiViewHolder>) {
        registerItemViewClickListener(holder, helper, "onClickItem")
    }


    /**
     * 只有局部刷新才会回调到这里，RecyclerView上下滑动则不会，有区别于RecyclerView.Adapter中的实现.
     */
    override fun onBindViewHolder(holder: MultiViewHolder, bean: CheckableBean, position: Int,
                                  payloads: MutableList<Any>) {
        payloads.forEach {
            if (it is Int)
                if (it == R.id.checkbox) {
                    val checkbox = holder.getView<CheckBox>(R.id.checkbox)
                    checkbox.isChecked = bean.isChecked
                }
        }
    }

    override fun onBindViewHolder(holder: MultiViewHolder, bean: CheckableBean, position: Int) {

        val tv = holder.getView<TextView>(R.id.tv)
        tv.text = bean.text
        //CheckBox
        val checkbox = holder.getView<CheckBox>(R.id.checkbox)
        checkbox.isChecked = bean.isChecked

    }
}