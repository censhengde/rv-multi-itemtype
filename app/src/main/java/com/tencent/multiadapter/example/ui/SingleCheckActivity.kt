package com.tencent.multiadapter.example.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import com.tencent.lib.multi.MultiAdapter
import com.tencent.lib.multi.core.MultiViewHolder
import com.tencent.lib.multi.core.listener.OnCheckingFinishedCallback
import com.tencent.multiadapter.R
import com.tencent.multiadapter.example.bean.CheckableItem
import com.tencent.multiadapter.example.itemtype.checking.CheckableItemType
import com.tencent.multiadapter.example.itemtype.checking.FooterItemType
import com.tencent.multiadapter.example.itemtype.checking.HeaderItemType
import kotlinx.android.synthetic.main.activity_check_item.*

class SingleCheckActivity : AppCompatActivity(), OnCheckingFinishedCallback<CheckableItem> {

    val adapter = MultiAdapter<CheckableItem, MultiViewHolder>()
    val dataSize = 30
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_single_check)
        val checkableItemType = CheckableItemType()
        /*注册item点击监听*/
        checkableItemType.inject(this)

        /*添加ItemType*/
        adapter.multiHelper.addItemType(HeaderItemType())
                .addItemType(checkableItemType)
                .addItemType(FooterItemType())

        //设置完成选择的回调
        adapter.checkingHelper.setOnCheckingFinishedCallback(this)

        adapter.setData(getData())
        rv_list.adapter = adapter
    }
    /*模拟数据(页面状态的改变可能会导致列表选择状态丢失，建议在ViewModel或其他序列化手段保存数据以便恢复列表选择状态)
   * */
    private fun getData(): MutableList<CheckableItem> {
        val data = ArrayList<CheckableItem>(dataSize + 2)
        /*头布局item 实体对象*/
        data.add(CheckableItem(CheckableItem.VIEW_TYPE_HEADER, ""))
        /*中间可选的item实体对象*/
        for (i in 0 until dataSize) {
            data.add(CheckableItem(CheckableItem.VIEW_TYPE_CHECKABLE, "可选的Item position=${i}"))
        }
        /*脚布局item实体对象*/
        data.add(CheckableItem(CheckableItem.VIEW_TYPE_FOOTER, ""))
        return data
    }

    /*点击完成*/
    fun onClickFinished(view: View) {
        adapter.checkingHelper.finishChecking()
    }

    /*点击可选的item*/
    private fun onClickItem(view: View, item: CheckableItem, position: Int) {

      adapter.checkingHelper.singleCheckItem(position,R.id.checkbox)

    }
    /*点击完成时的数据回调*/
    override fun onCheckingFinished(checked: List<CheckableItem>) {
        checked.forEach {
            Log.e("被选中的item：", it.text)
        }
    }
}
