package com.example.cw1dicegame

// https://universityofwestminster-my.sharepoint.com/:v:/g/personal/w1937706_westminster_ac_uk/EavvXKP_ztVDqwTIj7GDA64BzwBeHQM7epa3iGT61sdhyA?e=vrZRf8
// link to my recording on my univeristy onedrive
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.content.Intent
import androidx.appcompat.app.AlertDialog

import android.view.Gravity
import android.view.View
import android.widget.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        var NGbutton = findViewById<Button>(R.id.newGame)
        var ABTbutton = findViewById<Button>(R.id.about)
        var helpButton = findViewById<Button>(R.id.help)

        NGbutton.setOnClickListener{
            val intention = Intent(this, GameLoop::class.java)
            startActivity(intention)}

        ABTbutton.setOnClickListener{
            var popupView: View = layoutInflater.inflate(R.layout.popup, null)
            var popupWindow = PopupWindow(this)
            popupWindow.contentView = popupView
            popupWindow.showAtLocation(popupView, Gravity.CENTER, 0, 0)
            popupView.setOnClickListener { popupWindow.dismiss() }
        }
        helpButton.setOnClickListener{
            var popupView: View = layoutInflater.inflate(R.layout.popup_help, null)
            var popupWindow = PopupWindow(this)
            popupWindow.contentView = popupView
            popupWindow.showAtLocation(popupView, Gravity.CENTER, 0, 0)
            popupView.setOnClickListener { popupWindow.dismiss() }
        }
    }
}