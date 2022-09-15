package ge.tbabunashvili.notes.singlenotepage

import android.app.Application
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import ge.tbabunashvili.notes.data.entity.Note
import ge.tbabunashvili.notes.data.NotesDatabase
import ge.tbabunashvili.notes.NoteViewModel
import ge.tbabunashvili.notes.R
import ge.tbabunashvili.notes.Repository
import java.util.*


class AddItemActivity() : AppCompatActivity(),NoteListener {

    private var checkedList = mutableListOf<Note>()
    private var uncheckedList = mutableListOf<Note>()
    private val random = Random()
    private var isPinned = false

    val viewModel: NoteViewModel by lazy {
        ViewModelProvider(this, MainViewModelsFactory(application)).get(NoteViewModel::class.java)
    }

    private var uncheckAdapter = NoteAdapter(uncheckedList, this)//,viewModel)
    private var checkAdapter = NoteAdapter(checkedList, this)//,viewModel)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_item)
        findViewById<RecyclerView>(R.id.unDone).adapter = uncheckAdapter
        findViewById<RecyclerView>(R.id.done).adapter = checkAdapter
        val id = intent.getLongExtra(Note, -1)
        val def : Long = -1
        var title = Note(-1,"",-1, false, false)
        var notes = listOf<Note>()
        var doneNotes = listOf<Note>()
        if (id != def) {
            Thread(Runnable {
                title = viewModel.getNoteWithId(id)[0]
                notes = viewModel.getNotesOfParents(title.id)
                doneNotes = viewModel.getDoneNotesOfParents(title.id)
                viewModel.deleteInfo(title.id)
                isPinned = title.pinned
                val lst = notes - doneNotes
                for (note in lst) {
                    uncheckedList.add(note)
                }
                for (note in doneNotes) {
                    checkedList.add(note)
                }
                uncheckAdapter.items = uncheckedList
                checkAdapter.items = checkedList
                findViewById<EditText>(R.id.noteParent).setText(title.text)
                uncheckAdapter.notifyDataSetChanged()
                checkAdapter.notifyDataSetChanged()
                if (title.pinned) {
                    findViewById<ImageButton>(R.id.pin).setBackgroundResource(R.drawable.ic_pinned)
                }
            }).start()
        }
    }

    private fun saveData() {
        val parentItem = Note(0,findViewById<EditText>(R.id.noteParent).text.toString(),-1, false, isPinned)
        Thread(Runnable {
            val id = viewModel.saveNewNote(parentItem)
            val lastId = viewModel.getIds()
            for (note in uncheckedList + checkedList) {
                note.parentId =  id
                viewModel.saveNewNote(note)
            }
        }).start()
        setResult(RESULT_OK)
    }

    companion object{
        const val Note = "id"
    }

    fun changePinStatus(view: View) {
        val img = view as ImageButton
        if (isPinned) {
            img.setBackgroundResource(R.drawable.ic_pin)
        }   else {
            img.setBackgroundResource(R.drawable.ic_pinned)
        }
        isPinned = !isPinned
    }
    override fun onBackPressed() {
        saveData()
        super.onBackPressed()
    }

    fun backToHome(view: View) {
        saveData()
        finish()
    }

    fun addItem(view: View) {
        val id = random.nextInt(100000)
        val note = Note(0, "", 0, false, false)
        uncheckedList.add(note)
        uncheckAdapter.items = uncheckedList
        uncheckAdapter.notifyDataSetChanged()

    }

    override fun onCheckboxChange(note: Note, position: Int) {
        var listToInsert = checkedList
        var listToRemove = uncheckedList
        var adaptToIns = checkAdapter
        var adaptToRem = uncheckAdapter
        if (note.done) {
            listToInsert = uncheckedList
            listToRemove = checkedList
            adaptToIns = uncheckAdapter
            adaptToRem = checkAdapter
        }
        note.done = !note.done
        listToInsert.add(note)
        listToRemove.removeAt(position)
        adaptToIns.items = listToInsert
        adaptToRem.items = listToRemove
        adaptToIns.notifyDataSetChanged()
        adaptToRem.notifyDataSetChanged()
    }

    override fun onRemoveClick(note: Note) {
        uncheckedList.removeAt(uncheckedList.size-1)
        uncheckAdapter.items = uncheckedList
        uncheckAdapter.notifyDataSetChanged()
    }
}


class MainViewModelsFactory(var application: Application) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(NoteViewModel::class.java)) {
            return NoteViewModel(
                Repository(
                    Room.databaseBuilder(application, NotesDatabase::class.java, "DatabaseForNotes").build()
                )
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }


}