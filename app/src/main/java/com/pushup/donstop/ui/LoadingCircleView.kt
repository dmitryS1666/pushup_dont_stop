package com.pushup.donstop.ui

import android.animation.ArgbEvaluator
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import android.view.animation.LinearInterpolator
import kotlin.math.cos
import kotlin.math.min
import kotlin.math.sin

class LoadingCircleView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private val activeDotColor = Color.parseColor("#FFD700")
    private val inactiveDotColor = Color.WHITE
    private val dotCount = 7
    private val dotRadius = 50f

    private var centerX = 0f
    private var centerY = 0f
    private var radius = 0f
    private var activeIndex = 0

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
    }

    private val colorAnimator = ValueAnimator.ofInt(0, dotCount).apply {
        duration = 1200L
        interpolator = LinearInterpolator()
        repeatCount = ValueAnimator.INFINITE
        addUpdateListener {
            activeIndex = it.animatedValue as Int
            invalidate()
        }
    }

    init {
        colorAnimator.start()
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        centerX = w / 2f
        centerY = h / 2f
        radius = (min(w, h) / 2.5f) - dotRadius
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        for (i in 0 until dotCount) {
            val angle = Math.toRadians((i * 360.0 / dotCount))
            val x = centerX + cos(angle) * radius
            val y = centerY + sin(angle) * radius

            val isActive = isDotActive(i)

            paint.color = if (isActive) activeDotColor else inactiveDotColor
            canvas.drawCircle(x.toFloat(), y.toFloat(), dotRadius, paint)
        }
    }

    private fun isDotActive(index: Int): Boolean {
        // Определяем, входит ли текущий index в активное "окно" из 3 шариков
        val indices = List(3) { (activeIndex + it) % dotCount }
        return index in indices
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        colorAnimator.cancel()
    }
}
