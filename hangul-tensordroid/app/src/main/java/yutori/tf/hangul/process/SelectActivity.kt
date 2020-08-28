package yutori.tf.hangul.process;

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

    private fun setClickListener() {
        btn_select_sentence.setOnClickListener {
            SharedPreferenceController.instance?.setPrefData("sentenceTypes", "SENTENCE")
            LevelDialog(this).show()
        }
//        btn_select_music.setOnClickListener {
//            SharedPreferenceController.instance?.setPrefData("sentenceTypes", "SING")
//            LevelDialog(this).show()
//        }
    }
}