package com.bytedance.jstu.homework

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View


class CircleView(context: Context, attrs: AttributeSet) : View(context, attrs){

    var angle = 0F
        get() = field
        set(value) {
            field = value
        }

    private val radius = 81F
    val paint = Paint()
    init {

        paint.isAntiAlias=true
        paint.style = Paint.Style.STROKE
        paint.color = Color.RED
        paint.strokeWidth = 13F
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val xPos = canvas.width / 2
        val yPos = canvas.height / 2
        val rect = RectF(xPos - radius,yPos - radius,xPos + radius,yPos + radius)
        canvas.drawArc(rect, 0F, angle, false, paint)
    }



}