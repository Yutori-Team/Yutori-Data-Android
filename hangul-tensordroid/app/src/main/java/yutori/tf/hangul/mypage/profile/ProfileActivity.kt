package yutori.tf.hangul.mypage.profile

import kotlinx.android.synthetic.main.activity_profile.*
import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import org.jetbrains.anko.toast
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import yutori.tf.hangul.R
import yutori.tf.hangul.SplashActivity
import yutori.tf.hangul.data.GetProfileResponse
import yutori.tf.hangul.db.SharedPreferenceController
import yutori.tf.hangul.login.LoginActivity
import yutori.tf.hangul.mypage.MypageActivity
import yutori.tf.hangul.network.ApplicationController
import yutori.tf.hangul.network.NetworkService
import java.util.regex.Pattern

class ProfileActivity : AppCompatActivity(){
    lateinit var networkService: NetworkService

    val authorization = SharedPreferenceController.instance?.getPrefStringData("authorization")
    val userId = SharedPreferenceController.instance?.getPrefLongData("userId")


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
        init()
    }

    private fun init() {
        networkService = ApplicationController.instance.networkService
        setClickListener()
        checkValidation()
        getProfileResponse()
    }

    private fun setClickListener() {
        btn_profile_back.setOnClickListener {
            val intent = Intent(applicationContext, MypageActivity::class.java)
            startActivity(intent)
        }

        btn_profile_logout.setOnClickListener {
            SharedPreferenceController.instance!!.removeAllData(this)
            val intent = Intent(this, SplashActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        }
        btn_profile_delete.setOnClickListener{
            deleteMemberResponse()
            SharedPreferenceController.instance!!.removeAllData(this)
            val intent = Intent(this, SplashActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        }

        btn_profile_modify.setOnClickListener{
            if(name_validation && pw_validation && pw_confirm_validation) {
                putProfileResponse()
            }
            else {
                toast("잘못된 형식의 정보가 있습니다.")
            }

        }
    }

    var name_validation = false
    var pw_validation = false
    var pw_confirm_validation = false

    private fun checkValidation() {

        et_profile_name.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                name_validation = et_profile_name.text.toString().trim().isNotEmpty()
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        // 패스워드: 8-20자, 문자+숫자
        et_profile_pw.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if(Pattern.matches("^(?=.*?[a-zA-Z])(?=.*?[0-9]).{8,20}\$", et_profile_pw.text.toString()))
                {
                    iv_profile_pw_validation.visibility = View.VISIBLE
                    pw_validation = true
                }
                else {
                    iv_profile_pw_validation.visibility = View.GONE
                    pw_validation = false
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        // 패스워드 확인
        et_profile_compilepw.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if (et_profile_compilepw.text.toString().length != 0) {
                    if (et_profile_pw.text.toString() == et_profile_compilepw.text.toString()) {
                        iv_profile_compilepw_validation.visibility = View.VISIBLE
                        pw_confirm_validation = true
                    } else {
                        pw_confirm_validation = false
                    }
                } else if (et_profile_compilepw.text.toString().length == 0) {
                    pw_confirm_validation = false
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

    }

    private fun getProfileResponse() {

        val getProfileResponse = networkService.getProfileResponse(authorization, userId)

        getProfileResponse.enqueue(object : Callback<GetProfileResponse> {
            override fun onFailure(call: Call<GetProfileResponse>, t: Throwable) {
                Log.i("Error Mypage : ", t.message.toString())
                toast(t.message.toString())
            }

            override fun onResponse(call: Call<GetProfileResponse>, response: Response<GetProfileResponse>) {
                response.let {
                    when (it.code()) {
                        200 -> {
                            tv_profile_id.setText(response.body()?.userId)
                            et_profile_name.setText(response.body()?.name)
                        }
                        400 -> {
                            toast("400")
                        }
                        404 -> {
                            toast("404")
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

    private fun deleteMemberResponse(){

        val deleteMemberResponse = networkService.deleteMemberResponse(authorization,userId)

        deleteMemberResponse.enqueue(object : Callback<Void> {
            override fun onFailure(call: Call<Void>, t: Throwable) {
                Log.d("Error DeleteActivity : ", t.message.toString())
                toast(t.message.toString())
            }

            override fun onResponse(call: Call<Void>, response: Response<Void>) {


                response.let {
                    when (it.code()) {
                        200 -> {
                            startActivity(Intent(this@ProfileActivity, LoginActivity::class.java))
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

    private fun putProfileResponse(){

        val inputName = et_profile_name.text.toString().trim()
        val inputPw = et_profile_pw.text.toString().trim()

        var jsonObject = JSONObject()
        jsonObject.put("id", userId)
        jsonObject.put("name", inputName)
        jsonObject.put("userPw", inputPw)

        val gsonObject = JsonParser().parse(jsonObject.toString()) as JsonObject

        val putProfileResponse = networkService.putProfileResponse(authorization, gsonObject)

        putProfileResponse.enqueue(object : Callback<Void> {
            override fun onFailure(call: Call<Void>, t: Throwable) {
                Log.i("Error ProfileActivity: ", t.message.toString())
                toast(t.message.toString())
            }

            override fun onResponse(call: Call<Void>, response: Response<Void>) {

                response.let {
                    when (it.code()) {
                        200 -> {
                            startActivity(Intent(this@ProfileActivity, ProfileActivity::class.java))
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