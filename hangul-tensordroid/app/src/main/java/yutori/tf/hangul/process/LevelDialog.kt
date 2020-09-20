package yutori.tf.hangul.process

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Window
import kotlinx.android.synthetic.main.dialog_level.*
import yutori.tf.hangul.R
import yutori.tf.hangul.db.SharedPreferenceController

class LevelDialog(val ctx : Context?) : Dialog(ctx)  {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        setContentView(R.layout.dialog_level)
        init()
        setClickListener()
    }

    private fun init() {
        btn_level_high.isSelected = false
        btn_level_middle.isSelected = false
        btn_level_bottom.isSelected = false
    }

    private fun setClickListener() {

        var level = ""

        btn_level_high.setOnClickListener {
            btn_level_high.isSelected = true
            btn_level_middle.isSelected = false
            btn_level_bottom.isSelected = false
            level = "TOP";
        }

        btn_level_middle.setOnClickListener {
            btn_level_high.isSelected = false
            btn_level_middle.isSelected = true
            btn_level_bottom.isSelected = false
            level = "MIDDLE";
        }

        btn_level_bottom.setOnClickListener {
            btn_level_high.isSelected = false
            btn_level_middle.isSelected = false
            btn_level_bottom.isSelected = true
            level = "LOW";
        }

        btn_level_ok.setOnClickListener {
            if (btn_level_high.isSelected or btn_level_middle.isSelected or btn_level_bottom.isSelected) {
                SharedPreferenceController.instance?.setPrefData("levelTypes", level)
                val intent = Intent(ctx, NumselectActivity::class.java)
                ctx?.startActivity(intent)
            }
        }

        btn_level_cancel.setOnClickListener {
            dismiss()
        }

    }

}