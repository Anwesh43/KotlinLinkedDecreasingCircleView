package com.anwesh.uiprojects.kotlinlinkeddeccircleview

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.anwesh.uiprojects.linkeddeccircleview.LinkedDecCircleView

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        LinkedDecCircleView.create(this)
    }
}
