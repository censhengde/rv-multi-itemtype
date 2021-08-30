package com.tencent.multiadapter.example.itemtype.vb

import android.os.Parcel
import android.os.Parcelable
import com.tencent.lib.multi.core.MultiVBItemType
import com.tencent.multiadapter.R
import com.tencent.multiadapter.databinding.ItemABinding
import com.tencent.multiadapter.example.bean.ItemBean

/**

 * Author：岑胜德 on 2021/8/30 12:03

 * 说明：

 */
class AVBItemType : MultiVBItemType<ItemBean, ItemABinding>(){

    override fun matchItemType(bean: ItemBean?, position: Int): Boolean {
        return bean!!.viewType==ItemBean.TYPE_A
    }


    override fun getItemLayoutRes(): Int {
        return R.layout.item_a
    }

    override fun onBindViewHolder(vb: ItemABinding, bean: ItemBean, position: Int) {

        vb.tvA.text=bean.text

    }


}