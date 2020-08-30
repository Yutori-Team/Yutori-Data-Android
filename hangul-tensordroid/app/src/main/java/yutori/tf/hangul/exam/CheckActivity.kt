package yutori.tf.hangul.exam

import android.content.Intent
import android.content.pm.ActivityInfo
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import kotlinx.android.synthetic.main.activity_check.*
import org.jetbrains.anko.toast
import org.json.JSONArray
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import yutori.tf.hangul.R
import yutori.tf.hangul.data.PostCheckResponse
import yutori.tf.hangul.data.PostLoginResponse
import yutori.tf.hangul.db.SharedPreferenceController
import yutori.tf.hangul.login.LoginActivity
import yutori.tf.hangul.network.ApplicationController
import yutori.tf.hangul.network.NetworkService
import yutori.tf.hangul.process.HomeActivity

class CheckActivity : AppCompatActivity() {

    lateinit var networkService: NetworkService

    val jsonArray = JSONArray()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_check)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
        init()
    }

    private fun init() {
        networkService = ApplicationController.instance.networkService
        setClickListener()
        postCheckResponse()
    }

    private fun setClickListener() {
        btn_check_ok.setOnClickListener {
            val intent = Intent(applicationContext, HomeActivity::class.java)
            startActivity(intent)
        }
    }

    private fun answerList() {
        for (i in 1..10) {
            val jsonObject = JSONObject()
            jsonObject.put("sentence", SharedPreferenceController.instance?.getPrefStringData("answer$i"))
            jsonArray.put(jsonObject)
        }

    }

    private fun postCheckResponse() {
        val sentenceTypes = SharedPreferenceController.instance?.getPrefStringData("sentenceTypes")
        val levelTypes = SharedPreferenceController.instance?.getPrefStringData("levelTypes")
        val numTypes = SharedPreferenceController.instance?.getPrefStringData("numTypes")
        val userId = SharedPreferenceController.instance?.getPrefLongData("userId")
        answerList()

        val jsonObject = JSONObject()
        jsonObject.put("sentenceTypes", sentenceTypes)
        jsonObject.put("levelTypes", levelTypes)
        jsonObject.put("numTypes", numTypes)
        jsonObject.put("userId", userId)
        jsonObject.put("reqCheckDtoList", jsonArray)

        val gsonObject = JsonParser().parse(jsonObject.toString()) as JsonObject
        val postCheckResponse = networkService.postCheckResponse(gsonObject)

        postCheckResponse.enqueue(object : Callback<PostCheckResponse> {
            override fun onFailure(call: Call<PostCheckResponse>, t: Throwable) {
                Log.d("Error CheckActivity : ", t.message.toString())
                toast(t.message.toString())
            }

            override fun onResponse(call: Call<PostCheckResponse>, response: Response<PostCheckResponse>) {

                response.let {
                    when (it.code()) {
                        200 -> {
                            toast("200")
                            tv_check_score.setText(response.body()?.score.toString())
                            tv_check_answer1.setText(response.body()?.resCheckDtoList?.get(0)?.sentence)
                            tv_check_answer2.setText(response.body()?.resCheckDtoList?.get(1)?.sentence)
                            tv_check_answer3.setText(response.body()?.resCheckDtoList?.get(2)?.sentence)
                            tv_check_answer4.setText(response.body()?.resCheckDtoList?.get(3)?.sentence)
                            tv_check_answer5.setText(response.body()?.resCheckDtoList?.get(4)?.sentence)
                            tv_check_answer6.setText(response.body()?.resCheckDtoList?.get(5)?.sentence)
                            tv_check_answer7.setText(response.body()?.resCheckDtoList?.get(6)?.sentence)
                            tv_check_answer8.setText(response.body()?.resCheckDtoList?.get(7)?.sentence)
                            tv_check_answer9.setText(response.body()?.resCheckDtoList?.get(8)?.sentence)
                            tv_check_answer10.setText(response.body()?.resCheckDtoList?.get(9)?.sentence)

//                            if (response.body()?.resCheckDtoList?.get(0)?.match == true){
//
//                            } else {
//
//                            }
                        }
                        400 -> {
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
