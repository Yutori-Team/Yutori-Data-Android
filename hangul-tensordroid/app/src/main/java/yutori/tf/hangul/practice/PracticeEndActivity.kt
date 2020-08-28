package yutori.tf.hangul.practice

import android.content.Intent
import android.content.pm.ActivityInfo
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_practice_end.*
import yutori.tf.hangul.R
import yutori.tf.hangul.process.HomeActivity

class PracticeEndActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_practice_end)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
        setClickListener()
    }

    private fun setClickListener() {
        btn_practice_end_home.setOnClickListener {
            val intent = Intent(applicationContext, HomeActivity::class.java)
            startActivity(intent)
        }
    }

}

