package ge.tbabunashvili.notes

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.room.Query
import ge.tbabunashvili.notes.data.entity.Note
import ge.tbabunashvili.notes.data.NotesDatabase
import java.sql.Time
import java.sql.Timestamp

class Repository(private val db: NotesDatabase) {

    fun addItem(note: Note): Long {
        return db.notesDao().addNote(note)
    }

    fun getNotes(): List<Note> = db.notesDao().getAllNotes()


    fun getNoteWithId(id: Long): List<Note> {
        return db.notesDao().getWithId(id)
    }


    fun getNoteOfParent(parent : Long) : List<Note> = db.notesDao().getNotesOfParent(parent)

    fun getDoneNoteOfParent(parent : Long) : List<Note> = db.notesDao().getDoneNotesOfParent(parent)

    fun removeSingle(noteId: Long) {
        db.notesDao().removeNote(noteId)
    }
    fun removeChildren(noteId: Long) {
        db.notesDao().removeNotesOfParent(noteId)
    }

    fun getPinnedParents(): List<Note> = db.notesDao().getPinnedParents()

    fun getunPinnedParents(): List<Note> = db.notesDao().getunPinnedParents()

    fun getPinnedParentsWithTitle(title: String): List<Note> = db.notesDao().getPinnedParentsWithTitle(title)

    fun getunPinnedParentsWithTitle(title: String): List<Note> = db.notesDao().getunPinnedParentsWithTitle(title)

}