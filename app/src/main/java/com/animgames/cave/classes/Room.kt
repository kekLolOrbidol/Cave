package com.animgames.cave.classes

open class Room(direction: String, image: Int, protected val decoration: String) : Location(direction,image) {
    override fun Description():String{
        return "$decoration "
    }
}