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
        bItemType.bind(this)
        val cItemType = CItemType()
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
     * Item点击事件
     * rv:用于标识是哪一个RecyclerView的Item点击事件（因为一个Activity/Fragment可能有多个RecyclerView），可任意字符串。
     * it:ItemType的缩写，用于标识当前RecyclerView控件下哪一类ItemType的点击事件，可任意字符串。
     */
    private fun onClickItem(view: View, itemBean: ItemBean, position: Int) {
        Toast.makeText(this, "ItemBean:${itemBean.text},position:$position", Toast.LENGTH_SHORT).show()
    }
    /**
     * Item 子View点击事件
     * rv:用于标识是哪一个RecyclerView的Item点击事件（因为一个Activity/Fragment可能有多个RecyclerView），可任意字符串。
     * it:ItemType的缩写，用于标识当前RecyclerView控件下哪一类ItemType的点击事件，可任意字符串。
     * tags:Item 子View 的tag集合。因为同一种item中可能有多个子view响应同一点击事件。为什么不是View的id？答：因为View的id在子module中是变量。
     *           注解参数不支持变量。
     */
    private fun onClckBtnB(view: View, itemBean: ItemBean, position: Int) {
        Toast.makeText(this, "ItemBean:${itemBean.text},position:$position", Toast.LENGTH_SHORT).show()
    }

    /**
     * Item 长点击事件
     * rv:用于标识是哪一个RecyclerView的Item点击事件（因为一个Activity/Fragment可能有多个RecyclerView），可任意字符串。
     * it:ItemType的缩写，用于标识当前RecyclerView控件下哪一类ItemType的点击事件，可任意字符串。
     * return:与系统 View.OnLongClickListener  boolean onLongClick(View v)意义相同。
     */
    private fun onLongClickItem(view: View, itemBean: ItemBean, position: Int): Boolean {
        Toast.makeText(this, "ItemBean:${itemBean.text},position:$position", Toast.LENGTH_SHORT).show()
        return true
    }
/**/

    /* bItemType.setObserver(this)
                .setRv("rv")
                .setIt("item_b")
                .enableClickItemChildView()/*启用item子view点击事件监听功能*/
                .regist()
*/

}
