package com.tencent.multiadapter.example.ui

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.annotation.Keep
import androidx.appcompat.app.AppCompatActivity
import com.tencent.lib.multi.MultiAdapter
import com.tencent.multiadapter.R
import com.tencent.multiadapter.example.bean.AItemBean
import com.tencent.multiadapter.example.bean.BItemBean
import com.tencent.multiadapter.example.bean.CItemBean
import com.tencent.multiadapter.example.item.AMultiItem
import com.tencent.multiadapter.example.item.BMultiItem
import com.tencent.multiadapter.example.item.CMultiItem
import kotlinx.android.synthetic.main.activity_multi_item.*
import java.util.*

class MultiItemDemo02Activity : AppCompatActivity() {

    lateinit var adapter: MultiAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_multi_item)
        //初始化ItemType
        val aItemType = AMultiItem()
        val bItemType = BMultiItem()
        val cItemType = CMultiItem()
        bItemType.inject(this)
        cItemType.inject(this)
        /*初始化Adapter*/
        adapter = MultiAdapter(this)
        /*将所有ItemType添加到Adapter中*/
        adapter.addItemType(aItemType)
                .addItemType(bItemType)
                .addItemType(cItemType)
        /*设置数据*/
        adapter.dataList=(getData())
        rv_list.adapter = adapter


    }

    /* bItemType.bind(this)
        cItemType.bind(this)*/
    /**
     * 模拟数据
     */
    private fun getData(): List<Any> {
        val beans = ArrayList<Any>()
        for (i in 0..5) {
            beans.add(AItemBean(0, "我是A类Item$i"))
            beans.add(BItemBean( "我是B类Item${i + 1}"))
            beans.add(CItemBean(0, "我是C类Item${i + 2}"))
        }
        return beans
    }



    /**
     * item 子View 点击事件
     */
    private fun onClickItemChildView(view: View, itemBean: AItemBean, position: Int) {
        Toast.makeText(this, "ItemBean:${itemBean.text},position:$position", Toast.LENGTH_SHORT).show()
    }

    /**
     * item 子View 长点击事件
     */
    private fun onLongClickItemChildView(view: View, itemBean: AItemBean, position: Int): Boolean {
        Toast.makeText(this, "ItemBean:${itemBean.text},position:$position", Toast.LENGTH_SHORT).show()
        return true
    }

    /**
     * Item 长点击事件
     */
    private fun onLongClickItem(view: View, itemBean: AItemBean, position: Int): Boolean {
        Toast.makeText(this, "ItemBean:${itemBean.text},position:$position", Toast.LENGTH_SHORT).show()
        return true
    }

}
