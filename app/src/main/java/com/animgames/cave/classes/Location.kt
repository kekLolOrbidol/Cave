package com.animgames.cave.classes


abstract class Location(val direction:String,val image:Int) {
    var Exits = arrayOf<Location>()

    open fun Description():String{
        var description:String = "Possible ways:"
        for (Loc in Exits){
            description +=  " "+Loc.direction
        }
        return ""//description
    }
}