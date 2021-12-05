package com.tencent.multiadapter.example.ui

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.annotation.Keep
import androidx.appcompat.app.AppCompatActivity
import com.tencent.lib.multi.MultiAdapter2
import com.tencent.multiadapter.R
import com.tencent.multiadapter.example.bean.AItemBean
import com.tencent.multiadapter.example.bean.ItemBean
import com.tencent.multiadapter.example.item.*
import kotlinx.android.synthetic.main.activity_multi_item.*
import java.util.*

class MultiItemDemo01Activity : AppCompatActivity() {

    lateinit var adapter: MultiAdapter2

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_multi_item)
        //初始化ItemType
        val item00 = MultiItem00()

        item00.inject(this)

        val item01 = MultiItem01()
        val item02 = MultiItem02()
        item01.inject(this)
        item02.inject(this)
        /*初始化Adapter*/
        adapter = MultiAdapter2()
        /*将所有ItemType添加到Adapter中*/
        adapter.addItemType(item00)
                .addItemType(item01)
                .addItemType(item02)
        /*设置数据*/
        adapter.dataList=getData()
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
            beans.add(ItemBean(ItemBean.TYPE_00, "我是A_00类Item$i"))
            beans.add(ItemBean(ItemBean.TYPE_01, "我是A_01类Item${i + 1}"))
            beans.add(ItemBean(ItemBean.TYPE_02, "我是A_02类Item${i + 2}"))
        }
        return beans
    }

    /**
     *item点击事件
     */
    @Keep
    private fun onClickItem(view: View, itemBean: ItemBean, position: Int) {
        Toast.makeText(this, "AItemBean:${itemBean.text},position:$position", Toast.LENGTH_SHORT).show()
    }

    /**
     * item 子View 点击事件
     */
    private fun onClickItemChildView(view: View, itemBean: ItemBean, position: Int) {
        Toast.makeText(this, "AItemBean:${itemBean.text},position:$position", Toast.LENGTH_SHORT).show()
    }

    /**
     * item 子View 长点击事件
     */
    private fun onLongClickItemChildView(view: View, itemBean: ItemBean, position: Int): Boolean {
        Toast.makeText(this, "AItemBean:${itemBean.text},position:$position", Toast.LENGTH_SHORT).show()
        return true
    }

    /**
     * Item 长点击事件
     */
    private fun onLongClickItem(view: View, itemBean: AItemBean, position: Int): Boolean {
        Toast.makeText(this, "AItemBean:${itemBean.text},position:$position", Toast.LENGTH_SHORT).show()
        return true
    }

}
