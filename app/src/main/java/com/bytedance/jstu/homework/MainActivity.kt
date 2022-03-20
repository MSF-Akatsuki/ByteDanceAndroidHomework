package com.bytedance.jstu.homework

import android.animation.AnimatorInflater
import android.animation.AnimatorSet
import android.animation.ValueAnimator
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.MotionEvent
import android.view.View
import android.view.animation.LinearInterpolator
import android.widget.TextView
import androidx.core.animation.addListener

class MainActivity : AppCompatActivity() {



    private var animationFlag = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        val buttonIine = findViewById<TextView>(R.id.iine_btn)
        val buttonCoin = findViewById<TextView>(R.id.coin_btn)
        val buttonColl = findViewById<TextView>(R.id.coll_btn)

        val circleCoin = findViewById<CircleView>(R.id.coin_circle)
        val circleColl = findViewById<CircleView>(R.id.coll_circle)
        val handler = Handler(Looper.getMainLooper())

        val animatorCircle = ValueAnimator.ofFloat(0F,360F)
        animatorCircle.setDuration(3000)
        animatorCircle.setInterpolator(LinearInterpolator())
        animatorCircle.addUpdateListener {
            val value = animatorCircle.getAnimatedValue() as Float
            circleCoin.angle=value
            circleColl.angle=value

            circleCoin.requestLayout()
            circleColl.requestLayout()
        }

        animatorCircle.addListener(
            onEnd = {
                circleColl.angle = 0F
                circleCoin.angle = 0F
                circleColl.requestLayout()
                circleCoin.requestLayout()
            }
        )

        val animatorIine = AnimatorInflater.loadAnimator(this, R.animator.iine_flow)
        val animatorAfterIine = AnimatorInflater.loadAnimator(this, R.animator.after_flow)
        val animatorAfterCoin = AnimatorInflater.loadAnimator(this, R.animator.after_flow)
        val animatorAfterColl = AnimatorInflater.loadAnimator(this, R.animator.after_flow)


        val setAnimatorIine = AnimatorSet()
        setAnimatorIine.play(animatorIine).before(animatorAfterIine)
        setAnimatorIine.setTarget(buttonIine)

        val setAnimatorCoin = AnimatorSet()
        setAnimatorCoin.play(animatorAfterCoin)
        setAnimatorCoin.setTarget(buttonCoin)

        val setAnimatorColl = AnimatorSet()
        setAnimatorColl.play(animatorAfterColl)
        setAnimatorColl.setTarget(buttonColl)

        buttonIine.setOnLongClickListener {
            animationFlag = true

            setAnimatorIine.start()
            animatorCircle.start()
            handler.postDelayed({
                buttonIine.setTextColor(Color.parseColor("#006FF0"))
                buttonCoin.setTextColor(Color.parseColor("#006FF0"))
                buttonColl.setTextColor(Color.parseColor("#006FF0"))

                setAnimatorColl.start()
                setAnimatorCoin.start()
                animationFlag = false
            },3000)
            true
        }

        buttonIine.setOnTouchListener( View.OnTouchListener{ v, event ->


            if(event.action == MotionEvent.ACTION_UP) {
                if(animationFlag) {
                    handler.removeCallbacksAndMessages(null)
                    setAnimatorIine.end()
                    animatorCircle.end()
                    buttonIine.clearAnimation()
                    animationFlag = false
                }

                true
            }

            false
        })

    }


}