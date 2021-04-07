package com.tencent.multirecyclerview.example.ui

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.tencent.lib.multi.MultiAdapter
import com.tencent.lib.multi.core.OnClickItem
import com.tencent.lib.multi.core.OnClickItemSubView
import com.tencent.lib.multi.core.OnLongClickItemSubView
import com.tencent.multirecyclerview.R
import com.tencent.multirecyclerview.example.bean.ItemBean
import com.tencent.multirecyclerview.example.itemtype.AItemType
import kotlinx.android.synthetic.main.activity_annotation_test.*
import java.util.*

class AnnotationTestActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_annotation_test)
        val aItemType = AItemType()
        aItemType.regist(null, this, true, true, false, true)
        val adapter = MultiAdapter<ItemBean>()
        adapter.addItemType(aItemType).setDatas(getDatas())
        recyclerview.adapter = adapter
        recyclerview.linearBuilder().build()
    }

    private fun getDatas(): MutableList<ItemBean> {
        val itemBeans = ArrayList<ItemBean>(10)
        for (i in 0..9) {
            itemBeans.add(ItemBean(0, "hahahahaha $i"))
        }
        return itemBeans
    }

    @OnClickItem
    private fun onClickItem(view: View, itemBean: ItemBean, position: Int) {
        Toast.makeText(this, "item 条目点击：${itemBean.text}", Toast.LENGTH_SHORT).show()
    }

    /*由于 view id 在module中不是常量，所以采用tag作为view 身份标识*/
    @OnClickItemSubView(tags= ["tv_a"])
    private fun onClickItemSubView(view: View, itemBean: ItemBean, position: Int) {

        Toast.makeText(this, "item 条目sub view点击：${itemBean.text}", Toast.LENGTH_SHORT).show()
    }
    /*由于 view id 在module中不是常量，所以采用tag作为view 身份标识*/
    @OnLongClickItemSubView(tags= ["tv_a"])
    private fun onLongClickItemSubView(view: View, itemBean: ItemBean, position: Int) :Boolean{
        Toast.makeText(this, "item 条目sub view长按点击：${itemBean.text}", Toast.LENGTH_SHORT).show()
      return true
    }
}
