package yutori.tf.hangul.exam;

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Window
import kotlinx.android.synthetic.main.dialog_wrong.*
import yutori.tf.hangul.R


class WrongDialog(ctx : Context?) : Dialog(ctx)  {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        setContentView(R.layout.dialog_wrong)
        getWrongResponse()

        btn_wrong_close.setOnClickListener {
            dismiss()
        }
    }

    private fun getWrongResponse() {

    }
}
