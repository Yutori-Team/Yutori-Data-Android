package yutori.tf.hangul.mypage.PracticeRecord

import android.content.Intent
import android.content.pm.ActivityInfo
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_practice_record.*
import yutori.tf.hangul.R
import yutori.tf.hangul.mypage.MypageActivity
import yutori.tf.hangul.network.ApplicationController
import yutori.tf.hangul.network.NetworkService

class PracticeRecordActivity : AppCompatActivity() {

    lateinit var networkService: NetworkService

    lateinit var practiceRecordRecyclerViewAdapter: PracticeRecordRecyclerViewAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_practice_record)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
        init()
    }

    private fun init() {
        networkService = ApplicationController.instance.networkService
        setRecyclerView()

        btn_practice_record_back.setOnClickListener {
            val intent = Intent(applicationContext, MypageActivity::class.java)
            startActivity(intent)
        }
    }

    private fun setRecyclerView() {
        var dataList: ArrayList<PracticeRecordData> = ArrayList()
        dataList.add(PracticeRecordData("1", "1", "1", "1", "1"))
        dataList.add(PracticeRecordData("1", "1", "1", "1", "1"))
        dataList.add(PracticeRecordData("1", "1", "1", "1", "1"))
        dataList.add(PracticeRecordData("1", "1", "1", "1", "1"))

        practiceRecordRecyclerViewAdapter = PracticeRecordRecyclerViewAdapter(this, dataList)
        rv_practice_record.adapter = practiceRecordRecyclerViewAdapter
        rv_practice_record.layoutManager = LinearLayoutManager(this)

    }
}
