package com.onyx.notes.fragments

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.google.android.material.snackbar.Snackbar
import com.onyx.notes.MainActivity
import com.onyx.notes.R
import com.onyx.notes.databinding.FragmentNewNoteBinding
import com.onyx.notes.models.Hashtag
import com.onyx.notes.models.Note
import com.onyx.notes.viewmodel.NoteViewModel
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class NewNoteFragment : Fragment(R.layout.fragment_new_note) {

    private var _binding: FragmentNewNoteBinding? = null
    private val binding get() = _binding!!
    private lateinit var noteViewModel: NoteViewModel
    private lateinit var mView: View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setHasOptionsMenu(true)
        (activity as MainActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)
        (activity as MainActivity).supportActionBar?.setDisplayShowHomeEnabled(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentNewNoteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        noteViewModel = (activity as MainActivity).noteViewModel
        mView = view
    }

    private fun saveNote(view: View) {
        var noteTitle = binding.etNoteTitle.text.toString().trim()
        val noteHashtags = binding.etNoteHashtags.text.toString().trim()
        val noteBody = binding.etNoteBody.text.toString().trim()
        if (noteTitle.isEmpty()) {
            val calendar = Calendar.getInstance(TimeZone.getDefault())
            val formatter = SimpleDateFormat("HH:mm:ss dd.MM.yyyy")
            noteTitle = formatter.format(calendar.time).toString()
        }
        val note = Note(0,noteTitle,noteBody, Date())
        val hashtags: ArrayList<Hashtag> = ArrayList()
        if (noteHashtags != "") {
            val hashtagsString: List<String> = noteHashtags.split(",").map { it.trim() }
            hashtagsString.forEach { hashtags.add(Hashtag(0,0,it)) }
        }
        noteViewModel.addNote(note, hashtags)
        Snackbar.make(view, "Note saved", Snackbar.LENGTH_SHORT).show()

        view.findNavController().navigate(R.id.action_newNoteFragment_to_homeFragment)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.save_menu -> {
                saveNote(mView)
            }
            android.R.id.home -> {
                view?.findNavController()?.navigate(R.id.action_newNoteFragment_to_homeFragment)
            }
        }
        return super.onOptionsItemSelected(item)
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        menu.clear()
        inflater.inflate(R.menu.new_note_menu, menu)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}