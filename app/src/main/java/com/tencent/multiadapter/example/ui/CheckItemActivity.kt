package com.tencent.multiadapter.example.ui

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.tencent.lib.multi.MultiAdapter
import com.tencent.lib.multi.core.listener.OnCheckingFinishedCallback
import com.tencent.multiadapter.R
import com.tencent.multiadapter.example.bean.CheckableItem
import com.tencent.multiadapter.example.itemtype.checking.CheckableItemType
import com.tencent.multiadapter.example.itemtype.checking.FooterItemType
import com.tencent.multiadapter.example.itemtype.checking.HeaderItemType
import kotlinx.android.synthetic.main.activity_check_item.*

class CheckItemActivity : AppCompatActivity(), OnCheckingFinishedCallback<CheckableItem> {
    val adapter = MultiAdapter<CheckableItem>()
    val dataSize = 30
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_check_item)
        val checkableItemType = CheckableItemType()
        /*注册item点击监听*/
        checkableItemType.bind(this)

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

    /*点击全选、取消*/
    fun onClickCheckAll(view: View) {
        val btn = (view as Button)
        when (btn.text) {
            "全选" -> {
                btn.text = "取消"
                adapter.checkingHelper.checkAll(R.id.checkbox)

            }
            "取消" -> {
                btn.text = "全选"
                adapter.checkingHelper.cancelAll(R.id.checkbox)
            }
        }
    }

    /*点击可选的item*/
    private fun onClickItem(view: View, item: CheckableItem, position: Int) {
        if (item.isChecked) {
            adapter.checkingHelper.uncheckItem(position, R.id.checkbox)
        } else {
            adapter.checkingHelper.checkItem(position, R.id.checkbox)
        }
        /*当你想实现列表单选时，请调用adapter.checkingHelper.singleCheckItem(position, R.id.checkbox)*/
    }

    /*点击完成时的数据回调*/
    override fun onCheckingFinished(checked: List<CheckableItem>) {
        checked.forEach {
            Log.e("被选中的item：", it.text)
        }
    }
}
