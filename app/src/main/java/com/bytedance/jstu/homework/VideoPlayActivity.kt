package com.bytedance.jstu.homework

import android.content.pm.ActivityInfo
import android.content.res.Configuration
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.fragment.app.FragmentContainerView
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import kotlinx.coroutines.MainScope

class VideoPlayActivity : AppCompatActivity() {

    private lateinit var fragmentVideoContainer: FragmentContainerView
    private val viewModel:VideoViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_video_play)
        fragmentVideoContainer = findViewById<FragmentContainerView>(R.id.fragment_video_container)
    }

    override fun onResume() {
        super.onResume()
        Log.println(Log.INFO,"on_resume",resources.configuration.orientation.toString())

        if (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            switchToLandscape()
        } else {
            switchToPortrait()
        }
    }

    //configuration

    override fun onConfigurationChanged(newConfig: Configuration) {
        Log.println(Log.INFO,"on_cc",newConfig.orientation.toString())
        super.onConfigurationChanged(newConfig)
        if (newConfig.orientation==Configuration.ORIENTATION_LANDSCAPE){
            switchToLandscape()
        } else if (newConfig.orientation==Configuration.ORIENTATION_PORTRAIT) {
            switchToPortrait()
        }
    }

    private fun switchToLandscape(){

        supportFragmentManager.commit {
            replace<LandscapeVideoPlayFragment>(R.id.fragment_video_container)
        }
        window.decorView.systemUiVisibility =
            View.SYSTEM_UI_FLAG_FULLSCREEN or
            View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY or
            View.SYSTEM_UI_FLAG_HIDE_NAVIGATION

        supportActionBar?.hide()
    }

    private fun switchToPortrait(){

        supportFragmentManager.commit {
            replace<PortraitVideoPlayFragment>(R.id.fragment_video_container)
        }
        window.decorView.systemUiVisibility = 0

        supportActionBar?.show()
    }


}