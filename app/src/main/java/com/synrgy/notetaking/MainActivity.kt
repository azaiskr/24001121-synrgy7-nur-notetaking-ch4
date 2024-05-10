package com.synrgy.notetaking

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.synrgy.notetaking.databinding.ActivityMainBinding
import kotlinx.coroutines.delay

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter : ListAdapter
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

        checkSession()
        actionAddNote()
        loadNoteList()
    }

    private fun checkSession() {
        viewModel.checkSession().observe(this) { user ->
            if ((user.username == "") && (user.password == "")) {
                startActivity(Intent(this, WelcomeActivity::class.java))
                finish()
            } else{
                supportActionBar?.apply {
                    title = "Hi, ${user.username}!"
                }
            }
        }
    }

    @SuppressLint("InflateParams")
    private fun actionAddNote() {
        binding.create.setOnClickListener {
            MaterialAlertDialogBuilder(this)
                .setTitle("Input Data")
                //TODO: replace with fragments
                .setView(layoutInflater.inflate(R.layout.fragment_insert_note, null, false))
                .show()
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
        viewModel.getAllNotes().observe(this) { note ->
            if (note.isNotEmpty()) {
                adapter.submitList(note)
            } else {
                binding.clEmptyList.visibility = View.VISIBLE
                binding.tvNoteList.visibility = View.GONE
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