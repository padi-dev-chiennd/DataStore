package com.example.datastore

// FlagView.kt
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View

class FlagView(context: Context, attrs: AttributeSet) : View(context, attrs) {
    private val paint = Paint()
    private var drawFlag = false
    private var colorProgress = 0f

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        if (drawFlag) {
            // Vẽ background trắng
            paint.color = Color.WHITE
            canvas.drawRect(0f, 0f, width.toFloat(), height.toFloat(), paint)

            // Vẽ và tô màu lá cờ với màu sắc dần dần
            paint.color = Color.argb(255, (255 * colorProgress).toInt(), 0, 0)
            canvas.drawRect(0f, 0f, width.toFloat(), height * 2 / 3f, paint)

            paint.color = Color.argb(255, 255, (255 * colorProgress).toInt(), 0)
            canvas.drawRect(0f, height * 2 / 3f, width.toFloat(), height.toFloat(), paint)
        }
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        return when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                startColorAnimation()
                true
            }

            else -> super.onTouchEvent(event)
        }
    }

    private fun startColorAnimation() {
        val colorAnimator = ValueAnimator.ofFloat(0f, 1f)
        colorAnimator.duration = 10000 // Thời gian animation (2 giây)

        colorAnimator.addUpdateListener { animation ->
            colorProgress = animation.animatedValue as Float
            invalidate() // Yêu cầu vẽ lại FlagView 0
        }

        colorAnimator.start()
        drawFlag = true
    }
}
