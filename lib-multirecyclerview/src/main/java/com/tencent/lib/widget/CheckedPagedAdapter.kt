package com.tencent.lib.widget

import androidx.recyclerview.widget.DiffUtil
import java.util.*

/**

 * Author：岑胜德 on 2021/2/20 09:58

 * 说明：

 */
 internal class CheckedPagedAdapter<T : Checkable>(diffCallback: DiffUtil.ItemCallback<T>) : MultiPagingAdapter<T>(diffCallback) {
    var onCompletedCheckedCallback: OnCompletedCheckItemCallback<T>? = null

    init {
        delegateAdapter = object : CheckedDelegateAdapter<T>(this) {
            override fun complete(callback: OnCompletedCheckItemCallback<T>?) {
                /*筛选出被选中的Item*/
                val checkedItem = ArrayList<T>()
                this@CheckedPagedAdapter.snapshot().items.forEach {
                    if (it.isChecked) {
                        checkedItem.add(it)
                    }
                }
                onCompletedCheckedCallback?.onCompletedChecked(checkedItem)
            }

            override fun getItem(position: Int): T? {
                return this@CheckedPagedAdapter.getItem(position)
            }

        }
    }

}