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

    fun onTimeChanged() {
        val now = System.currentTimeMillis()


        val secs = now / 1000
        val mins = secs / 60
        val hrs = mins / 60

        seconds = (secs % 60).toInt()
        minutes = (mins % 60).toInt()
        hours = (hrs % 24).toInt()
        Log.println(Log.INFO,null,"Tick" + hours + ":" + minutes + ":" + seconds)
        invalidate()
    }

    /* Reference : android.widget.TextClock */
    private val mTicker : Runnable = object : Runnable{
        override fun run(){
            handler.removeCallbacksAndMessages(null)
            val now = SystemClock.uptimeMillis()
            val next = now + (1000 - now % 1000)

            onTimeChanged()


            handler.postAtTime(this, next)
        }
    }

    private val paintSec = Paint()
    private val paintMin = Paint()
    private val paintHour = Paint()
    private val bounds = RectF()


    var seconds = 7
    var minutes = 53
    var hours = 13

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


        onTimeChanged()
        val handler : Handler = Handler(Looper.getMainLooper())
        handler.postDelayed(mTicker,200)
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




}