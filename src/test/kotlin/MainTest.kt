import org.junit.Assert.assertEquals
import org.junit.Test
import ru.netology.*
import kotlin.test.assertFails

class MainTest {
    @Test
    fun note_Add(){
        val testNotes = NoteService()

        testNotes.add(title ="First test note",
                text = "First test Text")
        testNotes.add(title ="Second test note",
                text = "Second test Text")
        assertEquals(2,testNotes.notes.count)
        assertEquals("Second test Text",testNotes.items[1].text)
    }

    @Test
    fun note_Get(){
        val testNotes = NoteService()

        testNotes.add( title ="First test note",
                   text = "First test Text"      )
        testNotes.add( title ="Second test note",
                    text = "Second test Text" )

        assertEquals(2,testNotes.get(noteIds = "0,1")?.size)
        assertEquals( "Second test Text",testNotes.get("0,1")?.get(1)?.text)
    }

    @Test
    fun note_Edit(){
        val testNotes = NoteService()

        testNotes.add( title ="First test note",
                text = "First test Text"      )
        testNotes.add( title ="Second test note",
                text = "Second test Text" )
        assertEquals (1, testNotes.edit(1,"newTitle","newText"))
        assertEquals("newText", testNotes.items[1].text)
    }

    @Test
    fun note_getById(){
        val testNotes = NoteService()
        testNotes.add( title ="First test note",
                text = "First test Text"      )
        testNotes.add( title ="Second test note",
                text = "Second test Text" )
        assertEquals (1, testNotes.getById(1)?.id)
    }

    @Test
    fun note_delete(){
        val testNotes = NoteService()
        testNotes.add( title ="First test note",
                text = "First test Text"      )
        testNotes.add( title ="Second test note",
                text = "Second test Text" )
        assertEquals (1, testNotes.delete(0) )
        assertEquals (180, testNotes.delete(0) )
    }


    @Test
    fun comment_Create(){
        val notes = NoteService()
        notes.add( title ="First test note",
                text = "First test Text"      )
        notes.add( title ="Second test note",
                text = "Second test Text" )
        assertEquals (0,
                notes.createComment(noteId = 1,guid = "0", message = "хороший комментарий"))
        assertFails {notes.createComment(noteId = 1,guid = "0", message = "хороший комментарий")}

    }

    @Test
    fun comment_CreateMany(){
        val notes = NoteService()
        notes.add( title ="First test note",
                text = "First test Text"      )
        notes.add( title ="Second test note",
                text = "Second test Text" )
        assertEquals (0,
                notes.createComment(noteId = 0,guid = "0", message = "первый комментарий"))
        assertEquals (1,
                notes.createComment(noteId = 1,guid = "10", message = "второй комментарий"))
        assertEquals (2,
                notes.createComment(noteId = 0,guid = "3", message = "третий комментарий"))
        assertEquals (3,
                notes.createComment(noteId = 1,guid = "5", message = "четвертый комментарий"))
        assertEquals(0,notes.comments[2].nid)
        assertEquals("четвертый комментарий",notes.comments[3].message)
        assertEquals(4,notes.countComments.count)
    }


    @Test(expected = MyException::class)
    fun comment_CreateCrash(){
        val notes = NoteService()
        notes.add( title ="First test note",
                text = "First test Text"      )
        notes.add( title ="Second test note",
                text = "Second test Text" )
        notes.createComment(noteId = 1,guid = "0", message = "хороший комментарий")
        notes.createComment(noteId = 1,guid = "0", message = "ошибочный комментарий")
    }

    @Test
    fun comment_getComments(){
        val notes = NoteService()
        notes.add( title ="First test note",
                text = "First test Text"      )
        notes.add( title ="Second test note",
                text = "Second test Text" )
        notes.createComment(noteId = 0,guid = "0", message = "первый комментарий")
        notes.createComment(noteId = 0,guid = "10", message = "второй комментарий")
        notes.createComment(noteId = 0,guid = "3", message = "третий комментарий")
        notes.createComment(noteId = 1,guid = "5", message = "четвертый комментарий")
        assertEquals(3,notes.getComments(0).size)
    }

    @Test
    fun comment_editComment(){
        val notes = NoteService()
        notes.add( title ="First test note",
                text = "First test Text"      )
        notes.add( title ="Second test note",
                text = "Second test Text" )
        notes.createComment(noteId = 0,guid = "0", message = "первый комментарий")
        notes.createComment(noteId = 0,guid = "10", message = "второй комментарий")
        notes.createComment(noteId = 0,guid = "3", message = "третий комментарий")
        notes.createComment(noteId = 1,guid = "5", message = "четвертый комментарий")
        assertEquals(1,notes.editComment(2,message = "исправлено"))
        assertEquals("исправлено", notes.comments[2].message)
    }

    @Test(expected = MyException::class)
    fun comment_editCommentCrash(){
        val notes = NoteService()
        notes.add( title ="First test note",
                text = "First test Text"      )
        notes.add( title ="Second test note",
                text = "Second test Text" )
        notes.createComment(noteId = 0,guid = "0", message = "первый комментарий")
        notes.createComment(noteId = 0,guid = "10", message = "второй комментарий")
        notes.createComment(noteId = 0,guid = "3", message = "третий комментарий")
        notes.createComment(noteId = 1,guid = "5", message = "четвертый комментарий")
        assertEquals(1,notes.editComment(4,message = "исправлено"))
    }

    @Test
    fun comment_deleteComment(){
        val notes = NoteService()
        notes.add( title ="First test note",
                text = "First test Text"      )
        notes.add( title ="Second test note",
                text = "Second test Text" )
        notes.createComment(noteId = 0,guid = "0", message = "первый комментарий")
        notes.createComment(noteId = 0,guid = "10", message = "второй комментарий")
        notes.createComment(noteId = 0,guid = "3", message = "третий комментарий")
        notes.createComment(noteId = 1,guid = "5", message = "четвертый комментарий")
        assertEquals(1,notes.deleteComment(2))
        assertEquals(3,notes.countComments.count)
        assertFails {notes.deleteComment(2)  }
    }
}