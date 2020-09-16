package yutori.tf.hangul.mypage.profile

import kotlinx.android.synthetic.main.activity_profile.*
import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import org.jetbrains.anko.toast
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import yutori.tf.hangul.R
import yutori.tf.hangul.SplashActivity
import yutori.tf.hangul.data.GetProfileResponse
import yutori.tf.hangul.db.SharedPreferenceController
import yutori.tf.hangul.login.LoginActivity
import yutori.tf.hangul.network.ApplicationController
import yutori.tf.hangul.network.NetworkService

class ProfileActivity : AppCompatActivity(){
    lateinit var networkService: NetworkService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
        init()
    }

    private fun init() {
        networkService = ApplicationController.instance.networkService
        setClickListener()
        getProfileResponse()
    }

    private fun setClickListener() {
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
    }

    private fun getProfileResponse() {
        val authorization = SharedPreferenceController.instance?.getPrefStringData("authorization")
        val userId = SharedPreferenceController.instance?.getPrefLongData("userId")

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
        val authorization = SharedPreferenceController.instance?.getPrefStringData("authorization")
        val userId = SharedPreferenceController.instance?.getPrefLongData("userId")

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
}