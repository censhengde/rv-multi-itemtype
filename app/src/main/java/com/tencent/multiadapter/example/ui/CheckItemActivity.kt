package com.tencent.multiadapter.example.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.tencent.multiadapter.R

class CheckItemActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_check_item)
    }

    fun onClickFinished(view: View) {}
    fun onClickCheckAll(view: View) {}
}
