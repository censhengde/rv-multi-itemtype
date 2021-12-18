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
            R.id.demo_01 -> {
                goTo(MultiItemDemo01Activity::class.java)
            }
            R.id.demo_02 -> {
                goTo(MultiItemDemo02Activity::class.java)
            }

        }

    }

    private fun <T : Activity> goTo(clazz: Class<T>) {
        startActivity(Intent(this, clazz))
    }

}