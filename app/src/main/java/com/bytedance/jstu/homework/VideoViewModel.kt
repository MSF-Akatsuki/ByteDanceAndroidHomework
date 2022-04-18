package com.bytedance.jstu.homework

import android.media.MediaPlayer
import android.net.Uri
import android.text.Editable
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class VideoViewModel : ViewModel() {


    public val videoUri: MutableLiveData<Uri?> by lazy {
        MutableLiveData<Uri?>()
    }
    public var videoCurrentPosition: Int=0
    public val videoDuration: Int = 1

}
