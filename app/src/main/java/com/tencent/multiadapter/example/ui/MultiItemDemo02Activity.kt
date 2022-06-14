package com.tencent.multiadapter.example.ui

import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.paging.PagingData
import androidx.recyclerview.widget.DiffUtil
import com.csd.multi.paging.MultiPagingDataAdapter
import com.tencent.multiadapter.databinding.ActivityMultiItemBinding
import com.tencent.multiadapter.example.bean.BeanA
import com.tencent.multiadapter.example.bean.BeanB
import com.tencent.multiadapter.example.bean.BeanC
import com.tencent.multiadapter.example.item.AItemType
import com.tencent.multiadapter.example.item.BItemType
import com.tencent.multiadapter.example.item.CItemType
import kotlinx.coroutines.launch
import java.util.*
/**
 * 多 bean 类型对应多样 item。
 */
class MultiItemDemo02Activity : AppCompatActivity() {

    private lateinit var adapter: MultiPagingDataAdapter
    private val vb by lazy { ActivityMultiItemBinding.inflate(LayoutInflater.from(this)) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(vb.root)
        //初始化ItemType
        val aItemType = AItemType()
        val bItemType = BItemType()
        val cItemType = CItemType()
        /*初始化Adapter*/
        adapter = MultiPagingDataAdapter(diffCallback = object :
            DiffUtil.ItemCallback<Any>() {
            override fun areItemsTheSame(oldItem: Any, newItem: Any): Boolean = false
            override fun areContentsTheSame(oldItem: Any, newItem: Any): Boolean = false

        }, 3)
        adapter.clearAllItemTypes()
        /*将所有ItemType添加到Adapter中*/
        adapter.addItemType(aItemType)
                .addItemType(bItemType)
                .addItemType(cItemType)
        vb.rvList.adapter = adapter
        /*设置数据*/
        lifecycleScope.launch {
            adapter.submitData(PagingData.from(getData()))
        }

    }

    /**
     * 模拟数据
     */
    private fun getData(): List<Any> {
        val beans = ArrayList<Any>()
        for (i in 0..5) {
            beans.add(BeanA( "我是A类Item$i"))
            beans.add(BeanB("我是B类Item${i + 1}"))
            beans.add(BeanC( "我是C类Item${i + 2}"))
        }
        return beans
    }

}
