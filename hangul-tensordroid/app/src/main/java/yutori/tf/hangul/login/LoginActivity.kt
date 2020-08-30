package yutori.tf.hangul.login

import android.content.Intent
import android.content.pm.ActivityInfo
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import kotlinx.android.synthetic.main.activity_login.*
import org.jetbrains.anko.toast
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import yutori.tf.hangul.process.HomeActivity
import yutori.tf.hangul.R
import yutori.tf.hangul.data.PostLoginResponse
import yutori.tf.hangul.db.SharedPreferenceController
import yutori.tf.hangul.network.ApplicationController
import yutori.tf.hangul.network.NetworkService

class LoginActivity : AppCompatActivity() {

    lateinit var networkService: NetworkService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
        init()
    }

    private fun init() {
        networkService = ApplicationController.instance.networkService
        SharedPreferenceController.instance!!.load(this)
        setClickListener()
    }

    private fun setClickListener() {
        btn_login_login.setOnClickListener {
            val input_id: String = et_login_id.text.toString()
            val input_pw: String = et_login_pw.text.toString()

            if (input_id.isNotEmpty() && input_pw.isNotEmpty()) {
                postLoginResponse()
            }
        }

        btn_login_join.setOnClickListener {
            val intent = Intent(applicationContext, JoinActivity::class.java)
            startActivity(intent)
        }

    }

    private fun postLoginResponse() {
        val input_id = et_login_id.text.toString().trim()
        val input_pw = et_login_pw.text.toString().trim()

        var jsonObject = JSONObject()
        jsonObject.put("userId", input_id)
        jsonObject.put("userPw", input_pw)

        val gsonObject = JsonParser().parse(jsonObject.toString()) as JsonObject

        val postLoginResponse = networkService.postLoginResponse(gsonObject)

        postLoginResponse.enqueue(object : Callback<PostLoginResponse> {
            override fun onFailure(call: Call<PostLoginResponse>, t: Throwable) {
                Log.i("Error LoginActivity : ", t.message.toString())
                toast(t.message.toString())
            }

            override fun onResponse(call: Call<PostLoginResponse>, response: Response<PostLoginResponse>) {

                response.let {
                    when (it.code()) {
                        200 -> {
                            toast("200 로그인 성공")
                            SharedPreferenceController.instance?.setPrefData("userId", response.body()!!.id)
                            startActivity(Intent(this@LoginActivity, HomeActivity::class.java))
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