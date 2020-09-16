package yutori.tf.hangul.practice

import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Build
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import kotlinx.android.synthetic.main.activity_practice.*
import org.jetbrains.anko.toast
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import yutori.tf.hangul.R
import yutori.tf.hangul.data.GetSentenceResponse
import yutori.tf.hangul.db.SharedPreferenceController
import yutori.tf.hangul.network.ApplicationController
import yutori.tf.hangul.network.NetworkService
import yutori.tf.hangul.process.NumselectActivity
import java.util.*


class PracticeActivity : AppCompatActivity() {

    lateinit var networkService: NetworkService

    lateinit var tts: TextToSpeech
    lateinit var speakText: String

    val authorization = SharedPreferenceController.instance?.getPrefStringData("authorization")
    val sentenceTypes = SharedPreferenceController.instance?.getPrefStringData("sentenceTypes")
    val levelTypes = SharedPreferenceController.instance?.getPrefStringData("levelTypes")
    val numTypes = SharedPreferenceController.instance?.getPrefStringData("numTypes")

    private val FINISH_INTERVAL_TIME: Long = 2000
    private var backPressedTime: Long = 0

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
        initDraw()
        getSentenceResponse()
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
            toast("한번 더 누르면 종료됩니다.")
        }
    }

    private fun setClickListener() {
        btn_practice_next.setOnClickListener {
            val number = SharedPreferenceController.instance?.getPrefIntegerData("number_of_problem")
            if (number == 10) {
                postPracticeResponse()
                val intent = Intent(applicationContext, PracticeEndActivity::class.java)
                startActivity(intent)
            } else {
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

        btn_practice_clear.setOnClickListener {
            clear()
        }

        btn_practice_out.setOnClickListener {
            val intent = Intent(applicationContext, NumselectActivity::class.java)
            startActivity(intent)
        }

    }

    private fun playToSpeech() {
        tts = TextToSpeech(this, TextToSpeech.OnInitListener {
            @Override
            fun onInit(status: Int) {
                if (status != TextToSpeech.ERROR) {
                    tts.setLanguage(Locale.KOREAN)
                    tts.setPitch(0.8f)
                    tts.setSpeechRate(0.6f)
                }
            }
        })

        btn_practice_speak.setOnClickListener {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                tts.speak(speakText, TextToSpeech.QUEUE_FLUSH, null, null)
            } else {
                tts.speak(speakText, TextToSpeech.QUEUE_FLUSH, null)
            }
        }
    }

    private fun initDraw() {
        paint_practice_1.setDrawText(tv_practice_drawHere1)
        paint_practice_2.setDrawText(tv_practice_drawHere2)
        paint_practice_3.setDrawText(tv_practice_drawHere3)
        paint_practice_4.setDrawText(tv_practice_drawHere4)
        paint_practice_5.setDrawText(tv_practice_drawHere5)
        paint_practice_6.setDrawText(tv_practice_drawHere6)
        paint_practice_7.setDrawText(tv_practice_drawHere7)
        paint_practice_8.setDrawText(tv_practice_drawHere8)
        paint_practice_9.setDrawText(tv_practice_drawHere9)
        paint_practice_10.setDrawText(tv_practice_drawHere10)
        paint_practice_11.setDrawText(tv_practice_drawHere11)
        paint_practice_12.setDrawText(tv_practice_drawHere12)
        paint_practice_13.setDrawText(tv_practice_drawHere13)
        paint_practice_14.setDrawText(tv_practice_drawHere14)
        paint_practice_15.setDrawText(tv_practice_drawHere15)
        paint_practice_16.setDrawText(tv_practice_drawHere16)
    }

    private fun clear() {
        paint_practice_1.reset()
        paint_practice_2.reset()
        paint_practice_3.reset()
        paint_practice_4.reset()
        paint_practice_5.reset()
        paint_practice_6.reset()
        paint_practice_7.reset()
        paint_practice_8.reset()
        paint_practice_9.reset()
        paint_practice_10.reset()
        paint_practice_11.reset()
        paint_practice_12.reset()
        paint_practice_13.reset()
        paint_practice_14.reset()
        paint_practice_15.reset()
        paint_practice_16.reset()
        paint_practice_1.invalidate()
        paint_practice_2.invalidate()
        paint_practice_3.invalidate()
        paint_practice_4.invalidate()
        paint_practice_5.invalidate()
        paint_practice_6.invalidate()
        paint_practice_7.invalidate()
        paint_practice_8.invalidate()
        paint_practice_9.invalidate()
        paint_practice_10.invalidate()
        paint_practice_11.invalidate()
        paint_practice_12.invalidate()
        paint_practice_13.invalidate()
        paint_practice_14.invalidate()
        paint_practice_15.invalidate()
        paint_practice_16.invalidate()
    }

    override fun onResume() {
        paint_practice_1.onResume()
        paint_practice_2.onResume()
        paint_practice_3.onResume()
        paint_practice_4.onResume()
        paint_practice_5.onResume()
        paint_practice_6.onResume()
        paint_practice_7.onResume()
        paint_practice_8.onResume()
        paint_practice_9.onResume()
        paint_practice_10.onResume()
        paint_practice_11.onResume()
        paint_practice_12.onResume()
        paint_practice_13.onResume()
        paint_practice_14.onResume()
        paint_practice_15.onResume()
        paint_practice_16.onResume()
        super.onResume()
    }

    override fun onPause() {
        paint_practice_1.onPause()
        paint_practice_2.onPause()
        paint_practice_3.onPause()
        paint_practice_4.onPause()
        paint_practice_5.onPause()
        paint_practice_6.onPause()
        paint_practice_7.onPause()
        paint_practice_8.onPause()
        paint_practice_9.onPause()
        paint_practice_10.onPause()
        paint_practice_11.onPause()
        paint_practice_12.onPause()
        paint_practice_13.onPause()
        paint_practice_14.onPause()
        paint_practice_15.onPause()
        paint_practice_16.onPause()
        super.onPause()
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

        val getSentenceResponse = networkService.getSentenceResponse(authorization, sentenceTypes, levelTypes, numTypes)

        getSentenceResponse.enqueue(object : Callback<List<GetSentenceResponse>> {
            override fun onFailure(call: Call<List<GetSentenceResponse>>, t: Throwable) {
                Log.i("Error Practice : ", t.message.toString())
                toast(t.message.toString())
            }

            override fun onResponse(call: Call<List<GetSentenceResponse>>, response: Response<List<GetSentenceResponse>>) {
                response.let {
                    when (it.code()) {
                        200 -> {
                            tv_practice_sentence.setText(number.toString() + ". " + response.body()?.get(number!!.minus(1))?.sentence.toString())
                            speakText = response.body()?.get(number!!.minus(1))?.sentence.toString()
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

    private fun postPracticeResponse() {
        val userId = SharedPreferenceController.instance?.getPrefLongData("userId")

        val jsonObject = JSONObject()
        jsonObject.put("sentenceTypes", sentenceTypes)
        jsonObject.put("levelTypes", levelTypes)
        jsonObject.put("numTypes", numTypes)
        jsonObject.put("userId", userId)

        val gsonObject = JsonParser().parse(jsonObject.toString()) as JsonObject
        val postPracticeResponse = networkService.postPracticeResponse(authorization, gsonObject)

        postPracticeResponse.enqueue(object : Callback<Void> {
            override fun onFailure(call: Call<Void>, t: Throwable) {
                Log.i("Error PracticeRecord : ", t.message.toString())
                toast(t.message.toString())
            }

            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                response.let {
                    when (it.code()) {
                        200 -> {
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
