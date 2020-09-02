package yutori.tf.hangul.practice

import android.content.Intent
import android.content.pm.ActivityInfo
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_practice_end.*
import yutori.tf.hangul.R
import yutori.tf.hangul.process.HomeActivity
import yutori.tf.hangul.process.NumselectActivity

class PracticeEndActivity : AppCompatActivity() {

    private val FINISH_INTERVAL_TIME: Long = 2000
    private var backPressedTime: Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_practice_end)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
        setClickListener()
    }

    @Override
    override fun onBackPressed() {
        val tempTime = System.currentTimeMillis()
        val intervalTime = tempTime - backPressedTime

        if (0 <= intervalTime && FINISH_INTERVAL_TIME >= intervalTime) {
            val intent = Intent(applicationContext, NumselectActivity::class.java)
            startActivity(intent)
        } else {
            backPressedTime = tempTime
            Toast.makeText(applicationContext, "한번 더 누르면 종료됩니다.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun setClickListener() {
        btn_practice_end_home.setOnClickListener {
            val intent = Intent(applicationContext, HomeActivity::class.java)
            startActivity(intent)
        }
    }

}

