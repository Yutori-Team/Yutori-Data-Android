package yutori.tf.hangul.mypage

import android.content.Intent
import android.content.pm.ActivityInfo
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import kotlinx.android.synthetic.main.activity_mypage.*
import org.jetbrains.anko.toast
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import yutori.tf.hangul.R
import yutori.tf.hangul.data.GetProfileResponse
import yutori.tf.hangul.db.SharedPreferenceController
import yutori.tf.hangul.mypage.ExamRecord.ExamRecordActivity
import yutori.tf.hangul.mypage.PracticeRecord.PracticeRecordActivity
import yutori.tf.hangul.network.ApplicationController
import yutori.tf.hangul.network.NetworkService
import yutori.tf.hangul.process.HomeActivity

class MypageActivity : AppCompatActivity() {

    lateinit var networkService: NetworkService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mypage)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
        init()
    }

    private fun init() {
        networkService = ApplicationController.instance.networkService
        setClickListener()
        getProfileResponse()
    }

    private fun setClickListener() {
        btn_mypage_back.setOnClickListener {
            val intent = Intent(applicationContext, HomeActivity::class.java)
            startActivity(intent)
        }
        btn_mypage_exam.setOnClickListener {
            val intent = Intent(applicationContext, ExamRecordActivity::class.java)
            startActivity(intent)
        }
        btn_mypage_practice.setOnClickListener {
            val intent = Intent(applicationContext, PracticeRecordActivity::class.java)
            startActivity(intent)
        }

//        btn_mypage_profile.setOnClickListener {
//
//        }
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
                            tv_mypage_name.setText(response.body()?.name)
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


}
