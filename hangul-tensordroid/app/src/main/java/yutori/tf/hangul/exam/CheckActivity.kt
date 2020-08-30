package yutori.tf.hangul.exam

import android.content.pm.ActivityInfo
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import yutori.tf.hangul.R
import yutori.tf.hangul.network.ApplicationController
import yutori.tf.hangul.network.NetworkService

class CheckActivity : AppCompatActivity() {

    lateinit var networkService: NetworkService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_check)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
        init()
    }

    private fun init() {
        networkService = ApplicationController.instance.networkService
        setClickListener()
        getCheckResponse()
    }

    private fun setClickListener() {

    }

    private fun getCheckResponse() {

    }


}
