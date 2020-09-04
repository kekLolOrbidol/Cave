package com.animgames.cave

import android.content.Intent
import android.content.pm.ActivityInfo
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.os.CountDownTimer
import android.service.autofill.VisibilitySetterAction
import android.transition.Visibility
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.animgames.cave.classes.Game
import com.animgames.cave.classes.OnSwipeTouchListener
import com.animgames.cave.classes.RoomImpossible
import com.animgames.cave.classes.RoomWithEnemy
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {
    val game: Game = Game()
    var gameIsStarted:Boolean = false
    var mode : Int = -1

    lateinit var timer:CountDownTimer

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestedOrientation =  (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_main)
        window.statusBarColor = Color.BLACK
        hideButtons()
        InitGame()
        start_with_buttons.setOnClickListener{
            mode = 1
            start_with_swipe.visibility = View.GONE
            start_with_buttons.visibility = View.GONE
            SetImageAnimation()
            start()
        }
        start_with_swipe.setOnClickListener{
            mode = 0
            start_with_swipe.visibility = View.GONE
            start_with_buttons.visibility = View.GONE
            SetImageAnimation()
            start()
        }
        //mode = intent.getIntExtra("mode", -1)

    }

    fun start(){
        if(mode == 0){
            hideButtons()
            game_root.setOnTouchListener(object : OnSwipeTouchListener(this@MainActivity){
                override fun onSwipeTop() {
                    buttonClick(0)
                }

                override fun onSwipeRight() {
                    buttonClick(2)
                }

                override fun onSwipeLeft() {
                    buttonClick(1)
                }

            })
        }
        activateButtons()

    }

    fun hideButtons(){
        GameButtonForward.visibility = View.GONE
        GameButtonLeft.visibility = View.GONE
        GameButtonRight.visibility = View.GONE
    }


    private fun activateButtons(){
        if(mode == 1){
            GameButtonForward.setOnClickListener{
                buttonClick(0)
            }
            GameButtonLeft.setOnClickListener{
                buttonClick(1)
            }
            GameButtonRight.setOnClickListener{
                buttonClick(2)
            }
        }
        GameButtonGameOver.setOnClickListener{
            if(!gameIsStarted)
                startOver(true)
            else
                startOver(false)
        }
    }

    private fun buttonClick(dir:Int){
        try {
            if(game.currentLocation.Exits[dir] is RoomImpossible){
                Toast.makeText(this@MainActivity, "There's no way!", Toast.LENGTH_SHORT).show() //Room is impossible!

            }else
                MoveToANewLocation(dir)
        }
        catch (e: Exception) {
            Toast.makeText(this@MainActivity, e.toString(), Toast.LENGTH_SHORT).show()
        }
    }

    private fun MoveToANewLocation(newLocation:Int){
        val isMonsterRoom:Boolean = game.MoveToANewLocation(newLocation)
        ShowData()
        if (isMonsterRoom) {
            SetImageAnimation()
        }
    }

    private fun InitGame(){
        game.SetStartLocation()
        ShowData()

    }

    private fun ShowData(){
        //gif author: jjjjjohn on GIPHY
        //https://media2.giphy.com/media/MuE7CqaehCJiZfL8wS/giphy.gif?cid=790b76117f47267955f3a3f2721b4ca2ca5cb07a26b757d4&rid=giphy.gif

        //dwarf image:
        //https://warhammerfantasy.fandom.com/wiki/Dwarf?file=Dwarfhead-0.png

        if(game.currentLocation.direction == "Exit")
            Glide.with(this).asGif().load(R.drawable.skeldance).into(game_image)

        GameDescription.text = game.currentLocation.Description()
        game_image.setImageResource(game.currentLocation.image)
    }

    private fun SetImageAnimation(){
        var counter:Short= 0
        val animationImages = (game.currentLocation as RoomWithEnemy).getEventImages()
        hideButtons()
        timer = object: CountDownTimer(2000, 500) {
            override fun onTick(millisUntilFinished: Long) {
                counter++
                if(counter == 2.toShort())
                    game_image.setImageResource(animationImages[1])
                else if(counter == 3.toShort())
                    game_image.setImageResource(animationImages[2])
            }

            override fun onFinish() {
                game_image.setImageResource(animationImages[3])
                GameButtonGameOver.isVisible = true
            }
        }
        timer.start()
    }

    //--------------- buttons operations ---------------------------

    private fun setButtonsVisibility(){
        if(mode == 1){
            GameButtonForward.isVisible = !GameButtonForward.isVisible
            GameButtonLeft.isVisible = !GameButtonLeft.isVisible
            GameButtonRight.isVisible = !GameButtonRight.isVisible
        }
    }

    private fun startOver(firstStart:Boolean){
        if(firstStart){
            gameIsStarted = true
            GameButtonGameOver.text = "Game Over"
        }
        timer.cancel()
        GameButtonGameOver.isVisible = false
        setButtonsVisibility()
        MoveToANewLocation(0)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        val intent2= Intent(this,MainActivity::class.java)
        startActivity(intent2)
        finish()
    }

}
