package yutori.tf.hangul.process;

import android.content.Intent
import android.content.pm.ActivityInfo
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import kotlinx.android.synthetic.main.activity_select.*
import yutori.tf.hangul.R
import yutori.tf.hangul.db.SharedPreferenceController

class SelectActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_select)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
        setClickListener()
    }

    @Override
    override fun onBackPressed() {
        val intent = Intent(applicationContext, HomeActivity::class.java)
        startActivity(intent)
    }

    private fun setClickListener() {
        btn_select_sentence.setOnClickListener {
            SharedPreferenceController.instance?.setPrefData("sentenceTypes", "SENTENCE")
            LevelDialog(this).show()
        }

//        btn_select_music.setOnClickListener {
//            SharedPreferenceController.instance?.setPrefData("sentenceTypes", "SING")
//            LevelDialog(this).show()
//        }

        btn_select_back.setOnClickListener{
            val intent = Intent(applicationContext, HomeActivity::class.java)
            startActivity(intent)
        }
    }
}