package ru.netology

class NoteService {
    var notes: Notes = Notes(items = emptyList<Note>().toMutableList())
    var items: MutableList<Note> = notes.items
    var comments = emptyArray<Comment>()
    var countComments: Comments = Comments()

    fun add(title: String, text: String): Int {
        val newId = if (items.isEmpty()) {
            0
        } else {
            items.get(items.lastIndex).id + 1
        }
        val newNote: Note = Note(id = newId, title = title, text = text)
        items.add(newNote)
        notes.count++
        return newId
    }

    fun get(noteIds: String = "",
            userId: Int = 0,
            offset: Int = 0,
            count: Int = 20,
            sort: Int = 0): List<Note>? {
        val idNotes: List<Int> = noteIds.split(",").map { it.toInt() }
        return if (idNotes.size > 0) {
            items.filter { it -> idNotes.contains(it.id) }
                    .take(if (count < 100) count else 100)
        } else {
            items
        }
    }

    fun getById(noteIds: Int,
                ownerId: Int = 0,
                needWiki: Int = 0
    ): Note? {
        items.filter { it.id == noteIds }
                .firstOrNull()
                .let {
                    if (it == null) {
                        println("Ошибка 180 Заметка $noteIds не найдена ")
                    }
                    return it
                }
    }


    fun edit(noteIds: Int,
             title: String,
             text: String,
             privacyView: String = "all",
             privacyComment: String = "all"
    ): Int {
        items.find { it.id == noteIds }.let {
            if (it != null) {
                it.title = title
                it.text = text
                return 1
            }
        }
        println("Заметка $noteIds не найдена ")
        return 180
    }

    fun delete(noteIds: Int): Int {
        for ((index, note) in items.withIndex()) {
            if (note.id == noteIds) {
                items.removeAt(index)
                comments = comments.filter { it.nid == noteIds } //убираем комментарии к заметке
                                   .toTypedArray()
                countComments.count = comments.size
                return 1
            }
        }
        println("Заметка $noteIds не найдена ")
        return 180
    }

    fun createComment(noteId: Int,
                      ownerId: Int = 0,
                      replyTo: Int? = null,
                      message: String = "",
                      guid: String = "0"
    ): Int? {
//        try {
        for (comment in comments) {  // для предотвращения повторной отправки одинакового комментария
            if (guid == comment.guid)
                throw MyException(181, "Нет доступа к заметке $noteId")
        }
//        } catch(e: MyException ){
//           e.printError(181, "Нет доступа к заметке")
//            print("Нет доступа к заметке")
//        }
        val cid = if (comments.isEmpty()) 0
        else comments.size
        val newComment = Comment(id = cid,
                nid = noteId,
                oid = ownerId,
                message = message,
                replyTo = replyTo)
        comments = comments.plusElement(newComment)
        countComments.count++
        return cid
    }


    fun getComments(noteId: Int,
                    ownerId: Int = 0,
                    sort: Int = 0,
                    offset: Int = 0,
                    count: Int = 20): List<Comment> {
        return comments.filter { it.nid == noteId && it.oid == ownerId }
                .take(if (count < 100) count else 100)
    }

    fun editComment(commentId: Int,
                    ownerId: Int = 0,
                    message: String = " "): Int {
        comments.find { it.id == commentId && it.oid == ownerId }
                .let {
                    if (it != null) {
                        it.message = message
                        return 1
                    } else throw MyException(183, "Нет доступа к комментарию $commentId")
                }
    }

    fun deleteComment(commentId: Int,
                      ownerId: Int = 0): Int {
        for ((index, comment) in comments.withIndex()) {
            if (comment.id == commentId && comment.oid == ownerId) {
                comments = comments.filter { it.id != commentId || it.oid != ownerId }
                        .toTypedArray()
                countComments.count = comments.size
                return 1
            }
        }
        throw MyException(183, "Нет доступа к комментарию $commentId")
    }

    fun restoreComment(commentId: Int,
                       ownerId: Int = 0): Int {
        return 183
    }
}

class MyException(cod: Int?, message: String?) : Throwable() {
    val errorN: Any = if (cod != null) println("Error $cod") else {
    }
    val errorMessage: Any = if (message != null) println(message) else {
    }
}



