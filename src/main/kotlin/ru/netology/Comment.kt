package ru.netology

import java.time.Clock

data class Comment(val id: Int = 0,       //идентификатор комментария
                   val nid: Int = 0,      //идентификатор заметки
                   val oid:Int =0,        //идентификатор владельца заметки;
                   val uid: Int = 0,      //идентификатор автора комментария
                   val date: Int = Clock.systemUTC().millis().toInt(),
                   var message:String = "",
                   val replyTo: Int? = 0, //идентификатор пользователя комментария,на который этот ответ
                   val guid:  String = "0"
                  )  {  }
