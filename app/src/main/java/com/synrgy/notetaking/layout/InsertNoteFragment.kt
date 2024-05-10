package com.synrgy.notetaking.layout

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.synrgy.notetaking.R
import com.synrgy.notetaking.databinding.FragmentInsertNoteBinding

class InsertNoteFragment : Fragment() {

    private var _fragmentInsertNoteBinding: FragmentInsertNoteBinding? = null
    private val fragmentInsertNoteBinding get() = _fragmentInsertNoteBinding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _fragmentInsertNoteBinding = FragmentInsertNoteBinding.inflate(inflater, container,false)
        return fragmentInsertNoteBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fragmentInsertNoteBinding.btnInsert.setOnClickListener {

        }
    }
}