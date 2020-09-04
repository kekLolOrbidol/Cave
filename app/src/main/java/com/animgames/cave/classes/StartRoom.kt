package com.animgames.cave.classes

import com.animgames.cave.R

class StartRoom(direction: String, image:Int, decoration: String)
    : RoomWithEnemy(direction, image, decoration) {

    override fun getEventImages(): Array<Int> {
        return arrayOf(image,
            R.drawable.hallmonster1,
            R.drawable.hallmonster2,
            R.drawable.hallmonster3)
    }

}