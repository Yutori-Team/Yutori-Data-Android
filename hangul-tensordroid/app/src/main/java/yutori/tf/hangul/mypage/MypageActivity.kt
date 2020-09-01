package yutori.tf.hangul.mypage

import android.content.Intent
import android.content.pm.ActivityInfo
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_mypage.*
import yutori.tf.hangul.R
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
//        getProfileResponse()
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

//    private fun getProfileResponse() {
//
//    }


}
