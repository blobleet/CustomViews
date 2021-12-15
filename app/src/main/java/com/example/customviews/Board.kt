package com.example.customviews

import android.content.Context
import android.graphics.*
import android.graphics.Paint.ANTI_ALIAS_FLAG
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View

class Board(context: Context, attrs: AttributeSet) : View(context, attrs) {

    private var mBoardSize: Int
    private var mCellWidth: Float = 0f
    private var mCellHeight: Float = 0f
    // This creates a 2D array initialized with 0s
    private var mBoardState: Array<Array<Int>>
    private val mEmptyPaint = Paint(ANTI_ALIAS_FLAG)
    private val mClickedPaint = Paint(ANTI_ALIAS_FLAG)


    private var mCellRect: RectF

    companion object{
        private const val TAG = "Board"
    }

    init {
        context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.Board,
            0,
            0
        ).apply {
            try {
                mBoardSize = getInt(R.styleable.Board_size, 0)
            } finally {
                recycle()
            }
        }

        mBoardState = Array(mBoardSize) { Array(mBoardSize) {0} }

        mEmptyPaint.style = Paint.Style.STROKE
        mEmptyPaint.color = Color.BLACK
        mEmptyPaint.strokeWidth = 5f

        mClickedPaint.style = Paint.Style.FILL_AND_STROKE
        mClickedPaint.color = Color.GRAY
        mClickedPaint.strokeWidth = 5f

        mCellRect = RectF()
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)

        mCellWidth = (w.toFloat() / mBoardSize)
        mCellHeight = (h.toFloat() / mBoardSize)
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        for (i in 0 until mBoardSize) {
            for (j in 0 until mBoardSize) {
                val left = mCellWidth * i
                val top = mCellHeight * j
                val right = left + mCellWidth
                val bottom = top + mCellHeight
                mCellRect.set(left, top, right, bottom)

                if (mBoardState[i][j] == 0) {
                    canvas?.drawRect(mCellRect, mEmptyPaint)
                } else if (mBoardState[i][j] == 1){
                    canvas?.drawRect(mCellRect, mEmptyPaint)
                    drawCross(canvas)
                }
            }
        }
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        val value = super.onTouchEvent(event)

        when(event?.action){
            MotionEvent.ACTION_DOWN -> {
                // Converts the tapped position to board coords
                val cellX = (event.x / mCellWidth).toInt()
                val cellY = (event.y / mCellHeight).toInt()
                mBoardState[cellX][cellY] = 1

                invalidate()

                return true
            }
        }

        return value
    }

    private fun drawCross(canvas: Canvas?){
        canvas?.drawLine(
            mCellRect.left + 15,
            mCellRect.top + 15,
            mCellRect.right - 15,
            mCellRect.bottom - 15,
            mEmptyPaint)
        canvas?.drawLine(
            mCellRect.left + 15,
            mCellRect.bottom - 15,
            mCellRect.right - 15,
            mCellRect.top + 15,
            mEmptyPaint
        )
    }
}