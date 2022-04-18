package com.bytedance.jstu.homework

import android.content.Intent
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.activity.viewModels
import androidx.fragment.app.activityViewModels

class MainActivity : AppCompatActivity() {

    val sharedViewModel: VideoViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        bindActivity<PictureDispActivity>(findViewById(R.id.btn_picture))
        bindActivity<VideoPlayActivity>(findViewById(R.id.btn_video))
    }

    private inline fun <reified T> bindActivity(btn: Button){
        btn.setOnClickListener {
            val intent = Intent(this, T::class.java)
            startActivity(intent)
        }
    }

    override fun onDestroy() {
        super.onDestroy()

    }
}