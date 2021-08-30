package com.tencent.multiadapter.example

import com.tencent.multiadapter.example.bean.ItemBean
import java.util.ArrayList

/**

 * Author：岑胜德 on 2021/8/30 15:19

 * 说明：

 */
fun getDataList(): MutableList<ItemBean> {
    val beans = ArrayList<ItemBean>()
    for (i in 0..5) {
        beans.add(ItemBean(ItemBean.TYPE_A, "我是A类Item$i"))
        beans.add(ItemBean(ItemBean.TYPE_B, "我是B类Item${i + 1}"))
        beans.add(ItemBean(ItemBean.TYPE_C, "我是C类Item${i + 2}"))
    }
    return beans
}