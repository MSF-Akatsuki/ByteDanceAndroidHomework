package com.bytedance.jstu.homework

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.SystemClock
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.ActivityResultRegistry
import androidx.activity.result.ActivityResultRegistryOwner
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import com.bytedance.jstu.homework.databinding.FragmentVideoSelectorBinding
import java.util.concurrent.ScheduledExecutorService

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [VideoSelectorFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class VideoSelectorFragment : Fragment() {
    // TODO: Rename and change types of parameters

    private val sharedViewModel : VideoViewModel by activityViewModels()
    private var _binding:FragmentVideoSelectorBinding?=null
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

        _binding= FragmentVideoSelectorBinding.inflate(inflater,container,false)
        return binding.root
    }


    val getContent = registerForActivityResult(ActivityResultContracts.GetContent()) {  uri: Uri?->
        Log.println(Log.INFO,"get_content",uri.toString())
        sharedViewModel.videoUri.value = uri
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnPickVideo.setOnClickListener {
            getContent.launch("video/*")
        }


    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment VideoSelectorFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            VideoSelectorFragment().apply {
                arguments = Bundle().apply {
                }
            }
    }


}
