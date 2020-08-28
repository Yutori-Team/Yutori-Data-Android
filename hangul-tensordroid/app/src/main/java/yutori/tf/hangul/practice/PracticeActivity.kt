package yutori.tf.hangul.practice

import android.content.Intent
import android.content.pm.ActivityInfo
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import kotlinx.android.synthetic.main.activity_practice.*
import org.jetbrains.anko.toast
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import yutori.tf.hangul.R
import yutori.tf.hangul.data.GetSentenceResponse
import yutori.tf.hangul.data.PostLoginResponse
import yutori.tf.hangul.db.SharedPreferenceController
import yutori.tf.hangul.exam.WriteActivity
import yutori.tf.hangul.network.ApplicationController
import yutori.tf.hangul.network.NetworkService
import yutori.tf.hangul.process.HomeActivity

class PracticeActivity : AppCompatActivity() {

    lateinit var networkService: NetworkService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_practice)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
        init()
    }

    private fun init() {
        networkService = ApplicationController.instance.networkService
        setClickListener()
        getSentenceResponse()
    }

    private fun setClickListener() {
        btn_practice_next.setOnClickListener {
            val number = SharedPreferenceController.instance?.getPrefIntegerData("number_of_problem")
            if(number == 10) {
                val intent = Intent(applicationContext, PracticeEndActivity::class.java)
                startActivity(intent)
            }else {
                SharedPreferenceController.instance?.setPrefData("number_of_problem", number!!.plus(1))
                val intent = Intent(applicationContext, PracticeActivity::class.java)
                startActivity(intent)
            }
        }
        btn_practice_prev.setOnClickListener {
            val number = SharedPreferenceController.instance?.getPrefIntegerData("number_of_problem")
            if (number != 0) {
                SharedPreferenceController.instance?.setPrefData("number_of_problem", number!!.minus(1))
                val intent = Intent(applicationContext, PracticeActivity::class.java)
                startActivity(intent)
            }
        }

    }

    private fun getSentenceResponse() {
        val sentenceTypes = SharedPreferenceController.instance?.getPrefStringData("sentenceTypes")
        val levelTypes = SharedPreferenceController.instance?.getPrefStringData("levelTypes")
        val numTypes = SharedPreferenceController.instance?.getPrefStringData("numTypes")

        val getSentenceResponse = networkService.getSentenceResponse(sentenceTypes, levelTypes, numTypes)

        getSentenceResponse.enqueue(object : Callback<GetSentenceResponse> {
            override fun onFailure(call: Call<GetSentenceResponse>, t: Throwable) {
                Log.i("Error Practice : ", t.message.toString())
                toast(t.message.toString())
            }

            override fun onResponse(call: Call<GetSentenceResponse>, response: Response<GetSentenceResponse>) {
                response.let {
                    when (it.code()) {
                        200 -> {
                            toast("200")
                            Log.d("ddddddddddddd", response.body()?.resSentenceDtoList.toString())

                        }
                        400 -> {
                            toast("400")
                        }
                        404 -> {
                            toast("400")
                        }
                        500 -> {
                            toast("500")
                        }
                        else -> {
                            toast("else")
                        }
                    }
                }
            }


        })

    }
}
