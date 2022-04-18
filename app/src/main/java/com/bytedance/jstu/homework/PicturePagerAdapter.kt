package com.bytedance.jstu.homework

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class PicturePagerAdapter(activity: AppCompatActivity) : FragmentStateAdapter(activity) {
    override fun getItemCount(): Int {
        return ImageItemFragment.srcSize()
    }
    override fun createFragment(position: Int): Fragment {
        return ImageItemFragment.newInstance(position)
    }
}