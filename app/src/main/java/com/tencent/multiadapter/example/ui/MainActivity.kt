package com.tencent.multiadapter.example.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.tencent.multiadapter.R

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun onClickBtn(view: View) {
        val btn = (view as Button)
        when (btn.id) {
            R.id.btn_to_multi -> {
                goTo(MultiItemDemoActivity::class.java)
            }
            R.id.btn_to_check -> {
                goTo(CheckingDemoActivity::class.java)
            }
            R.id.btn_to_check_single -> {
                goTo(SingleCheckingDemoActivity::class.java)
            }
            R.id.btn_to_diff_demo -> {
                goTo(DiffUtilDemoActivity::class.java)
            }

            R.id.btn_to_viewbinding_demo -> {
                goTo(ViewBindingDemoActivity::class.java)
            }
            R.id.btn_to_multi_data_type_demo -> {
                goTo(MultiDataTypeDemoActivity::class.java)
            }
            else -> {
            }
        }

    }

    private fun <T : Activity> goTo(clazz: Class<T>) {
        startActivity(Intent(this, clazz))
    }

}