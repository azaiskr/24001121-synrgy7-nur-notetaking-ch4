package com.synrgy.notetaking

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.synrgy.notetaking.data.database.Note
import com.synrgy.notetaking.databinding.NoteItemBinding

class ListAdapter (private val listener: OnNoteItemClickListener): ListAdapter<Note, com.synrgy.notetaking.ListAdapter.ViewHolder>(DIFF_CALLBACK) {

    interface OnNoteItemClickListener{
        fun onEditClick(note: Note)
        fun onDeleteClick(note: Note)
    }

    class ViewHolder(private val binding : NoteItemBinding) :
    RecyclerView.ViewHolder(binding.root) {
        fun bind(note: Note, listener: OnNoteItemClickListener) {
            binding.tvNoteTitle.text = note.title
            binding.tvNoteContent.text = note.content
            binding.btnDelete.setOnClickListener {
                listener.onDeleteClick(note)
            }

            binding.btnEdit.setOnClickListener {
                listener.onEditClick(note)
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = NoteItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val note = getItem(position)
        note?.let {
            holder.bind(note, listener)
        }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Note>() {
            override fun areItemsTheSame(
                oldItem: Note,
                newItem: Note
            ): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(
                oldItem: Note,
                newItem: Note
            ): Boolean {
                return oldItem == newItem
            }
        }
    }
}