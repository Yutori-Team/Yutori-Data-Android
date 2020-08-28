package yutori.tf.hangul.practice

import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Build
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.support.v7.app.AppCompatActivity
import android.util.Log
import kotlinx.android.synthetic.main.activity_practice.*
import org.jetbrains.anko.toast
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import yutori.tf.hangul.R
import yutori.tf.hangul.data.GetSentenceResponse
import yutori.tf.hangul.db.SharedPreferenceController
import yutori.tf.hangul.network.ApplicationController
import yutori.tf.hangul.network.NetworkService
import java.util.*

class PracticeActivity : AppCompatActivity() {

    lateinit var networkService: NetworkService

    lateinit var tts : TextToSpeech
    lateinit var text: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_practice)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
        init()
    }

    private fun init() {
        networkService = ApplicationController.instance.networkService
        setClickListener()
        playToSpeech()
        getSentenceResponse()
    }

    private fun setClickListener() {
        btn_practice_next.setOnClickListener {
            val number = SharedPreferenceController.instance?.getPrefIntegerData("number_of_problem")
            if(number == 10) {
                val intent = Intent(applicationContext, PracticeEndActivity::class.java)
                startActivity(intent)
            }else {
                SharedPreferenceController.instance?.setPrefData("number_of_problem", number!!.plus(1))
                val intent = Intent(applicationContext, PracticeActivity::class.java)
                startActivity(intent)
            }
        }
        btn_practice_prev.setOnClickListener {
            val number = SharedPreferenceController.instance?.getPrefIntegerData("number_of_problem")
            if (number != 1) {
                SharedPreferenceController.instance?.setPrefData("number_of_problem", number!!.minus(1))
                val intent = Intent(applicationContext, PracticeActivity::class.java)
                startActivity(intent)
            }
        }

    }

    private fun playToSpeech() {
        tts = TextToSpeech(this, TextToSpeech.OnInitListener {
            @Override
            fun onInit(status: Int) {
                if (status != TextToSpeech.ERROR) {
                    tts.setLanguage(Locale.KOREAN)
                }
            }
        })

        btn_practice_speak.setOnClickListener {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                tts.speak(text, TextToSpeech.QUEUE_FLUSH, null, null)
            } else {
                tts.speak(text, TextToSpeech.QUEUE_FLUSH, null)
            }
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        if (tts != null) {
            tts.stop()
            tts.shutdown()
//            tts = null
        }
    }

    private fun getSentenceResponse() {
        val number = SharedPreferenceController.instance?.getPrefIntegerData("number_of_problem")

        val sentenceTypes = SharedPreferenceController.instance?.getPrefStringData("sentenceTypes")
        val levelTypes = SharedPreferenceController.instance?.getPrefStringData("levelTypes")
        val numTypes = SharedPreferenceController.instance?.getPrefStringData("numTypes")

        val getSentenceResponse = networkService.getSentenceResponse(sentenceTypes, levelTypes, numTypes)

        getSentenceResponse.enqueue(object : Callback<GetSentenceResponse> {
            override fun onFailure(call: Call<GetSentenceResponse>, t: Throwable) {
                Log.i("Error Practice : ", t.message.toString())
                toast(t.message.toString())
            }

            override fun onResponse(call: Call<GetSentenceResponse>, response: Response<GetSentenceResponse>) {
                response.let {
                    when (it.code()) {
                        200 -> {
                            toast("200")
                            tv_practice_sentence.setText(response.body()?.resSentenceDtoList?.get(number!!.minus(1))?.sentence.toString())
                            text = response.body()?.resSentenceDtoList?.get(number!!.minus(1))?.sentence.toString()
                        }
                        400 -> {
                            toast("400")
                        }
                        404 -> {
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
