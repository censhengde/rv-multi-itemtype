package com.tencent.multirecyclerview.example.ui

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.tencent.lib.multi.MultiAdapter
import com.tencent.lib.multi.core.OnClickItem
import com.tencent.lib.multi.core.OnClickItemChildView
import com.tencent.lib.multi.core.OnLongClickItemChildView
import com.tencent.multirecyclerview.R
import com.tencent.multirecyclerview.example.bean.ItemBean
import com.tencent.multirecyclerview.example.itemtype.AItemType
import com.tencent.multirecyclerview.example.itemtype.BItemType
import kotlinx.android.synthetic.main.activity_annotation_test.*
import java.util.*

class AnnotationTestActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_annotation_test)
        val aItemType = AItemType()
        aItemType.regist("recyclerview1", this, true, true, false, true)
        val adapter = MultiAdapter<ItemBean>()
        adapter.addItemType(aItemType).setDatas(getDatas(ItemBean.TYPE_A))
        recyclerview1.adapter = adapter
        recyclerview1.linearBuilder().build()

        val bItemtype = BItemType()
        bItemtype.regist("recyclerview2", this, true, true, false, false)
        val adapter2 = MultiAdapter<ItemBean>()
                .setDatas(getDatas(ItemBean.TYPE_B))
                .addItemType(bItemtype)
        recyclerview2.adapter = adapter2
        recyclerview2.gridBuilder(3).build()
    }

    private fun getDatas(type:Int): MutableList<ItemBean> {
        val itemBeans = ArrayList<ItemBean>(10)
        for (i in 0..9) {
            itemBeans.add(ItemBean(type, "hahahahaha $i"))
        }
        return itemBeans
    }

    @OnClickItem(rv="recyclerview1")
    private fun onClickItem(view: View, itemBean: ItemBean, position: Int) {
        Toast.makeText(this, "recyclerview1 item 条目点击：${itemBean.text}", Toast.LENGTH_SHORT).show()
    }
    @OnClickItem(rv="recyclerview2")
    private fun onClickItem2(view: View, itemBean: ItemBean, position: Int) {
        Toast.makeText(this, "recyclerview2 item 条目点击：${itemBean.text}", Toast.LENGTH_SHORT).show()
    }

    /*由于 view id 在module中不是常量，所以采用tag作为view 身份标识*/
    @OnClickItemChildView(rv="recyclerview1",tags= ["tv_a"])
    private fun onClickItemSubView(view: View, itemBean: ItemBean, position: Int) {
        Toast.makeText(this, " recyclerview1 item 条目sub view点击：${itemBean.text}", Toast.LENGTH_SHORT).show()
    }
    @OnClickItemChildView(rv="recyclerview2",tags= ["tv_b"])
    private fun onClickItemSubView2(view: View, itemBean: ItemBean, position: Int) {
        Toast.makeText(this, "recyclerview2 item 条目sub view点击：${itemBean.text}", Toast.LENGTH_SHORT).show()
    }
    /*由于 view id 在module中不是常量，所以采用tag作为view 身份标识*/
    @OnLongClickItemChildView(tags= ["tv_a"])
    private fun onLongClickItemSubView(view: View, itemBean: ItemBean, position: Int) :Boolean{
        Toast.makeText(this, "item 条目sub view长按点击：${itemBean.text}", Toast.LENGTH_SHORT).show()
      return true
    }
}
