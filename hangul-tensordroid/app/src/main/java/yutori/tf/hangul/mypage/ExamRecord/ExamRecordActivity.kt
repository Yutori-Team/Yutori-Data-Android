package yutori.tf.hangul.mypage.ExamRecord

import android.content.Intent
import android.content.pm.ActivityInfo
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import kotlinx.android.synthetic.main.activity_exam_record.*
import org.jetbrains.anko.toast
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import yutori.tf.hangul.R
import yutori.tf.hangul.data.GetExamRecordResponse
import yutori.tf.hangul.db.SharedPreferenceController
import yutori.tf.hangul.mypage.MypageActivity
import yutori.tf.hangul.network.ApplicationController
import yutori.tf.hangul.network.NetworkService

class ExamRecordActivity : AppCompatActivity() {

    lateinit var networkService: NetworkService

    lateinit var examRecordRecyclerViewAdapter: ExamRecordRecyclerViewAdapter

    val dataList: ArrayList<GetExamRecordResponse> by lazy {
        ArrayList<GetExamRecordResponse>()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_exam_record)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
        init()
    }

    private fun init() {
        networkService = ApplicationController.instance.networkService
        setRecyclerView()
        getExamRecordResponse()

        btn_exam_record_back.setOnClickListener {
            val intent = Intent(applicationContext, MypageActivity::class.java)
            startActivity(intent)
        }
    }

    private fun setRecyclerView() {
        examRecordRecyclerViewAdapter = ExamRecordRecyclerViewAdapter(this, dataList)
        rv_exam_record.adapter = examRecordRecyclerViewAdapter
        rv_exam_record.layoutManager = LinearLayoutManager(this)
    }

    private fun getExamRecordResponse() {
        val authorization = SharedPreferenceController.instance?.getPrefStringData("authorization")
        val userId = SharedPreferenceController.instance?.getPrefLongData("userId")

        val getExamRecordResponse = networkService.getExamRecordResponse(authorization, userId)

        getExamRecordResponse.enqueue(object : Callback<ArrayList<GetExamRecordResponse>> {
            override fun onFailure(call: Call<ArrayList<GetExamRecordResponse>>, t: Throwable) {
                Log.i("Error Mypage : ", t.message.toString())
                toast(t.message.toString())
            }

            override fun onResponse(call: Call<ArrayList<GetExamRecordResponse>>, response: Response<ArrayList<GetExamRecordResponse>>) {
                response.let {
                    when (it.code()) {
                        200 -> {
                            val dataList: ArrayList<GetExamRecordResponse> = response.body()!!
                            if (dataList.size > 0) {
                                val position = examRecordRecyclerViewAdapter.itemCount
                                examRecordRecyclerViewAdapter.dataList.addAll(dataList)
                                examRecordRecyclerViewAdapter.notifyItemInserted(position)
                            } else {
                            }
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