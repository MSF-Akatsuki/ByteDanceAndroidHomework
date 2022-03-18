package com.bytedance.jstu.homework

import android.animation.AnimatorInflater
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.widget.TextView

class MainActivity : AppCompatActivity() {



    private var animationFlag = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val buttonIine = findViewById<TextView>(R.id.iine_btn)
        val buttonCoin = findViewById<TextView>(R.id.coin_btn)
        val buttonColl = findViewById<TextView>(R.id.coll_btn)
        val animator_iine = AnimatorInflater.loadAnimator(this, R.animator.iine_flow);

        animator_iine.setTarget(buttonIine)
        buttonIine.setOnLongClickListener {
            animationFlag = true
            animator_iine.start()
            true
        }

        buttonIine.setOnTouchListener( View.OnTouchListener{ v, event ->

            if(event.action == MotionEvent.ACTION_UP) {
                animator_iine.end()
                buttonIine.clearAnimation()
                true
            }

            false
        })

    }


}