package com.bytedance.jstu.homework

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.icu.text.DateFormat
import android.os.Handler
import android.os.Looper
import android.os.SystemClock
import android.provider.Settings
import android.util.AttributeSet
import android.util.Log
import android.view.View
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

class ClockTicksView(context: Context, attrs: AttributeSet) : View(context, attrs) {

    var now : Long = 0
    var latestSystemMillis : Long = 0

    fun onTimeChanged() {
        val currentSystemMillis = System.currentTimeMillis()
        now = currentSystemMillis - latestSystemMillis + now
        latestSystemMillis = currentSystemMillis

        val secs = now / 1000
        val mins = secs / 60
        val hrs = mins / 60

        seconds = secs % 60
        minutes = mins % 60
        hours = hrs % 24
        Log.println(Log.INFO,null,"Tick" + hours + ":" + minutes + ":" + seconds)
        // invalidate()
    }

    /* Reference : android.widget.TextClock */
    private val mTicker : Runnable = object : Runnable{
        override fun run(){
            handler.removeCallbacksAndMessages(this)
            val now = SystemClock.uptimeMillis()
            val next = now + (1000 - now % 1000)
            onTimeChanged()
            handler.postAtTime(this, next)
        }
    }

    private val iTicker : Runnable = object : Runnable{
        override fun run() {
            handler.removeCallbacksAndMessages(this)
            val now = SystemClock.uptimeMillis()
            val next = now + (20 - now % 20)
            invalidate()
            handler.postAtTime(this, next)
        }
    }

    private val paintSec = Paint()
    private val paintMin = Paint()
    private val paintHour = Paint()
    private val bounds = RectF()


    var seconds:Long = 7
    var minutes:Long = 53
    var hours:Long = 13

    init {
        paintSec.isAntiAlias = true
        paintSec.style = Paint.Style.STROKE
        paintSec.color = Color.parseColor("#007F3F")
        paintSec.strokeWidth = 2F

        paintMin.isAntiAlias = true
        paintMin.style = Paint.Style.STROKE
        paintMin.color = Color.parseColor("#7F3F00")
        paintMin.strokeWidth = 4F

        paintHour.isAntiAlias = true
        paintHour.style = Paint.Style.STROKE
        paintHour.color = Color.parseColor("#3F007F")
        paintHour.strokeWidth = 7F

        now = System.currentTimeMillis()
        latestSystemMillis = now
        onTimeChanged()
        val handler : Handler = Handler(Looper.getMainLooper())
        handler.postDelayed(mTicker,200)
        handler.postDelayed(iTicker,200)
    }

    override fun onDraw(canvas: Canvas?) {

        val radius : Float = if (width<height) width / 2F else height / 2F
        val secAngle : Float = seconds * 6F
        val minAngle : Float = minutes * 6F + seconds * 0.1F
        val hourAngle : Float = hours *  30F + minutes * 0.5F



        super.onDraw(canvas)
        if (canvas!=null){

            canvas.drawLine(
                width/2F,
                height/2F,
                width/2F + radius * 0.9F * sin(secAngle/180F * PI.toFloat()),
                height/2F - radius * 0.9F * cos(secAngle/180F * PI.toFloat()),
                paintSec)

            canvas.drawLine(
                width/2F,
                height/2F,
                width/2F + radius * 0.65F * sin(minAngle/180F * PI.toFloat()),
                height/2F - radius * 0.65F * cos(minAngle/180F * PI.toFloat()),
                paintMin)

            canvas.drawLine(
                width/2F,
                height/2F,
                width/2F + radius * 0.3F * sin(hourAngle/180F * PI.toFloat()),
                height/2F - radius * 0.3F * cos(hourAngle/180F * PI.toFloat()),
                paintHour)
        }
    }

    override fun performClick(): Boolean {
        return super.performClick()
    }

    fun getRadius():Float=if (width<height) width / 2F else height / 2F

    fun setHourByAngle(angle : Float) {
        val secs = now / 1000
        val mins = secs / 60
        val hrs = mins / 60

        seconds = secs % 60
        minutes = mins % 60
        hours = hrs % 24

        hours = (angle / 30 + 0.5).toLong()

        now = hours *3600*1000 + mins * 60 * 1000 + secs * 1000
    }

    fun setTimeByAngle(angle : Float, holdTarget: MainActivity.HoldTarget) {
        val secs = now / 1000
        val mins = secs / 60
        val hrs = mins / 60

        seconds = (secs % 60).toLong()
        minutes = (mins % 60).toLong()
        hours = (hrs % 24).toLong()

        when (holdTarget) {
            MainActivity.HoldTarget.SECOND -> seconds = ((angle / PI + 2) * 180 / 6 + 0.5).toLong() % 60
            MainActivity.HoldTarget.MINUTE -> minutes = ((angle / PI + 2) * 180 / 6 + 0.5).toLong() % 60
            MainActivity.HoldTarget.HOUR -> hours = ((angle / PI + 2) * 180 / 30 + 0.5).toLong() % 12
        }

        now = hours *3600*1000 + minutes * 60 * 1000 + seconds * 1000
    }
}