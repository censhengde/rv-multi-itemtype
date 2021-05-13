package com.tencent.multiadapter.example.ui

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.tencent.lib.multi.MultiAdapter
import com.tencent.lib.multi.core.listener.OnCheckingFinishedCallback
import com.tencent.lib.multi.core.annotation.OnClickItem
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
        checkableItemType
                .setObserver(this)
                .setRv("rv")
                .setIt("it")
                .enableClickItem()
                .regist()

        adapter.multiHelper.addItemType(HeaderItemType())
                .addItemType(checkableItemType)
                .addItemType(FooterItemType())
        adapter.checkingHelper.setOnCheckingFinishedCallback(this)
        adapter.setData(getData())
        rv_list.adapter = adapter
    }

    /**/
    private fun getData(): MutableList<CheckableItem> {
        val data = ArrayList<CheckableItem>(dataSize)
        data.add(CheckableItem(CheckableItem.VIEW_TYPE_HEADER, ""))
        for (i in 1..dataSize - 2) {
            data.add(CheckableItem(CheckableItem.VIEW_TYPE_CHECKABLE, "可选的Item position=${i}"))
        }
        data.add(CheckableItem(CheckableItem.VIEW_TYPE_FOOTER, ""))
        return data
    }

    fun onClickFinished(view: View) {
        adapter.checkingHelper.finishChecking()
    }

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

    @OnClickItem(rv = "rv", it = "it")
    private fun onClickItem(view: View, item: CheckableItem, position: Int) {
        adapter.checkingHelper.checkItem(position, R.id.checkbox)
    }

    override fun onCheckingFinished(checked: List<CheckableItem>) {
        checked.forEach {
            Log.e("被选中的item：", it.text)
        }
    }
}
