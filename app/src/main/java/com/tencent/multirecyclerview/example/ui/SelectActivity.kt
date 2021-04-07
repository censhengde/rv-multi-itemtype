package com.tencent.multirecyclerview.example.ui

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.tencent.lib.multi.core.MultiHelper
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


    }




    inner class SelectionItemType : SimpleItemType<ItemBean>() {

        override fun getItemLayoutRes(): Int {
           return R.layout.item_checkable
        }




        override fun onBindViewHolder(holder: MultiViewHolder, helper: MultiHelper<ItemBean>, position: Int) {

        }

    }

    override fun onCompletedChecked(datas: MutableList<ItemBean>) {
            Log.e("===>", "onCompletedChecked")
        datas.forEach {
            Log.e("被选中：", it.text)
        }
    }

    /**
     * 点击 完成
     */
    fun onClickCompleted(view: View) {
    }

    /**
     * 点击全选/取消全选
     */
    fun onClickCheckAll(view: View) {
        val btn=view as Button
        if (btn.text.equals("全选")){
            btn.text="取消全选"
        }else{
            btn.text="全选"
        }
    }


}
