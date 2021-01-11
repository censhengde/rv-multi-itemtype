package com.tencent.multirecyclerview.example

import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.tencent.multirecyclerview.R

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