package com.synrgy.notetaking

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.synrgy.notetaking.data.database.Note
import com.synrgy.notetaking.databinding.ActivityMainBinding
import com.synrgy.notetaking.databinding.CustomDialogViewBinding

class MainActivity : AppCompatActivity(), ListAdapter.OnNoteItemClickListener {

    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: ListAdapter
    private val viewModel by viewModels<NoteViewModel> {
        ViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        binding.create.setOnClickListener {
            actionAddNote()
        }

        checkSession()
        loadNoteList()
    }

    private fun checkSession() {
        viewModel.checkSession().observe(this) { user ->
            if ((user.username == "") && (user.password == "")) {
                startActivity(Intent(this, WelcomeActivity::class.java))
                finish()
            } else {
                supportActionBar?.apply {
                    title = "Hi, ${user.username}!"
                }
            }
        }
    }

    @SuppressLint("InflateParams", "SetTextI18n")
    private fun actionAddNote() {
        val dialogBinding: CustomDialogViewBinding = CustomDialogViewBinding.inflate(layoutInflater)
        dialogBinding.dialogTitle.text = getString(R.string.insert)
        dialogBinding.dialogMessage.visibility = View.GONE
        val dialogView = dialogBinding.root
        val dialogBuilder = MaterialAlertDialogBuilder(this)
        dialogBuilder.setView(dialogView)
        val dialog = dialogBuilder.show()

        dialogBinding.btnInsert.apply {
            text = getString(R.string.insert)
            setOnClickListener {
                val noteTitle = dialogBinding.etNoteTitle.text.toString()
                val noteContent = dialogBinding.etNoteContent.text.toString()

                viewModel.insertNote(Note(title = noteTitle, content = noteContent))
                    .observe(this@MainActivity) {
                        when (it) {
                            is State.Success -> {
                                Toast.makeText(
                                    this@MainActivity,
                                    "Note Inserted",
                                    Toast.LENGTH_SHORT
                                ).show()
                                dialog.dismiss()
                            }
                            is State.Error -> Toast.makeText(
                                this@MainActivity,
                                it.error,
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
            }
        }

    }

    private fun loadNoteList() {
        binding.rvNotes.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            setHasFixedSize(true)
        }
        adapter = ListAdapter(this)
        binding.rvNotes.adapter = adapter
        viewModel.getAllNotes().observe(this@MainActivity) { notes ->
            if (notes.isEmpty()) {
                binding.tvNoteList.visibility = View.GONE
                binding.rvNotes.visibility = View.GONE
                binding.clEmptyList.visibility = View.VISIBLE
            } else {
                binding.rvNotes.visibility = View.VISIBLE
                binding.tvNoteList.visibility = View.VISIBLE
                binding.clEmptyList.visibility = View.GONE
                adapter.submitList(notes)
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.logout -> {
                viewModel.clearSession()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onEditClick(note: Note) {
        val dialogBinding: CustomDialogViewBinding = CustomDialogViewBinding.inflate(layoutInflater)
        dialogBinding.apply {
            dialogTitle.text = getString(R.string.update_note)
            dialogMessage.visibility = View.GONE
            etNoteTitle.setText(note.title)
            etNoteContent.setText(note.content)
        }
        val dialogView = dialogBinding.root
        val dialogBuilder = MaterialAlertDialogBuilder(this)
        dialogBuilder.setView(dialogView)
        val dialog = dialogBuilder.show()

        dialogBinding.btnInsert.apply {
            text = getString(R.string.update_note)
            setOnClickListener {
                val noteTitle = dialogBinding.etNoteTitle.text.toString()
                val noteContent = dialogBinding.etNoteContent.text.toString()
                viewModel.updateNote(Note(id = note.id, title = noteTitle, content = noteContent))
                    .observe(this@MainActivity) {
                        when (it) {
                            is State.Success -> {
                                Toast.makeText(
                                    this@MainActivity,
                                    "Note Updated",
                                    Toast.LENGTH_SHORT
                                ).show()
                                dialog.dismiss()
                            }
                            is State.Error -> Toast.makeText(
                                this@MainActivity,
                                it.error,
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
            }
        }

    }

    override fun onDeleteClick(note: Note) {
        val dialogBinding: CustomDialogViewBinding = CustomDialogViewBinding.inflate(layoutInflater)
        dialogBinding.dialogTitle.text = getString(R.string.delete_note)
        dialogBinding.dialogMessage.text = getString(R.string.dialog_delete_mssg)
        dialogBinding.btnInsert.visibility =View.GONE
        dialogBinding.noteContent.visibility = View.GONE
        dialogBinding.noteTitle.visibility = View.GONE
        val dialogView = dialogBinding.root

        val dialogBuilder = MaterialAlertDialogBuilder(this)
        dialogBuilder.apply {
            setView(dialogView)
            setPositiveButton("Delete") { dialog, _ ->
                viewModel.deleteNote(note)
                dialog.dismiss()
            }
            setNegativeButton("Cancel") { dialog, _ ->
                dialog.dismiss()
            }
            show()
        }
    }
}