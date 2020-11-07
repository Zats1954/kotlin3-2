package ru.netology

import java.time.Clock

class Note(
        val id: Int = 0,
        val ownerId: Int = 0,
        var title:String,
        var text: String,
        var date: Int = Clock.systemUTC().millis().toInt(),
        var comments: Int = 0,
        var readComments: Int = 0,
        var view_url: String = ""
){
}


