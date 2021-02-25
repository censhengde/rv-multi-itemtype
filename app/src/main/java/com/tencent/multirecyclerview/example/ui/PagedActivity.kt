package com.tencent.multirecyclerview.example.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DiffUtil
import com.tencent.multirecyclerview.R
import com.tencent.multirecyclerview.example.bean.ItemBean
import com.tencent.multirecyclerview.example.datasource.MyPagedSource
import kotlinx.android.synthetic.main.activity_paged.*

class PagedActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_paged)
        initRecyclerView()
        initRefreshLayout()

    }

    private fun initRecyclerView() {
        rv_paged
                /*风格*/
                .linearBuilder()
                .setItemSpace(5, 5, 5, 5)
                .build()
                /*分页*/
        rv_paged
                .adapterBuilder()
                .setDiffCallback(object : DiffUtil.ItemCallback<ItemBean>() {
                    override fun areItemsTheSame(oldItem: ItemBean, newItem: ItemBean): Boolean {
                       return oldItem.equals(newItem)
                    }

                    override fun areContentsTheSame(oldItem: ItemBean, newItem: ItemBean): Boolean {
                       return oldItem.text==newItem.text
                    }

                })
                .setDataSource(MyPagedSource())
                .build(this)
        /*删除Item事件*/


    }

    private fun initRefreshLayout() {
        /*下拉刷新*/
        refresh_layout.setOnRefreshListener {
            rv_paged.getPagedManager().refresh()
        }
        /*上拉加载更多*/
        refresh_layout.setOnLoadMoreListener {
            rv_paged.getPagedManager().retry()
        }
    }
}
