import org.junit.Assert.assertEquals
import org.junit.Test
import ru.netology.*
import kotlin.test.assertFails

class MainTest {
    @Test
    fun note_Add() {
        val testNotes = NoteService()
        testNotes.add(title = "First test note",
                text = "First test Text")
        testNotes.add(title = "Second test note",
                text = "Second test Text")
        assertEquals(2, testNotes.items.size)
        assertEquals("Second test Text", testNotes.items[1].text)
    }

    @Test
    fun note_Get() {
        val testNotes = NoteService()
        testNotes.add(title = "First test note",
                text = "First test Text")
        testNotes.add(title = "Second test note",
                text = "Second test Text")
        assertEquals(2, testNotes.get(noteIds = "0,1")?.size)
        assertEquals("Second test Text", testNotes.get("0,1")?.get(1)?.text)
    }

    @Test
    fun note_Edit() {
        val testNotes = NoteService()
        testNotes.add(title = "First test note",
                text = "First test Text")
        testNotes.add(title = "Second test note",
                text = "Second test Text")
        assertEquals(1, testNotes.edit(1, "newTitle", "newText"))
        assertEquals("newText", testNotes.items[1].text)
    }

    @Test
    fun note_getById() {
        val testNotes = NoteService()
        testNotes.add(title = "First test note",
                text = "First test Text")
        testNotes.add(title = "Second test note",
                text = "Second test Text")
        assertEquals(1, testNotes.getById(1)?.id)
    }

    @Test
    fun note_delete() {
        val testNotes = NoteService()
        testNotes.add(title = "First test note",
                text = "First test Text")
        testNotes.add(title = "Second test note",
                text = "Second test Text")
        assertEquals(1, testNotes.delete(0))
        assertFails { testNotes.delete(0) }
    }

    @Test
    fun comment_Create() {
        val comments = CommentService()
        assertEquals(0,
                comments.createComment(noteId = 1, guid = "0", message = "хороший комментарий"))
        assertFails { comments.createComment(noteId = 1, guid = "0", message = "хороший комментарий") }
    }

    @Test
    fun comment_CreateMany() {
        val notes = NoteService()
        val comments = CommentService()
        notes.add(title = "First test note",
                text = "First test Text")
        notes.add(title = "Second test note",
                text = "Second test Text")
        comments.items = notes.items
        assertEquals(0,
                comments.createComment(noteId = 0, guid = "0", message = "первый комментарий"))
        assertEquals(1,
                comments.createComment(noteId = 1, guid = "10", message = "второй комментарий"))
        assertEquals(2,
                comments.createComment(noteId = 0, guid = "3", message = "третий комментарий"))
        assertEquals(3,
                comments.createComment(noteId = 1, guid = "5", message = "четвертый комментарий"))
        assertEquals(0, comments.comments[2].nid)
        assertEquals("четвертый комментарий", comments.comments[3].message)
        assertEquals(4, comments.countComments.count)
    }

    @Test(expected = CommentException::class)
    fun comment_CreateCrash() {
        val notes = NoteService()
        val comments = CommentService()
        notes.add(title = "First test note",
                text = "First test Text")
        notes.add(title = "Second test note",
                text = "Second test Text")
        comments.items = notes.items
        comments.createComment(noteId = 1, guid = "0", message = "хороший комментарий")
        comments.createComment(noteId = 1, guid = "0", message = "ошибочный комментарий")
    }

    @Test
    fun comment_getComments() {
        val notes = NoteService()
        val comments = CommentService()
        notes.add(title = "First test note",
                text = "First test Text")
        notes.add(title = "Second test note",
                text = "Second test Text")
        comments.items = notes.items
        comments.createComment(noteId = 0, guid = "0", message = "первый комментарий")
        comments.createComment(noteId = 0, guid = "10", message = "второй комментарий")
        comments.createComment(noteId = 0, guid = "3", message = "третий комментарий")
        comments.createComment(noteId = 1, guid = "5", message = "четвертый комментарий")
        assertEquals(3, comments.getComments(0).size)
    }

    @Test
    fun comment_editComment() {
        val notes = NoteService()
        val comments = CommentService()
        notes.add(title = "First test note",
                text = "First test Text")
        notes.add(title = "Second test note",
                text = "Second test Text")
        comments.items = notes.items
        comments.createComment(noteId = 0, guid = "0", message = "первый комментарий")
        comments.createComment(noteId = 0, guid = "10", message = "второй комментарий")
        comments.createComment(noteId = 0, guid = "3", message = "третий комментарий")
        comments.createComment(noteId = 1, guid = "5", message = "четвертый комментарий")
        assertEquals(1, comments.editComment(2, message = "исправлено"))
        assertEquals("исправлено", comments.comments[2].message)
    }

    @Test(expected = CommentException::class)
    fun comment_editCommentCrash() {
        val notes = NoteService()
        val comments = CommentService()
        notes.add(title = "First test note",
                text = "First test Text")
        notes.add(title = "Second test note",
                text = "Second test Text")
        comments.items = notes.items
        comments.createComment(noteId = 0, guid = "0", message = "первый комментарий")
        comments.createComment(noteId = 0, guid = "10", message = "второй комментарий")
        comments.createComment(noteId = 0, guid = "3", message = "третий комментарий")
        comments.createComment(noteId = 1, guid = "5", message = "четвертый комментарий")
        assertEquals(1, comments.editComment(4, message = "исправлено"))
    }

    @Test
    fun comment_deleteComment() {
        val notes = NoteService()
        val comments = CommentService()
        notes.add(title = "First test note",
                text = "First test Text")
        notes.add(title = "Second test note",
                text = "Second test Text")
        comments.items = notes.items
        comments.createComment(noteId = 0, guid = "0", message = "первый комментарий")
        comments.createComment(noteId = 0, guid = "10", message = "второй комментарий")
        comments.createComment(noteId = 0, guid = "3", message = "третий комментарий")
        comments.createComment(noteId = 1, guid = "5", message = "четвертый комментарий")
        assertEquals(1, comments.deleteComment(2))
        assertEquals(3, comments.countComments.count)
        assertFails { comments.deleteComment(2) }
    }

    @Test
    fun comment_restoreComment() {
        val notes = NoteService()
        val comments = CommentService()
        notes.add(title = "First test note",
                text = "First test Text")
        notes.add(title = "Second test note",
                text = "Second test Text")
        comments.items = notes.items
        comments.createComment(noteId = 0, guid = "0", message = "первый комментарий")
        comments.createComment(noteId = 0, guid = "10", message = "второй комментарий", ownerId = 1)
        comments.createComment(noteId = 0, guid = "3", message = "третий комментарий")
        comments.createComment(noteId = 1, guid = "5", message = "четвертый комментарий")
        assertEquals(1, comments.deleteComment(2))
        assertEquals(1, comments.deleteComment(1, ownerId = 1))
        assertEquals(2, comments.countComments.count)
        assertEquals(1, comments.restoreComment(commentId = 2, ownerId = 0))
        assertFails { comments.restoreComment(commentId = 2, ownerId = 0) }
        assertFails { comments.restoreComment(commentId = 1, ownerId = 0) }
    }
}