package com.tencent.multirecyclerview.example.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DiffUtil
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
        pagedRecyclerView
                /*风格*/
                .linearBuilder()
                .setItemSpace(5, 5, 5, 5)
                .build()
                /*分页*/
        pagedRecyclerView
                .pagedBuilder()
                .setDiffCallback(object : DiffUtil.ItemCallback<ItemBean>() {
                    override fun areItemsTheSame(oldItem: ItemBean, newItem: ItemBean): Boolean {
                       return oldItem.equals(newItem)
                    }

                    override fun areContentsTheSame(oldItem: ItemBean, newItem: ItemBean): Boolean {
                       return oldItem.text==newItem.text
                    }

                })
                .build(this)
        /*删除Item事件*/


    }

    private fun initRefreshLayout() {
        /*下拉刷新*/
        refresh_layout.setOnRefreshListener {
//           pagedRecyclerView.getItemManager().refreshAll(object :OnRefreshListener{
//               override fun onSuccess() {
//
//               }
//
//               override fun onFailure() {
//
//               }
//
//           })
        }
        /*上拉加载更多*/
        refresh_layout.setOnLoadMoreListener {
//            pagedRecyclerView.getItemManager().loadMore(object :OnLoadMoreListener{
//                override fun onSuccess() {
//
//                }
//
//                override fun onFailure() {
//
//                }
//
//                override fun onNomore() {
//
//                }
//
//            })
        }
    }
}
