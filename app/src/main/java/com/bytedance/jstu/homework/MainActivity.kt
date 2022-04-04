package com.bytedance.jstu.homework

import android.annotation.SuppressLint
import android.content.Context
import android.os.*
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import android.view.MotionEvent
import android.view.View
import android.widget.TextClock
import java.lang.Math.atan
import java.lang.Math.pow
import kotlin.math.PI
import kotlin.math.atan2
import kotlin.math.sqrt

class MainActivity : AppCompatActivity() {
    enum class HoldTarget(val target:Int) {
        HOUR(1),MINUTE(2),SECOND(3),NONE(0)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val clockTicksView = findViewById<ClockTicksView>(R.id.clock_ticks_view)

        clockTicksView.setOnTouchListener(object : View.OnTouchListener {
            var startTimeMillis = 0
            var isHolding = false
            val handler = Handler(Looper.getMainLooper())
            val vibrator : Vibrator = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                (getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as VibratorManager).defaultVibrator
            } else {
                getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
            }



            var holdTarget : HoldTarget = HoldTarget.NONE

            override fun onTouch(view: View, motionEvent: MotionEvent): Boolean {

                val centerX = clockTicksView.width/2
                val centerY = clockTicksView.height/2

                if (motionEvent.action==MotionEvent.ACTION_DOWN) {
                    Log.println(Log.DEBUG,"act_up","ACTION_DOWN")
                    view.performClick()
                    val difX = motionEvent.getX(0) - centerX
                    val difY = motionEvent.getY(0) - centerY
                    val dist = sqrt(difX * difX + difY * difY)
                    val radius = clockTicksView.getRadius()

                    if (dist > radius * 0.65) {
                        holdTarget = HoldTarget.SECOND
                    } else if (dist > radius * 0.3) {
                        holdTarget = HoldTarget.MINUTE
                    } else {
                        holdTarget = HoldTarget.HOUR
                    }

                    handler.postDelayed({
                        isHolding = true

                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                            vibrator.vibrate(VibrationEffect.createOneShot(60,VibrationEffect.DEFAULT_AMPLITUDE))
                        } else {
                            vibrator.vibrate(60)
                        }
                    },500)
                } else if (motionEvent.action==MotionEvent.ACTION_MOVE) {
                    if (!isHolding) {
                        Log.println(Log.DEBUG,"act_mv","Halt due to false isHolding ")
                        return false
                    }
                    val x = motionEvent.getX(0) - centerX
                    val y = motionEvent.getY(0) - centerY

                    /* Pointer 与竖直中轴线在中心的夹角 */
                    val theta = atan2(x ,-y)
                    val otpt = theta / PI * 180F

                    clockTicksView.setTimeByAngle(theta, holdTarget)

                    Log.println(Log.DEBUG,"act_mv", "Move to ($x,$y)")
                } else if (motionEvent.action==MotionEvent.ACTION_UP) {
                    Log.println(Log.DEBUG,"act_up","ACTION_UP")
                    holdTarget = HoldTarget.NONE
                    handler.removeCallbacksAndMessages(null)
                    isHolding = false
                }
                return true
            }
        })
    }



}