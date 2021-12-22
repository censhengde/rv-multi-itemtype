package com.tencent.multiadapter.example.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DiffUtil
import com.tencent.lib.multi.MultiAdapter
import com.tencent.lib.multi.core.annotation.BindItemViewClickEvent
import com.tencent.multiadapter.databinding.ActivityMultiItemBinding
import com.tencent.multiadapter.example.bean.BeanA
import com.tencent.multiadapter.example.bean.ItemBean
import com.tencent.multiadapter.example.item.*
import java.util.*

class MultiItemDemo01Activity : AppCompatActivity() {

    lateinit var adapter: MultiAdapter
    private val vb by lazy { ActivityMultiItemBinding.inflate(LayoutInflater.from(this)) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(vb.root)
        //初始化ItemType
        val item00 = ItemType00()
        val item01 = ItemType01()
        val item02 = ItemType02()
        /*初始化Adapter*/
        adapter = MultiAdapter(this)
        /*将所有ItemType添加到Adapter中*/
        adapter.addItemType(item00)
                .addItemType(item01)
                .addItemType(item02)
        /*设置数据*/
        adapter.dataList = getData()
        vb.rvList.adapter = adapter
    }

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

}
