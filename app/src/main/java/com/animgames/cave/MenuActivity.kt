package com.animgames.cave

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_menu.*

class MenuActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)

        button_swipe.setOnClickListener{
            val intent2= Intent(this,MainActivity::class.java)
            intent2.putExtra("mode", 0)
            startActivity(intent2)
        }

        button_buttons.setOnClickListener{
            val intent2= Intent(this,MainActivity::class.java)
            intent2.putExtra("mode", 1)
            startActivity(intent2)
        }
    }
}