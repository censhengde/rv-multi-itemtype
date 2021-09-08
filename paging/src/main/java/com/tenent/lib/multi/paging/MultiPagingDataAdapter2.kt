package com.tenent.lib.multi.paging

import androidx.recyclerview.widget.DiffUtil
import com.tencent.lib.multi.core.MultiViewHolder

/**

 * Author：岑胜德 on 2021/9/8 10:04

 * 说明：

 */
open class MultiPagingDataAdapter2<T: Any>(diffCallback: DiffUtil.ItemCallback<T>)
    : MultiPagingDataAdapter<T, MultiViewHolder>(diffCallback)