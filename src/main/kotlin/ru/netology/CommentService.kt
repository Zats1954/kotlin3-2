package ru.netology

class CommentService {
    var items = mutableListOf<Note>()
    var comments = emptyArray<Comment>()
    val deletedComments: MutableList<Comment> = arrayListOf()
    var countComments = Comments()

    fun createComment(noteId: Int,
                      ownerId: Int = 0,
                      replyTo: Int? = null,
                      message: String = "",
                      guid: String = "0"
    ): Int? {
        for (comment in comments) {  // для предотвращения повторной отправки одинакового комментария
            if (guid == comment.guid)
                throw CommentException(181, "createComment: Нет доступа к заметке $noteId")
        }
        val cid = if (comments.isEmpty()) 0
        else comments.maxOf { it.id } + 1
        val newComment = Comment(id = cid,
                nid = noteId,
                oid = ownerId,
                message = message,
                replyTo = replyTo)
        comments += newComment
        countComments.count++
        return cid
    }


    fun getComments(noteId: Int,
                    ownerId: Int = 0,
                    sort: Int = 0,
                    offset: Int = 0,
                    count: Int = 20): List<Comment> {
        return comments.filter { it.nid == noteId && it.oid == ownerId }
    }

    fun editComment(commentId: Int,
                    ownerId: Int = 0,
                    message: String = " "): Int {
        comments.find { it.id == commentId && it.oid == ownerId }
                .let {
                    if (it != null) {
                        it.message = message
                        return 1
                    } else throw CommentException(183, "editComment: Нет доступа к комментарию $commentId")
                }
    }

    fun deleteComment(commentId: Int,
                      ownerId: Int = 0): Int {
        val comment = comments.find { it.id == commentId && it.oid == ownerId }
        if (comment != null) {
            comments = comments.filter { it != comment }
                    .toTypedArray()
            deletedComments.add(comment)
            countComments.count = comments.size
            return 1
        } else {
            throw CommentException(183, "deleteComment: Нет доступа к комментарию $commentId")
        }
    }

    fun restoreComment(commentId: Int,
                       ownerId: Int = 0): Int {
        deletedComments.asSequence()
                .firstOrNull { it.id == commentId && it.oid == ownerId }
                .let {
                    if (it != null) {
                        comments += it
                        deletedComments.remove(it)
                        countComments.count = comments.size
                    } else {
                        throw CommentException(183,
                                "restoreComment: нет удаленного комментария $commentId (oid =$ownerId) ")
                    }
                }
        return 1
    }
}

class CommentException(cod: Int?, message: String?) : Throwable() {
    val errorN: Any = if (cod != null) println("Error $cod") else {
    }
    val errorMessage: Any = if (message != null) println(message) else {
    }
}



