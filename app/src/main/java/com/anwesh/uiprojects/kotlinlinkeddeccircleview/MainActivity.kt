package com.anwesh.uiprojects.kotlinlinkeddeccircleview

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager
import android.widget.Toast
import com.anwesh.uiprojects.linkeddeccircleview.LinkedDecCircleView

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val view = LinkedDecCircleView.create(this)
        fullScreen()
        view.setOnCompletionListener {
            Toast.makeText(this, "animation number ${it + 1} completed", Toast.LENGTH_SHORT).show()
        }
    }
}

fun MainActivity.fullScreen() {
    supportActionBar?.hide()
    window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
}