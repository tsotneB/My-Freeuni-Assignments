package ge.tbabunashvili.notes.data.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import ge.tbabunashvili.notes.data.entity.Note
import java.sql.Timestamp

@Dao
interface NotesDAO {
    @Query("select * from Note where parentId=-1")
    fun getParentNotes(): List<Note>

    @Query("select * from Note where parentId = :parent order by id")
    fun getNotesOfParent(parent: Long): List<Note>


    @Query("select * from Note where parentId = -1 and pinned = 1 order by id desc")
    fun getPinnedParents(): List<Note>

    @Query("select * from Note where parentId = -1 and pinned = 0 order by id desc")
    fun getunPinnedParents(): List<Note>

    @Query("select * from Note where parentId = -1 and pinned = 1 and text like :title order by id desc")
    fun getPinnedParentsWithTitle(title: String): List<Note>

    @Query("select * from Note where parentId = -1 and pinned = 0 and text like :title+'%' order by id desc")
    fun getunPinnedParentsWithTitle(title: String): List<Note>

    @Query("select * from Note where parentId = :parent and done = 1 order by id")
    fun getDoneNotesOfParent(parent: Long): List<Note>

/*
    @Query("select * from Note where myId=:id")
    fun getWithMyId(id: Int) : LiveData<List<Note>>*/

    @Query("DELETE FROM Note WHERE id = :noteId")
    fun removeNote(noteId: Long)

    @Query("DELETE FROM Note WHERE parentId = :parent")
    fun removeNotesOfParent(parent: Long)

    @Query("UPDATE Note SET done=:done, text=:text where id=:noteId")
    fun changeNote(noteId: Long, text: String, done: Boolean)

    @Insert
    fun addNote(note: Note):Long

    @Query("select * from Note order by id desc")
    fun getAllNotes(): List<Note>

    @Query("select * from Note where id = :noteId")
    fun getWithId(noteId: Long) : List<Note>
}