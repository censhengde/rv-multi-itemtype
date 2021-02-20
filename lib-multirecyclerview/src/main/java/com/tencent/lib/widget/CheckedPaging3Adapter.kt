package com.tencent.lib.widget

import androidx.recyclerview.widget.DiffUtil
import java.util.*

/**

 * Author：岑胜德 on 2021/2/20 09:58

 * 说明：

 */
 internal class CheckedPaging3Adapter<T : Checkable>(diffCallback: DiffUtil.ItemCallback<T>) : MultiPaging3Adapter<T>(diffCallback) {
    var onCompletedCheckedCallback: OnCompletedCheckedCallback<T>? = null

    init {
        delegateAdapter = object : CheckedDelegateAdapter<T>(this) {
            override fun complete(callback: OnCompletedCheckedCallback<T>?) {
                /*筛选出被选中的Item*/
                val checkedItem = ArrayList<T>()
                this@CheckedPaging3Adapter.snapshot().items.forEach {
                    if (it.isChecked) {
                        checkedItem.add(it)
                    }
                }
                onCompletedCheckedCallback?.onCompletedChecked(checkedItem)
            }

            override fun getItem(position: Int): T? {
                return this@CheckedPaging3Adapter.getItem(position)
            }

        }
    }

}