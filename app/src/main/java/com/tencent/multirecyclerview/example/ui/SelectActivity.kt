package com.tencent.multirecyclerview.example.ui

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.tencent.lib.widget.Checkable
import com.tencent.lib.widget.ItemType
import com.tencent.lib.widget.MultiViewHolder
import com.tencent.multirecyclerview.R
import kotlinx.android.synthetic.main.activity_select.*

class SelectActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_select)
        rv_selectable.linearBuilder().setItemSpace(5, 5, 5, 5).build()
        rv_selectable.checkedBuilder()
                .setSingleSelection(true)
                .setItemType(SelectedItem())
                .build()

    }
     class CheckableData constructor(private var checked: Boolean): Checkable {

        override fun setChecked(checked: Boolean) {
           this.checked=checked
        }

        override fun isChecked(): Boolean {
            return checked
        }
    }

    class SelectedItem :ItemType<CheckableData> {
        override fun getViewType(): Int =0

        override fun matchItemType(data: CheckableData, position: Int): Boolean =true

        override fun getItemLayoutRes(): Int {
           return R.layout.item_checkable
        }

        override fun onBindViewHolder(holder: MultiViewHolder, data: CheckableData, position: Int) {
            if (data.isChecked){
                holder.getView<TextView>(R.id.tv).setTextColor(android.graphics.Color.RED)
            }else{
                holder.getView<TextView>(R.id.tv).setTextColor(android.graphics.Color.GRAY)
            }
        }

        override fun onInitItemSubViewListener(holder: MultiViewHolder?) {

        }

        override fun onClickItemView(holder: MultiViewHolder?, data: CheckableData, position: Int) {

        }
    }
}
