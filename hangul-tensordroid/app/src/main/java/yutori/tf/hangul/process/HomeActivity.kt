package yutori.tf.hangul.process;

import android.content.Intent
import android.content.pm.ActivityInfo
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log

import kotlinx.android.synthetic.main.activity_home.*
import org.jetbrains.anko.toast
import yutori.tf.hangul.R
import yutori.tf.hangul.db.SharedPreferenceController
import yutori.tf.hangul.mypage.MypageActivity

class HomeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
        setClickListener()
    }

    @Override
    override fun onBackPressed() {
        val intent = Intent(applicationContext, HomeActivity::class.java)
        startActivity(intent)
    }

    private fun setClickListener() {
        btn_home_practice.setOnClickListener {
            SharedPreferenceController.instance?.setPrefData("homeTypes", "PRACTICE")
            SharedPreferenceController.instance?.setPrefData("sentenceTypes", "SENTENCE")
            LevelDialog(this).show()
//            val intent = Intent(applicationContext, SelectActivity::class.java)
//            startActivity(intent)
        }
        btn_home_exam.setOnClickListener {
            SharedPreferenceController.instance?.setPrefData("homeTypes", "EXAM")
            SharedPreferenceController.instance?.setPrefData("sentenceTypes", "SENTENCE")
            LevelDialog(this).show()
//            val intent = Intent(applicationContext, SelectActivity::class.java)
//            startActivity(intent)
        }
        btn_home_mypage.setOnClickListener {
            val intent = Intent(applicationContext, MypageActivity::class.java)
            startActivity(intent)
        }
    }

}