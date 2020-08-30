package yutori.tf.hangul.exam

import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Build
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.MotionEvent
import kotlinx.android.synthetic.main.activity_exam.*
import org.jetbrains.anko.toast
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import yutori.tf.hangul.R
import yutori.tf.hangul.data.GetSentenceResponse
import yutori.tf.hangul.db.SharedPreferenceController
import yutori.tf.hangul.hangul.HangulClassifier
import yutori.tf.hangul.hangul.PaintView
import yutori.tf.hangul.network.ApplicationController
import yutori.tf.hangul.network.NetworkService
import java.util.*

class ExamActivity : AppCompatActivity() {

    lateinit var networkService: NetworkService

    private var classifier: HangulClassifier? = null
    private var currentTopLabels = arrayOfNulls<String>(5)

    lateinit var tts: TextToSpeech
    lateinit var speakText: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_exam)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
        init()
    }

    private fun init() {
        networkService = ApplicationController.instance.networkService
        setClickListener()
        playToSpeech()
        initDraw()
        loadModel()
        getSentenceResponse()
    }

    private fun setClickListener() {
        btn_write_next.setOnClickListener {
            val number = SharedPreferenceController.instance?.getPrefIntegerData("number_of_problem")
            if (number == 10) {
                val intent = Intent(applicationContext, CheckActivity::class.java)
                startActivity(intent)
            } else {
                SharedPreferenceController.instance?.setPrefData("number_of_problem", number!!.plus(1))
                val intent = Intent(applicationContext, ExamActivity::class.java)
                startActivity(intent)
            }
        }

        btn_write_prev.setOnClickListener {
            val number = SharedPreferenceController.instance?.getPrefIntegerData("number_of_problem")
            if (number != 1) {
                SharedPreferenceController.instance?.setPrefData("number_of_problem", number!!.minus(1))
                val intent = Intent(applicationContext, ExamActivity::class.java)
                startActivity(intent)
            }
        }

        btn_write_clear.setOnClickListener {
            clear()
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

        btn_write_speak.setOnClickListener {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                tts.speak(speakText, TextToSpeech.QUEUE_FLUSH, null, null)
            } else {
                tts.speak(speakText, TextToSpeech.QUEUE_FLUSH, null)
            }
        }
    }

    private fun initDraw() {
        paint_write_1.setDrawText(tv_write_drawHere1)
        paint_write_2.setDrawText(tv_write_drawHere2)
        paint_write_3.setDrawText(tv_write_drawHere3)
        paint_write_4.setDrawText(tv_write_drawHere4)
        paint_write_5.setDrawText(tv_write_drawHere5)
        paint_write_6.setDrawText(tv_write_drawHere6)
        paint_write_7.setDrawText(tv_write_drawHere7)
        paint_write_8.setDrawText(tv_write_drawHere8)
        paint_write_9.setDrawText(tv_write_drawHere9)
        paint_write_10.setDrawText(tv_write_drawHere10)
        paint_write_11.setDrawText(tv_write_drawHere11)
        paint_write_12.setDrawText(tv_write_drawHere12)
        paint_write_13.setDrawText(tv_write_drawHere13)
        paint_write_14.setDrawText(tv_write_drawHere14)
        paint_write_15.setDrawText(tv_write_drawHere15)
        paint_write_16.setDrawText(tv_write_drawHere16)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (event.action == MotionEvent.ACTION_DOWN) {
            val classify_timer = Timer()
            val timer_task: TimerTask = object : TimerTask() {
                override fun run() {
                    classify()
                }
            }
            classify_timer.schedule(timer_task, 1500)
        }
        return true
    }


    private fun clear() {
        paint_write_1.reset()
        paint_write_2.reset()
        paint_write_3.reset()
        paint_write_4.reset()
        paint_write_5.reset()
        paint_write_6.reset()
        paint_write_7.reset()
        paint_write_8.reset()
        paint_write_9.reset()
        paint_write_10.reset()
        paint_write_11.reset()
        paint_write_12.reset()
        paint_write_13.reset()
        paint_write_14.reset()
        paint_write_15.reset()
        paint_write_16.reset()

        paint_write_1.invalidate()
        paint_write_2.invalidate()
        paint_write_3.invalidate()
        paint_write_4.invalidate()
        paint_write_5.invalidate()
        paint_write_6.invalidate()
        paint_write_7.invalidate()
        paint_write_8.invalidate()
        paint_write_9.invalidate()
        paint_write_10.invalidate()
        paint_write_11.invalidate()
        paint_write_12.invalidate()
        paint_write_13.invalidate()
        paint_write_14.invalidate()
        paint_write_15.invalidate()
        paint_write_16.invalidate()

        tv_write_result.text = ""
        paint_write_1.touch = true
        paint_write_2.touch = true
        paint_write_3.touch = true
        paint_write_4.touch = true
        paint_write_5.touch = true
        paint_write_6.touch = true
        paint_write_7.touch = true
        paint_write_8.touch = true
        paint_write_9.touch = true
        paint_write_10.touch = true
        paint_write_11.touch = true
        paint_write_12.touch = true
        paint_write_13.touch = true
        paint_write_14.touch = true
        paint_write_15.touch = true
        paint_write_16.touch = true
    }

    override fun onResume() {
        paint_write_1.onResume()
        paint_write_2.onResume()
        paint_write_3.onResume()
        paint_write_4.onResume()
        paint_write_5.onResume()
        paint_write_6.onResume()
        paint_write_7.onResume()
        paint_write_8.onResume()
        paint_write_9.onResume()
        paint_write_10.onResume()
        paint_write_11.onResume()
        paint_write_12.onResume()
        paint_write_13.onResume()
        paint_write_14.onResume()
        paint_write_15.onResume()
        paint_write_16.onResume()
        super.onResume()
    }

    override fun onPause() {
        paint_write_1.onPause()
        paint_write_2.onPause()
        paint_write_3.onPause()
        paint_write_4.onPause()
        paint_write_5.onPause()
        paint_write_6.onPause()
        paint_write_7.onPause()
        paint_write_8.onPause()
        paint_write_9.onPause()
        paint_write_10.onPause()
        paint_write_11.onPause()
        paint_write_12.onPause()
        paint_write_13.onPause()
        paint_write_14.onPause()
        paint_write_15.onPause()
        paint_write_16.onPause()
        super.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        if (tts != null) {
            tts!!.stop()
            tts!!.shutdown()
//            tts = null
        }
    }

    private fun classify() {
        tv_write_result.text = ""
        val pixels : FloatArray = paint_write_1.pixelData
        currentTopLabels = classifier!!.classify(pixels)
        if (paint_write_1.touch) {
            tv_write_result.append(" ")
        } else {
            tv_write_result.append(currentTopLabels[0])
        }
        val pixels2 : FloatArray = paint_write_2.pixelData
        currentTopLabels = classifier!!.classify(pixels2)
        if (paint_write_2.touch) {
            tv_write_result.append(" ")
        } else {
            tv_write_result.append(currentTopLabels[0])
        }
        val pixels3 : FloatArray = paint_write_3.pixelData
        currentTopLabels = classifier!!.classify(pixels3)
        if (paint_write_3.touch) {
            tv_write_result.append(" ")
        } else {
            tv_write_result.append(currentTopLabels[0])
        }
        val pixels4 : FloatArray = paint_write_4.pixelData
        currentTopLabels = classifier!!.classify(pixels4)
        if (paint_write_4.touch) {
            tv_write_result.append(" ")
        } else {
            tv_write_result.append(currentTopLabels[0])
        }
        val pixels5 : FloatArray = paint_write_5.pixelData
        currentTopLabels = classifier!!.classify(pixels5)
        if (paint_write_5.touch) {
            tv_write_result.append(" ")
        } else {
            tv_write_result.append(currentTopLabels[0])
        }
        val pixels6 : FloatArray = paint_write_6.pixelData
        currentTopLabels = classifier!!.classify(pixels6)
        if (paint_write_6.touch) {
            tv_write_result.append(" ")
        } else {
            tv_write_result.append(currentTopLabels[0])
        }
        val pixels7 : FloatArray = paint_write_7.pixelData
        currentTopLabels = classifier!!.classify(pixels7)
        if (paint_write_7.touch) {
            tv_write_result.append(" ")
        } else {
            tv_write_result.append(currentTopLabels[0])
        }
        val pixels8 : FloatArray = paint_write_8.pixelData
        currentTopLabels = classifier!!.classify(pixels8)
        if (paint_write_8.touch) {
            tv_write_result.append(" ")
        } else {
            tv_write_result.append(currentTopLabels[0])
        }
        val pixels9 : FloatArray = paint_write_9.pixelData
        currentTopLabels = classifier!!.classify(pixels9)
        if (paint_write_9.touch) {
            tv_write_result.append(" ")
        } else {
            tv_write_result.append(currentTopLabels[0])
        }
        val pixels10 : FloatArray = paint_write_10.pixelData
        currentTopLabels = classifier!!.classify(pixels10)
        if (paint_write_10.touch) {
            tv_write_result.append(" ")
        } else {
            tv_write_result.append(currentTopLabels[0])
        }
        val pixels11 : FloatArray = paint_write_11.pixelData
        currentTopLabels = classifier!!.classify(pixels11)
        if (paint_write_11.touch) {
            tv_write_result.append(" ")
        } else {
            tv_write_result.append(currentTopLabels[0])
        }
        val pixels12 : FloatArray = paint_write_12.pixelData
        currentTopLabels = classifier!!.classify(pixels12)
        if (paint_write_12.touch) {
            tv_write_result.append(" ")
        } else {
            tv_write_result.append(currentTopLabels[0])
        }
        val pixels13 : FloatArray = paint_write_13.pixelData
        currentTopLabels = classifier!!.classify(pixels13)
        if (paint_write_13.touch) {
            tv_write_result.append(" ")
        } else {
            tv_write_result.append(currentTopLabels[0])
        }
        val pixels14 : FloatArray = paint_write_14.pixelData
        currentTopLabels = classifier!!.classify(pixels14)
        if (paint_write_14.touch) {
            tv_write_result.append(" ")
        } else {
            tv_write_result.append(currentTopLabels[0])
        }
        val pixels15 : FloatArray = paint_write_15.pixelData
        currentTopLabels = classifier!!.classify(pixels15)
        if (paint_write_15.touch) {
            tv_write_result.append(" ")
        } else {
            tv_write_result.append(currentTopLabels[0])
        }
        val pixels16 : FloatArray = paint_write_16.pixelData
        currentTopLabels = classifier!!.classify(pixels16)
        if (paint_write_16.touch) {
            tv_write_result.append(" ")
        } else {
            tv_write_result.append(currentTopLabels[0])
        }
    }

    private fun getSentenceResponse() {
        val number = SharedPreferenceController.instance?.getPrefIntegerData("number_of_problem")

        val sentenceTypes = SharedPreferenceController.instance?.getPrefStringData("sentenceTypes")
        val levelTypes = SharedPreferenceController.instance?.getPrefStringData("levelTypes")
        val numTypes = SharedPreferenceController.instance?.getPrefStringData("numTypes")

        val getSentenceResponse = networkService.getSentenceResponse(sentenceTypes, levelTypes, numTypes)

        getSentenceResponse.enqueue(object : Callback<List<GetSentenceResponse>> {
            override fun onFailure(call: Call<List<GetSentenceResponse>>, t: Throwable) {
                Log.i("Error Practice : ", t.message.toString())
                toast(t.message.toString())
            }

            override fun onResponse(call: Call<List<GetSentenceResponse>>, response: Response<List<GetSentenceResponse>>) {
                response.let {
                    when (it.code()) {
                        200 -> {
                            toast("200")
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

    private fun loadModel() {
        Thread {
            classifier = try {
                HangulClassifier.create(assets,
                        MODEL_FILE, LABEL_FILE, PaintView.FEED_DIMENSION,
                        "input", "keep_prob", "output")
            } catch (e: Exception) {
                throw RuntimeException("Error loading pre-trained model.", e)
            }
        }.start()
    }

    companion object {
        private const val LABEL_FILE = "256-common-hangul.txt"
        private const val MODEL_FILE = "optimized_hangul_tensorflow.pb"
    }
}