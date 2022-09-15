package ge.tbabunashvili.notes.data
import androidx.room.Database
import androidx.room.RoomDatabase
import ge.tbabunashvili.notes.data.dao.NotesDAO
import ge.tbabunashvili.notes.data.entity.Note

@Database(entities = arrayOf(Note::class), version = 1)
abstract class NotesDatabase(): RoomDatabase() {
    abstract fun notesDao(): NotesDAO
}