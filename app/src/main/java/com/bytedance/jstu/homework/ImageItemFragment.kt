package com.bytedance.jstu.homework

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.bytedance.jstu.homework.databinding.FragmentImageItemBinding

private const val ARG_POSITION = "position"

class ImageItemFragment : Fragment() {
    private var uri: String? = null
    private var _binding: FragmentImageItemBinding ? =null
    protected val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            uri = srcList[ it.getInt(ARG_POSITION) ]
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentImageItemBinding.inflate(inflater, container, false)
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Glide.with(this)
            .load(uri)
            .into(binding.imageViewItem)
    }

    companion object {
        val srcList:List<String> = listOf(
            "https://shuiyuan.sjtu.edu.cn/uploads/default/original/3X/5/2/52c6a5ed6816d1feb7b62ea427708de4dc346c3c.jpeg",
            "https://shuiyuan.sjtu.edu.cn/uploads/default/original/3X/1/7/178ead97e2b44fafe26b083ae907eca0613470c9.jpeg",
            "https://shuiyuan.sjtu.edu.cn/uploads/default/optimized/3X/d/4/d44ed747c0cd50175af62195b90f2b349f47be91_2_1380x1950.png",
            "https://shuiyuan.sjtu.edu.cn/uploads/default/original/3X/7/6/766d2e1066ff64aa2f83a16ee9ec1c5fab327fca.gif"
        )

        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(position: Int) =
            ImageItemFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_POSITION, position)
                }
            }

        fun srcSize():Int = srcList.size

    }
}