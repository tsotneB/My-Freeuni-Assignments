package ge.tbabunashvili.notes.mainpage

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import ge.tbabunashvili.notes.NoteViewModel
import ge.tbabunashvili.notes.singlenotepage.AddItemActivity
import ge.tbabunashvili.notes.R
import ge.tbabunashvili.notes.data.entity.Note
import ge.tbabunashvili.notes.singlenotepage.MainViewModelsFactory


class MainActivity : AppCompatActivity(),MainNoteListener {
    var resultContract = registerForActivityResult(ActivityResultContracts.StartActivityForResult())
    {
            result: ActivityResult? ->
                if (result?.resultCode == RESULT_OK) {
                    refreshData()
                }
    }
    private val lst = mutableListOf<List<Note>>()

    private var unpinnedAdapter = MainNotesAdapter(lst,this)//,viewModel)
    private var pinnedAdapter = MainNotesAdapter(lst,this)//,viewModel)

    val viewModel: NoteViewModel by lazy {
        ViewModelProvider(this, MainViewModelsFactory(application)).get(NoteViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        findViewById<RecyclerView>(R.id.unpinnedNotes).adapter = unpinnedAdapter
        findViewById<RecyclerView>(R.id.pinnedNotes).adapter = pinnedAdapter

        findViewById<RecyclerView>(R.id.unpinnedNotes).addItemDecoration(MainNoteSpacing(resources.getDimensionPixelOffset(R.dimen.smallsep)))
        findViewById<RecyclerView>(R.id.pinnedNotes).addItemDecoration(MainNoteSpacing(resources.getDimensionPixelOffset(R.dimen.smallsep)))

        refreshData()
        findViewById<EditText>(R.id.searchNotes).addTextChangedListener(object : TextWatcher {

            override fun afterTextChanged(s: Editable) {
                val title = findViewById<EditText>(R.id.searchNotes).text.toString()
                if (title != "") {
                    refreshDataWithString(title)
                }   else {
                    refreshData()
                }
            }

            override fun beforeTextChanged(
                s: CharSequence, start: Int,
                count: Int, after: Int
            ) {
            }

            override fun onTextChanged(
                s: CharSequence, start: Int,
                before: Int, count: Int
            ) {

            }
        })
    }

    fun onNewItemClicked(view: View) {
        var intent = Intent(this, AddItemActivity::class.java)
        resultContract.launch(intent)
    }

    fun refreshDataWithString(title: String) {
        var unpinnedList = mutableListOf<List<Note>>()
        var pinnedList = mutableListOf<List<Note>>()
        Thread(Runnable {
            var pinnedl = viewModel.getPinnedParentsWithTitle(title)
            var unpinnedl = viewModel.getunPinnedParentsWithTitle(title)
            runOnUiThread {
                if (pinnedl.size==0) {
                    findViewById<RecyclerView>(R.id.pinnedNotes).visibility=View.GONE
                    findViewById<TextView>(R.id.pinned).visibility=View.GONE
                    findViewById<TextView>(R.id.other).visibility=View.GONE
                }   else {
                    findViewById<RecyclerView>(R.id.pinnedNotes).visibility=View.VISIBLE
                    findViewById<TextView>(R.id.pinned).visibility=View.VISIBLE
                    findViewById<TextView>(R.id.other).visibility=View.VISIBLE
                }
            }
            for (parent in unpinnedl) {
                var notes = listOf<Note>(parent) + viewModel.getNotesOfParents(parent.id)
                unpinnedList.add(notes)
            }
            for (parent in pinnedl) {
                var notes = listOf<Note>(parent) + viewModel.getNotesOfParents(parent.id)
                pinnedList.add(notes)
            }
        }).start()
        unpinnedAdapter.list = unpinnedList
        unpinnedAdapter.notifyDataSetChanged()
        pinnedAdapter.list = pinnedList
        pinnedAdapter.notifyDataSetChanged()
    }

    fun refreshData() {
        var unpinnedList = mutableListOf<List<Note>>()
        var pinnedList = mutableListOf<List<Note>>()
        Thread(Runnable {
            var pinnedl = viewModel.getPinnedParents()
            var unpinnedl = viewModel.getunPinnedParents()
            runOnUiThread {
            if (pinnedl.size==0) {
                findViewById<RecyclerView>(R.id.pinnedNotes).visibility=View.GONE
                findViewById<TextView>(R.id.pinned).visibility=View.GONE
                findViewById<TextView>(R.id.other).visibility=View.GONE
            }   else {
                findViewById<RecyclerView>(R.id.pinnedNotes).visibility=View.VISIBLE
                findViewById<TextView>(R.id.pinned).visibility=View.VISIBLE
                findViewById<TextView>(R.id.other).visibility=View.VISIBLE
            }
            }
            for (parent in unpinnedl) {
                var notes = listOf<Note>(parent) + viewModel.getNotesOfParents(parent.id)
                unpinnedList.add(notes)
            }
            for (parent in pinnedl) {
                var notes = listOf<Note>(parent) + viewModel.getNotesOfParents(parent.id)
                pinnedList.add(notes)
            }
        }).start()
        unpinnedAdapter.list = unpinnedList
        unpinnedAdapter.notifyDataSetChanged()
        pinnedAdapter.list = pinnedList
        pinnedAdapter.notifyDataSetChanged()
    }

    override fun onClickListener(note: Note) {
        var intent = Intent(this, AddItemActivity::class.java).apply {
            putExtra(AddItemActivity.Note, note.id)
        }
        resultContract.launch(intent)
    }

}