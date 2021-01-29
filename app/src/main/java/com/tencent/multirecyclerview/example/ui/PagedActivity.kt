package com.tencent.multirecyclerview.example.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DiffUtil
import com.tencent.lib.widget.OnLoadMoreListener
import com.tencent.lib.widget.OnRefreshListener
import com.tencent.multirecyclerview.R
import com.tencent.multirecyclerview.example.bean.ItemBean
import kotlinx.android.synthetic.main.activity_paged.*

class PagedActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_paged)
        initRecyclerView()
        initRefreshLayout()

    }

    private fun initRecyclerView() {
        /*链式调度有顺序讲究，原则是先风格、后分页*/
        multiRecyclerView
                /*风格*/
                .linearBuilder()
                .setItemSpace(5, 5, 5, 5)
                /*分页*/
                .isPaged
                .setDiffCallback(object : DiffUtil.ItemCallback<ItemBean>() {
                    override fun areItemsTheSame(oldItem: ItemBean, newItem: ItemBean): Boolean {
                        TODO("Not yet implemented")
                    }

                    override fun areContentsTheSame(oldItem: ItemBean, newItem: ItemBean): Boolean {
                        TODO("Not yet implemented")
                    }

                })
                .build()
        /*删除Item事件*/


    }

    private fun initRefreshLayout() {
        /*下拉刷新*/
        refresh_layout.setOnRefreshListener {
           multiRecyclerView.getItemManager().refreshAll(object :OnRefreshListener{
               override fun onSuccess() {

               }

               override fun onFailure() {

               }

           })
        }
        /*上拉加载更多*/
        refresh_layout.setOnLoadMoreListener {
            multiRecyclerView.getItemManager().loadMore(object :OnLoadMoreListener{
                override fun onSuccess() {

                }

                override fun onFailure() {

                }

                override fun onNomore() {

                }

            })
        }
    }
}
