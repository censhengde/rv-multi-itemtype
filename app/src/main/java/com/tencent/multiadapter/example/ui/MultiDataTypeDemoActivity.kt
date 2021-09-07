package com.tencent.multiadapter.example.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.tencent.lib.multi.MultiAdapter2
import com.tencent.multiadapter.R
import com.tencent.multiadapter.example.bean.CheckableBean
import com.tencent.multiadapter.example.bean.ItemBean
import com.tencent.multiadapter.example.bean.PagedBean
import com.tencent.multiadapter.example.itemtype.multiDatatype.CheckableBeanType
import com.tencent.multiadapter.example.itemtype.multiDatatype.PagedBeanType
import com.tencent.multiadapter.example.itemtype.multiDatatype.ItemBeanType
import kotlinx.android.synthetic.main.activity_multi_data_type_demo.*
import java.util.ArrayList

/**
 * 不同item类型的实体类类型不同用例
 */
class MultiDataTypeDemoActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_multi_data_type_demo)
        /*必须传Any类型才能兼容不同bean类型*/
        val adapter=MultiAdapter2<Any>()

        adapter.addItemType(CheckableBeanType())
        adapter.addItemType(PagedBeanType())
        adapter.addItemType(ItemBeanType())

        adapter.setData(getData())
        rv_list.adapter=adapter

    }


    fun getData():MutableList<Any>{
        val data=ArrayList<Any>()

        data.add(CheckableBean(0, "我是CheckableBean"))
        data.add(ItemBean(0,"我是ItemBean"))
        data.add(ItemBean(0,"我是ItemBean"))
        data.add(ItemBean(0,"我是ItemBean"))
        data.add(PagedBean("我是PagedBean"))
        data.add(ItemBean(0,"我是ItemBean"))
        data.add(PagedBean("我是PagedBean"))
        data.add(ItemBean(0,"我是ItemBean"))
        data.add(PagedBean("我是PagedBean"))
        data.add(PagedBean("我是PagedBean"))
        return data
    }
    private fun onClickCheckableItem(view:View,bean: CheckableBean,position:Int){
        Toast.makeText(this,bean.text,Toast.LENGTH_SHORT).show()
    }
    private fun onClickItemBean(view:View,bean: ItemBean,position:Int){
        Toast.makeText(this,bean.text,Toast.LENGTH_SHORT).show()
    }
    private fun onClickPagedItem(view:View,bean: PagedBean,position:Int){
        Toast.makeText(this,bean.text,Toast.LENGTH_SHORT).show()
    }

}