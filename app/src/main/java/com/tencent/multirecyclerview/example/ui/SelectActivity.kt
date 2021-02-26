package com.tencent.multirecyclerview.example.ui

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.tencent.lib.multi.core.MultiViewHolder
import com.tencent.lib.multi.core.OnCompletedCheckItemCallback
import com.tencent.lib.multi.core.SimpleItemType
import com.tencent.multirecyclerview.R
import com.tencent.multirecyclerview.example.bean.ItemBean
import com.tencent.multirecyclerview.example.viewmodel.SelectionViewModel
import kotlinx.android.synthetic.main.activity_select.*

class SelectActivity : AppCompatActivity(), OnCompletedCheckItemCallback<ItemBean> {
  lateinit var viewmodle:SelectionViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_select)

        viewmodle=ViewModelProvider(this).get(SelectionViewModel::class.java)

        rv_selectable.linearBuilder().setItemSpace(10,10,10,10).build()
        rv_selectable.newAdapterBuilder()
//                .checkable(true)/*开启选择功能,不调用则默认 false*/
//                .setSingleSelection(false)/*是否单选模式，不调用则默认 false*/
//                .setOnCompletedCheckItemCallback(this)/*选择完成回调*/
                .setItemType(SelectionItemType())/*单样式*/
                .setDatas(viewmodle.items)/*数据源*/
                .build()

    }




    inner class SelectionItemType : SimpleItemType<ItemBean>() {

        override fun getItemLayoutRes(): Int {
           return R.layout.item_checkable
        }
        /*这里进行选中/未选中状态设置*/
        override fun onBindViewHolder(holder: MultiViewHolder, data: ItemBean, position: Int) {
            holder.getView<TextView>(R.id.tv)?.text = data.text

            if (data.isChecked) {
                holder.getView<TextView>(R.id.tv)?.setTextColor(android.graphics.Color.RED)
            } else {
                holder.getView<TextView>(R.id.tv)?.setTextColor(android.graphics.Color.GRAY)
            }

        }

        override fun onInitItemSubViewListener(holder: MultiViewHolder) {
                 //点击删除Item事件
                 holder.getView<Button>(R.id.btn_delete_item)?.setOnClickListener {
                     rv_selectable.getItemManager().removeItem(holder.absoluteAdapterPosition)
                 }
        }

    }

    override fun onCompletedChecked(datas: MutableList<ItemBean>) {
        datas.forEach {
            Log.e("被选中：", it.text)
        }
    }

    /**
     * 点击 完成
     */
    fun onClickCompleted(view: View) {
         rv_selectable.getCheckManager().complete()
    }

    /**
     * 点击全选/取消全选
     */
    fun onClickCheckAll(view: View) {
        val btn=view as Button
        if (btn.text.equals("全选")){
            rv_selectable.getCheckManager().checkAll()
            btn.text="取消全选"
        }else{
            rv_selectable.getCheckManager().cancelAll()
            btn.text="全选"
        }
    }


}
