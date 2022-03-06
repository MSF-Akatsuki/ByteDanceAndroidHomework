package com.bytedance.jstu.homework

import android.content.Intent
import android.content.res.XmlResourceParser
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.cardview.widget.CardView
import androidx.core.view.marginRight


class MainAdapter: RecyclerView.Adapter<MainAdapter.MainItemViewHolder>()
{

    /**
     *  Pair<String, String>::first : Title of the problem
     *  Pair<String, String>::second : Brief introduction of the problem
     */
    private val contentList : MutableList<ProblemContents.ProblemAbstract> = mutableListOf()




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

    fun setContentList(list: List<ProblemContents.ProblemAbstract>) {
        contentList.clear()
        contentList.addAll(list)
        notifyDataSetChanged()
    }

    class MainItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val ll_item = view.findViewById<androidx.cardview.widget.CardView>(R.id.main_item_card)
        private val tv_title = view.findViewById<TextView>(R.id.main_Title_tv)
        private val tv_tags = view.findViewById<LinearLayout>(R.id.main_tags_lin)
        private val tv_info = view.findViewById<TextView>(R.id.main_info_tv)

        private val view = view
        init {
            ll_item.setOnClickListener {
                val intentProblemDisplayActivity = Intent(view.context,ProblemDisplayActivity::class.java)

                intentProblemDisplayActivity.putExtra("problemId", adapterPosition+1)
                view.context.startActivity(intentProblemDisplayActivity)
            }
        }

        fun bind(content: ProblemContents.ProblemAbstract) {
            tv_title.text = content.title

            tv_tags.removeAllViews();

            val inflater : LayoutInflater = LayoutInflater.from(view.context);
            for (item in content.tags) {
                val cardView : CardView =
                    inflater.inflate(R.layout.tag_view, tv_tags, false) as CardView
                cardView.findViewById<TextView>(R.id.tag_tv).text=item
                tv_tags.addView(cardView)
            }
            tv_info.text = "通过人数: " + content.numPassed.toString()
        }


    }


}
