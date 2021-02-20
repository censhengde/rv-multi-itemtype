package com.tencent.multirecyclerview.example.ui

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.tencent.lib.widget.ItemType
import com.tencent.lib.widget.MultiViewHolder
import com.tencent.lib.widget.OnCompletedCheckedCallback
import com.tencent.multirecyclerview.R
import com.tencent.multirecyclerview.example.bean.ItemBean
import kotlinx.android.synthetic.main.activity_select.*

class SelectActivity : AppCompatActivity(), OnCompletedCheckedCallback<ItemBean> {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_select)
        rv_selectable.gridBuilder(3).build()
        rv_selectable.adapterBuilder()
                .checkable(true)
                .setSingleSelection(false)
                .setItemType(SelectedItem())
                .setOnCompletedCheckedCallback(this)
                .setDatas(initData())
                .build()

    }

    fun initData(): MutableList<ItemBean> {
        val datas = ArrayList<ItemBean>()

        for (i in 0..20) {
            datas.add(ItemBean(0, "Item:$i"))
        }
        return datas
    }


    class SelectedItem : ItemType<ItemBean> {
        override fun getViewType(): Int =0

        override fun matchItemType(data: ItemBean, position: Int): Boolean = true

        override fun getItemLayoutRes(): Int {
           return R.layout.item_checkable
        }

        override fun onBindViewHolder(holder: MultiViewHolder, data: ItemBean, position: Int) {
//            holder.getView<TextView>(R.id.tv)?.setTextColor(android.graphics.Color.GRAY)
            holder.getView<TextView>(R.id.tv)?.setText(data.text)

        }

        override fun onInitItemSubViewListener(holder: MultiViewHolder?) {

        }

        override fun onClickItemView(holder: MultiViewHolder?, data: ItemBean, position: Int) {
            if (data.isChecked) {
                holder?.getView<TextView>(R.id.tv)?.setTextColor(android.graphics.Color.RED)
            } else {
                holder?.getView<TextView>(R.id.tv)?.setTextColor(android.graphics.Color.GRAY)
            }

        }
    }

    override fun onCompletedChecked(datas: MutableList<ItemBean>) {
        datas.forEach {
            Log.e("被选中：", it.text)
        }
    }

    fun onClickCompleted(view: View) {
         rv_selectable.completed()
    }


}
