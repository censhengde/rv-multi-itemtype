package com.tencent.lib.multi

import android.content.Context
import android.util.AttributeSet
import com.tencent.lib.multi.core.BaseRecyclerView

/**
 * Author：岑胜德 on 2021/1/6 14:51
 *
 *
 * 说明：
 */
open class MultiRecyclerView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) :
        BaseRecyclerView<MultiAdapter.Builder>(context, attrs, defStyleAttr) {


   override fun newAdapterBuilder() = MultiAdapter.Builder(this)
   fun getMultiAdapter(): MultiAdapter<*>? {
      return adapter as MultiAdapter<*>?
   }

}