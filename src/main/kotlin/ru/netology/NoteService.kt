package ru.netology

class NoteService {
    var items = mutableListOf<Note>()
    var comments = emptyArray<Comment>()
    var countComments = Comments()

    fun add(title: String, text: String): Int {

        val newId = if (items.isEmpty()) {
            0
        } else {
            items[items.lastIndex].id + 1
        }
        val newNote = Note(id = newId, title = title, text = text)
        items.add(newNote)
        return newId
    }

    fun get(noteIds: String = "",
            userId: Int = 0,
            offset: Int = 0,
            count: Int = 20,
            sort: Int = 0): List<Note>? {
        val idNotes: List<Int> = noteIds.split(",").map { it.toInt() }
        return if (idNotes.isNotEmpty()) {
            items.filter { idNotes.contains(it.id) }
        } else {
            items
        }
    }

    fun getById(noteIds: Int,
                ownerId: Int = 0,
                needWiki: Int = 0
    ): Note? {
        items.firstOrNull { it.id == noteIds }
                .let {
                    if (it == null) {
                        println("getById: Ошибка 180 Заметка $noteIds не найдена ")
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
        throw NoteException(180, "edit: Заметка $noteIds не найдена ")
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
        throw NoteException(180, "delete: Заметка $noteIds не найдена ")
    }
}

class NoteException(cod: Int?, message: String?) : Throwable() {
    val errorN: Any = if (cod != null) println("Error $cod") else {
    }
    val errorMessage: Any = if (message != null) println(message) else {
    }
}



