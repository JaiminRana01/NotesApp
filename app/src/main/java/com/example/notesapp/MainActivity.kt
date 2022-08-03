package com.example.notesapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), INoteRVAdapter {

    private lateinit var viewModel: NoteViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recyclerView.layoutManager = LinearLayoutManager(this)
        val adapter = NoteRVAdapter(this)
        recyclerView.adapter = adapter

        val noteDao = NoteDatabase.getDatabase(application).getDao()
        val repository = NoteRepository(noteDao)

        val viewModelFactory = NoteViewModelFactory(repository)
        viewModel = ViewModelProvider(this, viewModelFactory).get(NoteViewModel::class.java)

        viewModel.allNotes.observe(this) { list ->
            list?.let {
                adapter.updateNote(it)
            }
        }

        addButton.setOnClickListener {
            val text = input.text.toString()
            if (text.isNotEmpty()) {
                viewModel.insertNote(Note(text))
            }
        }
    }

    override fun onDeleteClicked(note: Note) {
        viewModel.deleteNote(note)
    }
}