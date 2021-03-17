package com.jepster.minipaint

import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowInsets
import android.view.WindowInsetsController

import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(null)
        val myCanvasView = MyCanvasView(this)
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.R) {
            @Suppress("DEPRECATION")
            myCanvasView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN
        }
        myCanvasView.contentDescription = getText(R.string.canvasContentDescription)
        setContentView(myCanvasView)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.setDecorFitsSystemWindows(false)
            val controller: WindowInsetsController? = window.insetsController
            controller!!.hide(WindowInsets.Type.statusBars() or
                    WindowInsets.Type.navigationBars())
            controller.systemBarsBehavior =
                WindowInsetsController.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        }
    }
}
