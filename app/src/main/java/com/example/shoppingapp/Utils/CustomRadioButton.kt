package com.example.shoppingapp.Utils

import android.content.Context
import android.graphics.Typeface
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatRadioButton

class CustomRadioButton(context: Context, attributeSet: AttributeSet):AppCompatRadioButton(context,attributeSet) {

    init {
        applyfont()
    }
    private fun applyfont(){
        val typeface:Typeface=
            Typeface.createFromAsset(context.assets,"montserrat.bold.ttf")
    }
}