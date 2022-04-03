package com.bytedance.jstu.homework

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View

class ClockBackgroundView(context:Context, attrs: AttributeSet) : View(context, attrs) {

    private val paint = Paint()
    private val bounds = RectF()

    init {
        paint.isAntiAlias = true
        paint.style = Paint.Style.STROKE
        paint.color = Color.parseColor("#007F3F")
        paint.strokeWidth = 30F
    }




    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        if (canvas!=null){

            if (width < height) {
                bounds.set(
                    paint.strokeWidth / 2F,
                    (height - width + paint.strokeWidth ) / 2F,
                    width.toFloat() - paint.strokeWidth / 2F,
                    (height + width - paint.strokeWidth) / 2F
                )

            }
            else {
                bounds.set(
                    (width - height + paint.strokeWidth) / 2F,
                    paint.strokeWidth / 2F,
                    (width + height - paint.strokeWidth) / 2F,
                    height.toFloat() - paint.strokeWidth / 2F
                )
            }

            for (i in 1..60) {
                if (i % 5 != 0) {
                    canvas.drawArc(bounds, 6 * i - 0.4F, 0.8F, false, paint)
                } else {
                    canvas.drawArc(bounds, 6 * i - 0.8F, 1.6F, false, paint)
                }
            }

        }
    }



}