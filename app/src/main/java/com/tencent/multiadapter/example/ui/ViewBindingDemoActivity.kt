package com.tencent.multiadapter.example.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.tencent.lib.multi.MultiAdapter2
import com.tencent.multiadapter.R
import com.tencent.multiadapter.example.bean.ItemBean
import com.tencent.multiadapter.example.getDataList
import com.tencent.multiadapter.example.itemtype.vb.AVBMultiItem
import com.tencent.multiadapter.example.itemtype.vb.BVBMultiItem
import com.tencent.multiadapter.example.itemtype.vb.CVBMultiItem
import kotlinx.android.synthetic.main.activity_view_binding.*

class ViewBindingDemoActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_binding)

        val adapter = MultiAdapter2<ItemBean>()

        adapter.addItemType(AVBMultiItem())
        adapter.addItemType(BVBMultiItem())
        adapter.addItemType(CVBMultiItem())

        adapter.setData(getDataList())
        rv_list.adapter=adapter
    }
}