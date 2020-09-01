package yutori.tf.hangul.mypage.ExamRecord

import android.content.Intent
import android.content.pm.ActivityInfo
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_exam_record.*
import yutori.tf.hangul.R
import yutori.tf.hangul.mypage.MypageActivity
import yutori.tf.hangul.network.ApplicationController
import yutori.tf.hangul.network.NetworkService

class ExamRecordActivity : AppCompatActivity() {

    lateinit var networkService: NetworkService

    lateinit var examRecordRecyclerViewAdapter: ExamRecordRecyclerViewAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_exam_record)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
        init()
    }

    private fun init() {
        networkService = ApplicationController.instance.networkService
        setRecyclerView()

        btn_exam_record_back.setOnClickListener {
            val intent = Intent(applicationContext, MypageActivity::class.java)
            startActivity(intent)
        }
    }

    private fun setRecyclerView() {
        var dataList: ArrayList<ExamRecordData> = ArrayList()
        dataList.add(ExamRecordData("1", "1", "1", "1", "1",10))
        dataList.add(ExamRecordData("1", "1", "1", "1", "1",10))
        dataList.add(ExamRecordData("1", "1", "1", "1", "1",10))
        dataList.add(ExamRecordData("1", "1", "1", "1", "1",10))

        examRecordRecyclerViewAdapter = ExamRecordRecyclerViewAdapter(this, dataList)
        rv_exam_record.adapter = examRecordRecyclerViewAdapter
        rv_exam_record.layoutManager = LinearLayoutManager(this)
    }
}
