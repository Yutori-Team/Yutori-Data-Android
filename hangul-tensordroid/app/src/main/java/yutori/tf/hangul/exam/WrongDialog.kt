package yutori.tf.hangul.exam;

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.Window
import kotlinx.android.synthetic.main.dialog_wrong.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import yutori.tf.hangul.R
import yutori.tf.hangul.data.GetWrongResponse
import yutori.tf.hangul.db.SharedPreferenceController
import yutori.tf.hangul.network.ApplicationController
import yutori.tf.hangul.network.NetworkService


class WrongDialog(ctx: Context?, var sentenceId: Long?) : Dialog(ctx) {

    lateinit var networkService: NetworkService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        setContentView(R.layout.dialog_wrong)
        networkService = ApplicationController.instance.networkService
        getWrongResponse()

        btn_wrong_close.setOnClickListener {
            dismiss()
        }
    }

    private fun getWrongResponse() {
        val userId = SharedPreferenceController.instance?.getPrefLongData("userId")

        val postJoinResponse = networkService.getWrongResponse(userId, sentenceId)

        postJoinResponse.enqueue(object : Callback<GetWrongResponse> {
            override fun onFailure(call: Call<GetWrongResponse>, t: Throwable) {
                Log.d("Error WrongDialog : ", t.message.toString())
            }

            override fun onResponse(call: Call<GetWrongResponse>, response: Response<GetWrongResponse>) {

                response.let {
                    when (it.code()) {
                        200 -> {
                            tv_wrong_myanswer.setText(response.body()?.mySentence)
                            tv_wrong_answer.setText(response.body()?.answer)
                        }
                        400 -> {
                        }
                        404 -> {
                        }
                        500 -> {
                        }
                        else -> {
                        }
                    }
                }
            }


        })
    }


}
