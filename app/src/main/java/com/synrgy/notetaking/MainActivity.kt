package com.synrgy.notetaking

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.observe
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.synrgy.notetaking.data.database.Note
import com.synrgy.notetaking.databinding.ActivityMainBinding
import com.synrgy.notetaking.databinding.CustomDialogViewBinding

class MainActivity : AppCompatActivity() {

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
        val dialogView = dialogBinding.root
        val dialogBuilder = MaterialAlertDialogBuilder(this)
        dialogBuilder.setView(dialogView).show()
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
        //TODO: load note acsess view models
        adapter = ListAdapter()
        binding.rvNotes.adapter = adapter
        viewModel.getAllNotes().observe(this@MainActivity){notes ->
            if (notes.isEmpty()){
                binding.tvNoteList.visibility = View.GONE
                binding.clEmptyList.visibility = View.VISIBLE
            } else {
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
}