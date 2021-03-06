package yutori.tf.hangul.login

import android.content.Intent
import android.content.pm.ActivityInfo
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import kotlinx.android.synthetic.main.activity_join.*
import org.jetbrains.anko.toast
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import yutori.tf.hangul.R
import yutori.tf.hangul.data.PostLoginResponse
import yutori.tf.hangul.db.SharedPreferenceController
import yutori.tf.hangul.network.ApplicationController
import yutori.tf.hangul.network.NetworkService
import java.util.regex.Pattern
import kotlin.math.log

class JoinActivity : AppCompatActivity() {

    lateinit var networkService: NetworkService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_join)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
        init()
    }

    private fun init() {
        networkService = ApplicationController.instance.networkService
        setClickListener()
        checkValidation()
    }

    private fun setClickListener() {
        btn_join_ok.setOnClickListener {
            if(name_validation && id_validation && pw_validation && pw_confirm_validation && id_check) {
                postJoinResponse()
            }
            else {
                toast("잘못된 형식의 정보가 있습니다.")
            }
        }
    }

    var name_validation = false
    var id_validation = false
    var pw_validation = false
    var pw_confirm_validation = false
    var id_check = false

    private fun checkValidation() {

        et_join_name.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                name_validation = et_join_name.text.toString().trim().isNotEmpty()
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        et_join_id.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                id_validation = et_join_id.text.toString().trim().isNotEmpty()

                getCheckIdResponse()
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

        })


        // 패스워드: 8-20자, 문자+숫자
        et_join_pw.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if(Pattern.matches("^(?=.*?[a-zA-Z])(?=.*?[0-9]).{8,20}\$", et_join_pw.text.toString()))
                {
                    iv_join_pw_validation.visibility = View.VISIBLE
                    pw_validation = true
                }
                else {
                    iv_join_pw_validation.visibility = View.GONE
                    pw_validation = false
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        // 패스워드 확인
        et_join_confilmpw.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if (et_join_confilmpw.text.toString().length != 0) {
                    if (et_join_pw.text.toString() == et_join_confilmpw.text.toString()) {
                        iv_join_confilmpw_validation.visibility = View.VISIBLE
                        pw_confirm_validation = true
                    } else {
                        pw_confirm_validation = false
                    }
                } else if (et_join_confilmpw.text.toString().length == 0) {
                    pw_confirm_validation = false
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

    }

    private fun getCheckIdResponse(){

        val inputId = et_join_id.text.toString().trim()

        val getCheckIdResponse = networkService.getCheckIdResponse(inputId)

        getCheckIdResponse.enqueue(object : Callback<Void> {
            override fun onFailure(call: Call<Void>, t: Throwable) {
                Log.d("Error CheckActivity : ", t.message.toString())
                toast(t.message.toString())
            }

            override fun onResponse(call: Call<Void>, response: Response<Void>) {

                response.let {
                    when (it.code()) {
                        200 -> {
                            id_check = true
                            toast("중복된 아이디가 없습니다.")
                        }
                        400 -> {
                            toast("400")
                        }
                        500 -> {
                            toast("중복된 아이디가 있습니다.")
                        }
                        else -> {
                            toast("else")

                        }
                    }
                }
            }
        })
    }

    private fun postJoinResponse() {
        val inputId = et_join_id.text.toString().trim()
        val inputName = et_join_name.text.toString().trim()
        val inputPw = et_join_pw.text.toString().trim()

        var jsonObject = JSONObject()
        jsonObject.put("id", inputId)
        jsonObject.put("name", inputName)
        jsonObject.put("pw", inputPw)

        val gsonObject = JsonParser().parse(jsonObject.toString()) as JsonObject

        val postJoinResponse = networkService.postJoinResponse(gsonObject)

        postJoinResponse.enqueue(object : Callback<Void> {
            override fun onFailure(call: Call<Void>, t: Throwable) {
                Log.d("Error JoinActivity : ", t.message.toString())
                toast(t.message.toString())
            }

            override fun onResponse(call: Call<Void>, response: Response<Void>) {

                response.let {
                    when (it.code()) {
                        200 -> {
                            startActivity(Intent(this@JoinActivity, LoginActivity::class.java))
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
