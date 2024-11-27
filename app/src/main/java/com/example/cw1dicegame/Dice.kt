package com.example.cw1dicegame

import android.content.Context
import java.util.Random
import android.graphics.drawable.Drawable

class Dice (val context: Context){
    var roll: Int = 0

    fun roll():Int {
        var randomroll = Random()
        //  1 + (0..5) = 1..6
        roll = 1 + randomroll.nextInt(6)
        return roll}

    fun getDrawable(d:Int): Drawable {
        val drawableResource = when (d) {
            // each number corresponds to one of the images in the drawable file
            // used ms paint to make the images
            0 -> R.drawable.dfdefault
            1 -> R.drawable.d1w1937706
            2 -> R.drawable.d2w1937706
            3 -> R.drawable.d3w1937706
            4 -> R.drawable.d4w1937706
            5 -> R.drawable.d5w1937706
            else -> R.drawable.d6w1937706
        }
        return context.getDrawable(drawableResource)!!
    }
}
