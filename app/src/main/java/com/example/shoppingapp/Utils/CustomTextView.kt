package com.example.shoppingapp.Utils

import android.content.Context
import android.graphics.Typeface
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView

class CustomTextView(context:Context, attributeSet: AttributeSet): AppCompatTextView(context,attributeSet) {
    init{
        applyFont()
    }
    private fun applyFont(){
        val boldTypeFace:Typeface=
            Typeface.createFromAsset(context.assets,"montserrat.bold.ttf")
        typeface=boldTypeFace
    }

}