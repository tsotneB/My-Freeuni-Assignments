package ge.tbabunashvili.notes

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ge.tbabunashvili.notes.data.entity.Note
import ge.tbabunashvili.notes.singlenotepage.NoteListener
import java.sql.Timestamp

class NoteViewModel(val rep: Repository): ViewModel() {

    fun saveNewNote(note: Note): Long {
        return rep.addItem(note)
    }

    fun getNotesOfParents(parentId: Long) : List<Note> = rep.getNoteOfParent(parentId)

    fun getDoneNotesOfParents(parentId: Long) : List<Note> = rep.getDoneNoteOfParent(parentId)

    fun getPinnedParents(): List<Note> = rep.getPinnedParents()

    fun getunPinnedParents(): List<Note> = rep.getunPinnedParents()

    fun getPinnedParentsWithTitle(title: String): List<Note> = rep.getPinnedParentsWithTitle(title)

    fun getunPinnedParentsWithTitle(title: String): List<Note> = rep.getunPinnedParentsWithTitle(title)

    fun getNoteWithId(id: Long): List<Note> = rep.getNoteWithId(id)

    fun deleteInfo(myId: Long) {
        rep.removeSingle(myId)
        rep.removeChildren(myId)
    }

    fun getIds(): List<Note> {
        return rep.getNotes()
    }
}