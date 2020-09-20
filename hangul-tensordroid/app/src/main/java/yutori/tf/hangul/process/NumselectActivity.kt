package yutori.tf.hangul.process;

import android.content.Intent
import android.content.pm.ActivityInfo
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import kotlinx.android.synthetic.main.activity_numselect.*
import yutori.tf.hangul.R
import yutori.tf.hangul.db.SharedPreferenceController
import yutori.tf.hangul.exam.CheckActivity
import yutori.tf.hangul.exam.ExamActivity
import yutori.tf.hangul.practice.PracticeActivity

class NumselectActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_numselect)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
        SharedPreferenceController.instance?.setPrefData("number_of_problem",1)
        setClickListener()
    }

    @Override
    override fun onBackPressed() {
        val intent = Intent(applicationContext, HomeActivity::class.java)
        startActivity(intent)
    }

    private fun setClickListener() {
        btn_numselect_back.setOnClickListener {
            val intent = Intent(applicationContext, HomeActivity::class.java)
            startActivity(intent)
        }

        btn_numselect_1.setOnClickListener {
            SharedPreferenceController.instance?.setPrefData("numTypes", "NUM1")
            moveActivity()
        }

        btn_numselect_2.setOnClickListener {
            SharedPreferenceController.instance?.setPrefData("numTypes", "NUM2")
            moveActivity()
        }

        btn_numselect_3.setOnClickListener {
            SharedPreferenceController.instance?.setPrefData("numTypes", "NUM3")
            moveActivity()
        }

        btn_numselect_4.setOnClickListener {
            SharedPreferenceController.instance?.setPrefData("numTypes", "NUM4")
            moveActivity()
        }

        btn_numselect_5.setOnClickListener {
            SharedPreferenceController.instance?.setPrefData("numTypes", "NUM5")
            moveActivity()
        }
    }

    private fun moveActivity() {
        val homeType = SharedPreferenceController.instance?.getPrefStringData("homeTypes")
        if (homeType.equals("PRACTICE")) {
            val intent = Intent(applicationContext, PracticeActivity::class.java)
            startActivity(intent)
        } else if (homeType.equals("EXAM")) {
            val intent = Intent(applicationContext, ExamActivity::class.java)
            startActivity(intent)
        }

    }
}