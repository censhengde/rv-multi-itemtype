package com.tencent.mutilrecyclerview.example

import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.tencent.mutilrecyclerview.R
import com.tencent.mutilrecyclerview.example.bean.ItemBean
import com.tencent.mutilrecyclerview.example.itemtype.AItemType
import com.tencent.mutilrecyclerview.example.itemtype.BItemType
import com.tencent.mutilrecyclerview.example.itemtype.CItemType
import com.tencent.mutilrecyclerview.mutilrecyclerview.ItemType
import kotlinx.android.synthetic.main.activity_main.*
import java.util.ArrayList

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun onClickBtn(view: View) {
        val btn=(view as Button)
        when(btn.text){
            "线性垂直"->{ExampleActivity.start(this,ExampleActivity.DEMO_LINEAR_VERTICAL)}
            "线性水平"->{ExampleActivity.start(this,ExampleActivity.DEMO_LINEAR_HORIEN)}
            "网格效果"->{ExampleActivity.start(this,ExampleActivity.DEMO_GRID)}
            "瀑布效果"->{ExampleActivity.start(this,ExampleActivity.DEMO_FALLS)}
        }

    }


}