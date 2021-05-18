package com.tencent.multiadapter.example.ui

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.tencent.lib.multi.MultiAdapter
import com.tencent.multiadapter.R
import com.tencent.multiadapter.example.bean.ItemBean
import com.tencent.multiadapter.example.itemtype.AItemType
import com.tencent.multiadapter.example.itemtype.BItemType
import com.tencent.multiadapter.example.itemtype.CItemType
import kotlinx.android.synthetic.main.activity_multi_item.*
import java.util.*

class MultiItemActivity : AppCompatActivity() {

    lateinit var adapter: MultiAdapter<ItemBean>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_multi_item)
        //初始化ItemType
        val aItemType = AItemType()

        aItemType.bind(this)

        val bItemType = BItemType()
        val cItemType = CItemType()
        bItemType.bind(this)
        cItemType.bind(this)
        /*初始化Adapter*/
        adapter = MultiAdapter<ItemBean>()
        /*将所有ItemType添加到Adapter中*/
        adapter.multiHelper.addItemType(aItemType)
                .addItemType(bItemType)
                .addItemType(cItemType)
        /*设置数据*/
        adapter.setData(getData())
        rv_list.adapter = adapter

    }

    /* bItemType.bind(this)
        cItemType.bind(this)*/
    /**
     * 模拟数据
     */
    private fun getData(): List<ItemBean> {
        val beans = ArrayList<ItemBean>()
        for (i in 0..5) {
            beans.add(ItemBean(ItemBean.TYPE_A, "我是A类Item$i"))
            beans.add(ItemBean(ItemBean.TYPE_B, "我是B类Item${i + 1}"))
            beans.add(ItemBean(ItemBean.TYPE_C, "我是C类Item${i + 2}"))
        }
        return beans
    }

    /**
     *item点击事件
     */
    private fun onClickItem(view: View, itemBean: ItemBean, position: Int) {
        Toast.makeText(this, "ItemBean:${itemBean.text},position:$position", Toast.LENGTH_SHORT).show()
    }
    /**
     * item 子View 点击事件
     */
    private fun onClickItemChildView(view: View, itemBean: ItemBean, position: Int) {
        Toast.makeText(this, "ItemBean:${itemBean.text},position:$position", Toast.LENGTH_SHORT).show()
    }
    /**
     * item 子View 长点击事件
     */
    private fun onLongClickItemChildView(view: View, itemBean: ItemBean, position: Int) :Boolean{
        Toast.makeText(this, "ItemBean:${itemBean.text},position:$position", Toast.LENGTH_SHORT).show()
        return true
    }

    /**
     * Item 长点击事件
     */
    private fun onLongClickItem(view: View, itemBean: ItemBean, position: Int): Boolean {
        Toast.makeText(this, "ItemBean:${itemBean.text},position:$position", Toast.LENGTH_SHORT).show()
        return true
    }

}
