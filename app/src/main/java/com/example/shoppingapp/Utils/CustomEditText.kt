package com.example.shoppingapp.Utils

import android.content.Context
import android.graphics.Typeface
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatEditText

class CustomEditText(context: Context,attributeSet: AttributeSet):AppCompatEditText(context,attributeSet) {
    init{
        applyFont()
    }
    private fun applyFont(){
        val boldTypeFace: Typeface =
            Typeface.createFromAsset(context.assets,"montserrat.regular.ttf")
        typeface=boldTypeFace
    }
}