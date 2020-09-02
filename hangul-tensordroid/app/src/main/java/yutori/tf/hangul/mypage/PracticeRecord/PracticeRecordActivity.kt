package yutori.tf.hangul.mypage.PracticeRecord

import android.content.Intent
import android.content.pm.ActivityInfo
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import kotlinx.android.synthetic.main.activity_practice_record.*
import org.jetbrains.anko.toast
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import yutori.tf.hangul.R
import yutori.tf.hangul.data.GetPracticeRecordResponse
import yutori.tf.hangul.db.SharedPreferenceController
import yutori.tf.hangul.mypage.MypageActivity
import yutori.tf.hangul.network.ApplicationController
import yutori.tf.hangul.network.NetworkService

class PracticeRecordActivity : AppCompatActivity() {

    lateinit var networkService: NetworkService

    lateinit var practiceRecordRecyclerViewAdapter: PracticeRecordRecyclerViewAdapter

    val dataList: ArrayList<GetPracticeRecordResponse> by lazy {
        ArrayList<GetPracticeRecordResponse>()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_practice_record)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
        init()
    }

    private fun init() {
        networkService = ApplicationController.instance.networkService
        setRecyclerView()
        getPracticeRecordResponse()

        btn_practice_record_back.setOnClickListener {
            val intent = Intent(applicationContext, MypageActivity::class.java)
            startActivity(intent)
        }
    }

    private fun setRecyclerView() {
        practiceRecordRecyclerViewAdapter = PracticeRecordRecyclerViewAdapter(this, dataList)
        rv_practice_record.adapter = practiceRecordRecyclerViewAdapter
        rv_practice_record.layoutManager = LinearLayoutManager(this)
    }

    private fun getPracticeRecordResponse() {
        val authorization = SharedPreferenceController.instance?.getPrefStringData("authorization")
        val userId = SharedPreferenceController.instance?.getPrefLongData("userId")

        val getPracticeRecordResponse = networkService.getPracticeRecordResponse(authorization, userId)

        getPracticeRecordResponse.enqueue(object : Callback<ArrayList<GetPracticeRecordResponse>> {
            override fun onFailure(call: Call<ArrayList<GetPracticeRecordResponse>>, t: Throwable) {
                Log.i("Error Mypage : ", t.message.toString())
                toast(t.message.toString())
            }

            override fun onResponse(call: Call<ArrayList<GetPracticeRecordResponse>>, response: Response<ArrayList<GetPracticeRecordResponse>>) {
                response.let {
                    when (it.code()) {
                        200 -> {
                            toast("200")
                            val dataList: ArrayList<GetPracticeRecordResponse> = response.body()!!
                            if (dataList.size > 0) {
                                val position = practiceRecordRecyclerViewAdapter.itemCount
                                practiceRecordRecyclerViewAdapter.dataList.addAll(dataList)
                                practiceRecordRecyclerViewAdapter.notifyItemInserted(position)
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
