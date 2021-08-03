package com.tencent.multiadapter.example.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.DiffUtil
import com.tencent.lib.multi.MultiAdapter2
import com.tencent.multiadapter.R
import com.tencent.multiadapter.example.bean.ItemBean
import com.tencent.multiadapter.example.itemtype.AItemType
import com.tencent.multiadapter.example.itemtype.BItemType
import com.tencent.multiadapter.example.itemtype.CItemType
import kotlinx.android.synthetic.main.activity_diff_util_demo.*
import java.util.ArrayList

class DiffUtilDemoActivity : AppCompatActivity() {

    val callback = object : DiffUtil.ItemCallback<ItemBean>() {

        /*是否item 相同*/
        override fun areItemsTheSame(oldItem: ItemBean, newItem: ItemBean): Boolean {
            val r = oldItem.id == newItem.id
            Log.e("===>", "是否item 相同:$r")
            return r
        }

        /*是否item内容相同*/
        override fun areContentsTheSame(oldItem: ItemBean, newItem: ItemBean): Boolean {
            val r = oldItem == newItem
            Log.e("===>", "是否item 内容相同:$r")
            return r

        }

        override fun getChangePayload(oldItem: ItemBean, newItem: ItemBean): Any? {
            val bundle = Bundle()
            if (oldItem.text != newItem.text) {
                bundle.putString("content", newItem.text)
            }
            return bundle
        }


    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_diff_util_demo)
        val adapter = MultiAdapter2<ItemBean>(callback)
        adapter.multiHelper
                .addItemType(AItemType())
                .addItemType(BItemType())
                .addItemType(CItemType())
        rv_list.adapter = adapter
        adapter.setData(getData(5, "初始内容"))

        /*下拉刷新*/
        refresh_layout.setOnRefreshListener {
            adapter.setData(getData(3, "下拉刷新内容"))
            it.finishRefresh(1000)
        }

        /*上拉加载更多*/
        refresh_layout.setOnLoadMoreListener {
            adapter.setData(getData(10, "上拉加载更多内容"))
            it.finishLoadMore(1000)

        }

    }


    fun getData(size: Int, content: String): MutableList<ItemBean?> {
        val list = ArrayList<ItemBean?>(size)
        for (i in 0 until size) {
            var data: ItemBean? = null
            when (i % 3) {
                0 -> {
                    data = ItemBean(i, ItemBean.TYPE_A, "A 类item：$content :${i}")
                }
                1 -> {
                    data = ItemBean(i, ItemBean.TYPE_B, "B 类item：$content :${i}")
                }
                2 -> {
                    data = ItemBean(i, ItemBean.TYPE_C, "C 类item：$content :${i}")
                }
            }
            list.add(data)
        }
        return list
    }

}