package com.animgames.cave

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_game.*

class GameActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)

        val intent = intent
        var message = intent.getStringExtra("myMsg")
        textView.text = message

        button.setOnClickListener{
            val intent2= Intent(this,MainActivity::class.java)
            startActivity(intent2)
        }
    }
}