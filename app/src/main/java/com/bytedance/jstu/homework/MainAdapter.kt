package com.bytedance.jstu.homework

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView


class MainAdapter: RecyclerView.Adapter<MainAdapter.MainItemViewHolder>()
{

    /**
     *  Pair<String, String>::first : Title of the problem
     *  Pair<String, String>::second : Brief introduction of the problem
     */
    private  val contentList = mutableListOf< Pair<String,String> >()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainItemViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.main_item_layout, parent, false );
        return MainItemViewHolder(v)
    }

    override fun onBindViewHolder(holder: MainItemViewHolder, position: Int) {
        holder.bind(contentList[position])
    }

    override fun getItemCount(): Int {
        return contentList.size
    }

    fun setContentList(list: List<Pair<String,String>>) {
        contentList.clear()
        contentList.addAll(list)
        notifyDataSetChanged()
    }

    class MainItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val ll_item = view.findViewById<LinearLayout>(R.id.main_item_layout)
        // private val tv_icon = view.findViewById<TextView>(R.id.main_icon_tv)
        private val tv_title = view.findViewById<TextView>(R.id.main_Title_tv)
        private val tv_intro = view.findViewById<TextView>(R.id.main_intro_tv)

        init {
            ll_item.setOnClickListener {
                tv_title.text = "nEGATIVE"
            }
        }

        fun bind(content: Pair<String,String>) {
            tv_title.text = content.first
            tv_intro.text = content.second
        }


    }


}