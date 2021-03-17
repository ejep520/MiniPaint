package com.jepster.minipaint

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.view.MotionEvent
import android.view.View
import android.view.ViewConfiguration

import androidx.annotation.ColorInt
import androidx.core.content.res.ResourcesCompat

private const val STROKE_WIDTH: Float = 12f

class MyCanvasView(context: Context) : View(context) {

    private val drawing: Path = Path()
    private val curPath: Path = Path()

    @ColorInt
    private val backgroundColor: Int = ResourcesCompat.getColor(resources, R.color.colorBackground, null)
    @ColorInt
    private val paintColor: Int = ResourcesCompat.getColor(resources, R.color.colorPaint, null)

    private val paint = Paint().apply{
        color = paintColor
        isAntiAlias = true
        isDither = true
        style = Paint.Style.STROKE
        strokeJoin = Paint.Join.ROUND
        strokeCap = Paint.Cap.ROUND
        strokeWidth = STROKE_WIDTH
    }

    private lateinit var frame: Rect

    private var motionTouchEventX: Float = 0f
    private var motionTouchEventY: Float = 0f
    private var currentX: Float = 0f
    private var currentY: Float = 0f

    private val touchTolerance: Int = ViewConfiguration.get(context).scaledTouchSlop

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        val inset = 40
        frame = Rect(inset, inset, width - inset, height - inset)
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        canvas!!.drawColor(backgroundColor)
        canvas.drawPath(drawing, paint)
        canvas.drawPath(curPath, paint)
        canvas.drawRect(frame, paint)
    }

    private fun touchStart() {
        curPath.reset()
        curPath.moveTo(motionTouchEventX, motionTouchEventY)
        currentX = motionTouchEventX
        currentY = motionTouchEventY
    }

    private fun touchMove() {
        val dx = kotlin.math.abs(motionTouchEventX - currentX)
        val dy = kotlin.math.abs(motionTouchEventY - currentY)
        if (dx >= touchTolerance || dy >= touchTolerance) {
            curPath.quadTo(currentX, currentY, (motionTouchEventX + currentX)/2, (motionTouchEventY + currentY)/2)
            currentX = motionTouchEventX
            currentY = motionTouchEventY
        }
        invalidate()
    }

    private fun touchUp() {
        drawing.addPath(curPath)
        curPath.reset()
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        motionTouchEventX = event.x
        motionTouchEventY = event.y

        when (event.action) {
            MotionEvent.ACTION_DOWN -> touchStart()
            MotionEvent.ACTION_MOVE -> touchMove()
            MotionEvent.ACTION_UP -> touchUp()
        }
        return true
    }
}
