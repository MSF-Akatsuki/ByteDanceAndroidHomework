package com.bytedance.jstu.homework

import android.content.res.Configuration
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.SystemClock
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.MediaController
import androidx.core.view.isInvisible
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import com.bytedance.jstu.homework.databinding.FragmentImageItemBinding
import com.bytedance.jstu.homework.databinding.FragmentVideoPlayerBinding

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [VideoPlayerFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class VideoPlayerFragment : Fragment() {
    // TODO: Rename and change types of parameters
    val sharedViewModel: VideoViewModel by activityViewModels()

    private var handler: Handler?=null
    private var _binding: FragmentVideoPlayerBinding ? =null
    protected val binding get() = _binding!!


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        handler = Handler(Looper.getMainLooper())
        handler?.postDelayed(iTicker,20)

        _binding = FragmentVideoPlayerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val videoUriObserver = Observer<Uri?> { newVideoUri ->
            binding.viewVideo.setVideoURI(newVideoUri)
            newVideoUri?.let {
                binding.viewVideo.start()
            }
        }
        sharedViewModel.videoUri.observe(viewLifecycleOwner,videoUriObserver)

        binding.viewVideo.setOnClickListener {
            if (binding.videoToolbar.visibility==View.VISIBLE){
                binding.videoToolbar.visibility = View.GONE
            } else {
                binding.videoToolbar.visibility = View.VISIBLE
            }
        }

        binding.btnMediaPlay.setOnClickListener {
            if (!binding.viewVideo.isPlaying){
                val cur = binding.viewVideo.currentPosition
                binding.viewVideo.start()
                binding.viewVideo.seekTo(cur)
            }

            Log.println(Log.INFO,"player","${binding.viewVideo.isPlaying}")
        }

        binding.btnMediaReplay.setOnClickListener {
            binding.viewVideo.resume()
            Log.println(Log.INFO,"player","${binding.viewVideo.isPlaying}")
        }

        binding.btnMediaStop.setOnClickListener {
            if (binding.viewVideo.isPlaying)
                binding.viewVideo.pause()
            Log.println(Log.INFO,"player","${binding.viewVideo.isPlaying}")
        }

    }

    override fun onPause() {
        sharedViewModel.videoCurrentPosition=binding.viewVideo.currentPosition
        super.onPause()
    }

    override fun onResume() {
        super.onResume()
        binding.viewVideo.seekTo(sharedViewModel.videoCurrentPosition)
    }

    private val iTicker : Runnable = object : Runnable{
        override fun run() {
            handler?.removeCallbacksAndMessages(this)
            if (binding.viewVideo.isPlaying) {
                val duration = binding.viewVideo.duration
                val currentPosition = binding.viewVideo.currentPosition
                val tmp: Float = if (duration!=0) currentPosition.toFloat()/duration else 0F
                Log.println(Log.INFO,"iticker", "$duration $currentPosition")
                binding.progressBar.progress = if (tmp<0 || tmp>1) 0F else tmp
                binding.progressBar.invalidate()
            }

            handler?.postDelayed(this, 800)
        }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment VideoPlayerFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance() =
            VideoPlayerFragment().apply {
                arguments = Bundle().apply {
                }
            }
    }
}