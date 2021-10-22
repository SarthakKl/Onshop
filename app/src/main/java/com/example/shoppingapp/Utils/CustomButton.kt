package com.example.shoppingapp.Utils

import android.content.Context
import android.graphics.Typeface
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatButton


class CustomButton(context: Context, attributeSet: AttributeSet):AppCompatButton(context,attributeSet) {
    init{
        applyFont()
    }
    private fun applyFont(){
        val boldTypeFace: Typeface =
            Typeface.createFromAsset(context.assets,"montserrat.regular.ttf")
        typeface=boldTypeFace
    }

}