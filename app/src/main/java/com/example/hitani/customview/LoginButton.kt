package com.example.hitani.customview

import android.content.Context
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.Gravity
import androidx.appcompat.widget.AppCompatButton
import androidx.core.content.ContextCompat
import com.example.hitani.R

class LoginButton: AppCompatButton {
    private lateinit var enabledBackground: Drawable
    private var txtColor: Int = 0

    constructor(context: Context): super(context){
        init()
    }

    constructor(context: Context, attrs: AttributeSet): super(context, attrs){
        init()
    }
    constructor(context: Context, attrs: AttributeSet, defStyleAttr:Int): super(context, attrs, defStyleAttr){
        init()
    }

    private fun init() {
        txtColor = ContextCompat.getColor(context, android.R.color.background_light)
        enabledBackground = ContextCompat.getDrawable(context, R.drawable.bg_button)as Drawable
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        background = enabledBackground
        setTextColor(txtColor)
        textSize = 14f
        gravity = Gravity.CENTER
        text = context.getString(R.string.login_text)
    }
}