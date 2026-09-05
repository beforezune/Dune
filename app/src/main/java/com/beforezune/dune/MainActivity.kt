package com.beforezune.dune

import android.app.Activity
import android.os.Bundle
import android.widget.TextView

class MainActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(TextView(this).apply {
            text = "Dune\n\nParental safety foundation\n\nExplicit consent and visible controls"
            textSize = 20f
            setPadding(48, 80, 48, 48)
        })
    }
}
