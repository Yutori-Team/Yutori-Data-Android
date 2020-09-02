package yutori.tf.hangul

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import org.jetbrains.anko.toast
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import yutori.tf.hangul.data.PostLoginResponse
import yutori.tf.hangul.db.SharedPreferenceController
import yutori.tf.hangul.login.JoinActivity
import yutori.tf.hangul.login.LoginActivity
import yutori.tf.hangul.network.ApplicationController
import yutori.tf.hangul.network.NetworkService
import yutori.tf.hangul.process.HomeActivity

class SplashActivity : AppCompatActivity() {

    lateinit var networkService: NetworkService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        init()
    }

    private fun init() {
        SharedPreferenceController.instance!!.load(this)
        networkService = ApplicationController.instance.networkService
        moveActivity()
    }

    private fun moveActivity() {
        val handler = Handler()
        handler.postDelayed(Runnable {
            val auto_login_flag = SharedPreferenceController.instance!!.getPrefBooleanData("auto_login")
            val intent: Intent
            if (auto_login_flag!!) {
                postLoginResponse()
                intent = Intent(applicationContext, HomeActivity::class.java)
            } else {
                intent = Intent(applicationContext, LoginActivity::class.java)
            }
            startActivity(intent)
            finish()
        }, 1000)
    }

    private fun postLoginResponse() {
        val userId = SharedPreferenceController.instance!!.getPrefStringData("login_id")
        val userPw = SharedPreferenceController.instance!!.getPrefStringData("login_pw")

        var jsonObject = JSONObject()
        jsonObject.put("userId", userId)
        jsonObject.put("userPw", userPw)

        val gsonObject = JsonParser().parse(jsonObject.toString()) as JsonObject

        val postLoginResponse = networkService.postLoginResponse(gsonObject)

        postLoginResponse.enqueue(object : Callback<PostLoginResponse> {
            override fun onFailure(call: Call<PostLoginResponse>, t: Throwable) {
                Log.i("Error SplashActivity : ", t.message.toString())
                toast(t.message.toString())
            }

            override fun onResponse(call: Call<PostLoginResponse>, response: Response<PostLoginResponse>) {

                response.let {
                    when (it.code()) {
                        200 -> {
                            SharedPreferenceController.instance?.setPrefData("userId", response.body()!!.id)
                            SharedPreferenceController.instance?.setPrefData("authorization", response.body()!!.token)
                        }
                        400 -> {
                            toast("400")
                        }
                        500 -> {
                            val intent = Intent(applicationContext, LoginActivity::class.java)
                            startActivity(intent)
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
