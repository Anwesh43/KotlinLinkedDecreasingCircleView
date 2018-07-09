package com.anwesh.uiprojects.linkeddeccircleview

/**
 * Created by anweshmishra on 09/07/18.
 */

import android.content.Context
import android.graphics.Paint
import android.graphics.Canvas
import android.view.View
import android.view.MotionEvent

val DC_NODES : Int = 5

class LinkedDecCircleView (ctx : Context) : View(ctx) {

    private val paint : Paint = Paint(Paint.ANTI_ALIAS_FLAG)

    override fun onDraw(canvas : Canvas) {

    }

    override fun onTouchEvent(event : MotionEvent) : Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {

            }
        }
        return true
    }

    data class DCState(var scale : Float = 0f, var prevScale : Float = 0f, var dir : Float = 0f) {

        fun update(stopcb : (Float) -> Unit) {
            scale = prevScale + dir
            if (Math.abs(scale - prevScale) > 1) {
                scale = prevScale + dir
                dir = 0f
                prevScale = scale
                stopcb(prevScale)
            }
        }

        fun startUpdating(startcb : () -> Unit) {
            if (dir == 0f) {
                dir = 1 - 2 * prevScale
                startcb()
            }
        }
    }

    data class DSAnimator(var view : View, var animated : Boolean = false) {

        fun animate(cb : () -> Unit) {
            if (animated) {
                cb()
                try {
                    Thread.sleep(50)
                    view.invalidate()
                } catch(ex : Exception) {

                }
            }
        }

        fun start() {
            if (!animated) {
                animated = true
                view.postInvalidate()
            }
        }

        fun stop() {
            if (animated) {
                animated = false
            }
        }
    }

    data class DSNode(var i : Int, val state : DCState = DCState()) {

        var next : DSNode? = null

        var prev : DSNode? = null

        fun update(stopcb : (Int, Float) -> Unit) {
            state.update {
                stopcb(i, it)
            }
        }

        fun startUpdating(startcb : () -> Unit) {
            state.startUpdating(startcb)
        }

        fun draw(canvas : Canvas, paint : Paint) {
            val w : Float = canvas.width.toFloat()
            val h : Float = canvas.height.toFloat()
            val gap : Float = w / DC_NODES
            val r : Float = gap / (2 * DC_NODES)
            var factor : Int = 1
            if (i == DC_NODES - 1) {
                factor = 0
            }
            canvas.save()
            canvas.translate(i * gap + (i + 1)  * r * gap * state.scale, h/2)
            canvas.drawCircle(0f, 0f, (i + 1) * r + r * state.scale * factor, paint)
            canvas.restore()
            next?.draw(canvas, paint)
        }

        init {
            addNeighbor()
        }

        fun addNeighbor() {
            if (i < DC_NODES - 1) {
                next = DSNode(i + 1)
                next?.prev = this
            }
        }

        fun getNext(dir : Int, cb : () -> Unit) : DSNode {
            var curr : DSNode? = prev
            if (dir == 1) {
                curr = next
            }
            if (curr != null) {
                return curr
            }
            cb()
            return this
        }

    }

    data class LinkedDecCircle (var i : Int) {

        private var curr : DSNode = DSNode(0)

        private var dir : Int = 1

        fun draw(canvas : Canvas, paint : Paint) {
            curr.draw(canvas, paint)
        }

        fun update(stopcb : (Int, Float) -> Unit) {
            curr.update {j, scale ->
                curr = curr.getNext(dir) {
                    dir *= -1
                }
                stopcb(j, scale)
            }
        }

        fun startUpdating(startcb : () -> Unit) {
            curr.startUpdating(startcb)
        }
    }
}