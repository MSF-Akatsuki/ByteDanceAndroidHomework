package com.bytedance.jstu.homework

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.TextView
import androidx.cardview.widget.CardView

class ProblemDisplayActivity : AppCompatActivity() {



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_problem_display)

        val contentId = intent.extras?.get("problemId") as Int
        val content = MainActivity.problemContents.getContentById(contentId)
        val tvTitle = findViewById<TextView>(R.id.problem_title_tv)
        val tvInfo = findViewById<TextView>(R.id.content_info_tv)

        tvTitle.text = content.title
        tvInfo.text = "通过人数: " + content.numPassed.toString()

        val tv_tags = findViewById<LinearLayout>(R.id.contents_tags_view)
        val inflater : LayoutInflater = LayoutInflater.from(this);
        for (item in content.tags) {
            val cardView : CardView =
                inflater.inflate(R.layout.tag_view, tv_tags, false) as CardView
            cardView.findViewById<TextView>(R.id.tag_tv).text=item
            tv_tags.addView(cardView)
        }

        val cv_sections = findViewById<LinearLayout>(R.id.problem_display_sv)
        for (item in content.contents) {
            val cardView : CardView =
                inflater.inflate(R.layout.problem_section_view, cv_sections, false) as CardView
            cardView.findViewById<TextView>(R.id.section_title_tv).text=item.first
            cardView.findViewById<TextView>(R.id.section_contents_tv).text=item.second
            cv_sections.addView(cardView)

        }

    }

    fun bindWithProblemID(id: Int) {
        //val contents = getProblemContentsById(id)



    }

    private fun getProblemContentsById(id: Int): Any {
        return 0
    }


}