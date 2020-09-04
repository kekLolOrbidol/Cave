package com.animgames.cave.classes

import com.animgames.cave.R
import kotlin.system.exitProcess

class Game {
    lateinit var currentLocation:Location

    lateinit var startScreen:StartRoom
    lateinit var none:RoomImpossible
    lateinit var entryHall:Room
    lateinit var L1:Room
    lateinit var L1L2:RoomWithEnemy
    lateinit var L1R2:Room
    lateinit var L1R2F3:RoomWithEnemy
    lateinit var L1R2R3:TunnelWithEnemy

    lateinit var R1:Room
    lateinit var R1L2:RoomWithEnemy
    lateinit var R1R2:Room
    lateinit var R1R2L3:Room
    lateinit var R1R2F3:RoomWithEnemy
    lateinit var R1R2L3F4:Room
    lateinit var R1R2L3R4:RoomWithEnemy
    lateinit var R1R2L3F4L5:TunnelWithEnemy
    lateinit var R1R2L3F4R5:Room
    lateinit var R1R2L3F4R5F6:TunnelWithEnemy
    lateinit var R1R2L3F4R5L6:Room

    lateinit var R1R2L3F4R5L6Fwin:Room
    lateinit var R1R2L3F4R5L6L7:TunnelWithEnemy
    lateinit var R1R2L3F4R5L6R7:RoomWithEnemy

    //win
    lateinit var victoryRoom:Room

    init {
        main()
    }
    private fun main(){
        val rightLeft:Int = R.drawable.rl1
        val rightLeftHP:Int = R.drawable.rl2
        val forwLeft:Int = R.drawable.fl
        val forwRight:Int = R.drawable.fr
        val monsterWall:Int = R.drawable.wallmonster1
        val monsterTunnel:Int = R.drawable.hallmonster1

        startScreen = StartRoom("--start--",R.drawable.hallmonster1,"Welcome to the Cave! \n\nWell, you gotta get out of here..")
        none = RoomImpossible("",0)
        entryHall = Room("Game Over", R.drawable.entry,"Main entrance to the Cave.")

        //left side
        L1 = Room("Move Left", rightLeft,"I just heard a strange voice.")
        L1L2 = RoomWithEnemy("Move Left", monsterWall,"Oh no, help me!")
        L1R2 = Room("Move Right", forwRight,"That's a long corridor.")
        L1R2F3 = RoomWithEnemy("Move Forward",monsterWall,"I can't move!")
        L1R2R3 = TunnelWithEnemy("Move Right",monsterTunnel,"Something is hiding there..")

        //right side
        R1 = Room("Move Right", rightLeftHP,"Is that a health potion?")//How did i get here?
        R1L2 = RoomWithEnemy("Move Left",monsterWall,"What is that?")
        R1R2 = Room("Move Right",forwLeft,"it's getting darker with each step.")
        R1R2L3 = Room("Move Left",forwRight,"Where should i go?")
        R1R2F3 = RoomWithEnemy("Move Forward",monsterWall,"That's certainly a wrong way..")
        R1R2L3F4 = Room("Move Forward",rightLeft,"Getting here was a bad idea.")
        R1R2L3R4 = RoomWithEnemy("Move Right",monsterWall,"Something is behind me!")
        R1R2L3F4L5 = TunnelWithEnemy("Move Left",monsterTunnel,"how am i supposed to get through?")
        R1R2L3F4R5 = Room("Move Right",forwLeft,"I am almost there.")
        R1R2L3F4R5F6 = TunnelWithEnemy("Move Forward",monsterTunnel,"Get away!")

        //final crossroads
        R1R2L3F4R5L6 = Room("Move Left",R.drawable.crossroads,"Damn it! They are everywhere..")
        R1R2L3F4R5L6Fwin = Room("Move Forward",R.drawable.victory,"We're home now!")
        R1R2L3F4R5L6L7 = TunnelWithEnemy("Move Left",monsterTunnel,"I can't escape him..")
        R1R2L3F4R5L6R7 = RoomWithEnemy("Move Right",monsterWall,"It's a dead end.")


        victoryRoom = Room("Exit",R.drawable.victory,"Congratulations, you have completed the game!")

        initLeftSide()
        initRightSide()
    }

    private fun initLeftSide(){
        startScreen.Exits = arrayOf(entryHall)
        entryHall.Exits = arrayOf(none,L1,R1)
        L1.Exits = arrayOf(none,L1L2,L1R2)
        L1L2.Exits = arrayOf(entryHall)
        L1R2.Exits = arrayOf(L1R2F3,none,L1R2R3)
        L1R2F3.Exits = arrayOf(entryHall)
        L1R2R3.Exits = arrayOf(entryHall)
    }

    private fun initRightSide(){
        R1.Exits = arrayOf(none,R1L2,R1R2)
        R1L2.Exits = arrayOf(entryHall)
        R1R2.Exits = arrayOf(R1R2F3,R1R2L3,none)
        R1R2L3.Exits = arrayOf(R1R2L3F4,none,R1R2L3R4)
        R1R2F3.Exits = arrayOf(entryHall)
        R1R2L3F4.Exits = arrayOf(none,R1R2L3F4L5,R1R2L3F4R5)
        R1R2L3R4.Exits = arrayOf(entryHall)
        R1R2L3F4L5.Exits = arrayOf(entryHall)
        R1R2L3F4R5.Exits = arrayOf(R1R2L3F4R5F6,R1R2L3F4R5L6,none)
        R1R2L3F4R5F6.Exits = arrayOf(entryHall)

        //final crossroads
        R1R2L3F4R5L6Fwin.Exits = arrayOf(victoryRoom,none,none)
        R1R2L3F4R5L6L7.Exits = arrayOf(entryHall)
        R1R2L3F4R5L6R7.Exits = arrayOf(entryHall)
        R1R2L3F4R5L6.Exits = arrayOf(R1R2L3F4R5L6Fwin,R1R2L3F4R5L6L7,R1R2L3F4R5L6R7)
        victoryRoom.Exits = arrayOf(startScreen,startScreen,startScreen) //check it!

    }

    fun MoveToANewLocation(newLocation:Int):Boolean{
        //game is won
        if (currentLocation.direction == "Exit")
            exitProcess(0)
        currentLocation = currentLocation.Exits[newLocation]

        return currentLocation is RoomWithEnemy
    }

    fun SetStartLocation(){
        currentLocation = startScreen
    }
}