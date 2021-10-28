package com.onyx.notes.fragments

import android.app.AlertDialog
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import com.onyx.notes.MainActivity
import com.onyx.notes.R
import com.onyx.notes.databinding.FragmentUpdateNoteBinding
import com.onyx.notes.models.Hashtag
import com.onyx.notes.models.Note
import com.onyx.notes.models.NoteWithHashTags
import com.onyx.notes.viewmodel.NoteViewModel
import java.text.SimpleDateFormat
import java.util.*

class UpdateNoteFragment : Fragment(R.layout.fragment_update_note) {

    private var _binding: FragmentUpdateNoteBinding? = null
    private val binding get() = _binding!!
    private lateinit var noteViewModel: NoteViewModel
    private val args: UpdateNoteFragmentArgs by navArgs()
    private lateinit var currentNote: NoteWithHashTags

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
        _binding = FragmentUpdateNoteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        noteViewModel = (activity as MainActivity).noteViewModel

        currentNote = args.note!!
        binding.etNoteTitleUpdate.setText(currentNote.note.noteTitle)
        binding.etNoteBodyUpdate.setText(currentNote.note.noteBody)
        binding.etNoteHashtagsUpdate.setText(currentNote.hashtags.joinToString(",") {it.text})

        binding.fabUpdate.setOnClickListener {
            var noteTitle = binding.etNoteTitleUpdate.text.toString().trim()
            val noteHashtags = binding.etNoteHashtagsUpdate.text.toString().trim()
            val noteBody = binding.etNoteBodyUpdate.text.toString().trim()
            if (noteTitle.isEmpty()) {
                val calendar = Calendar.getInstance(TimeZone.getDefault())
                val formatter = SimpleDateFormat("HH:mm:ss dd.MM.yyyy")
                noteTitle = formatter.format(calendar.time).toString()
            }
            val note = Note(currentNote.note.id, noteTitle, noteBody, Date())

            val hashtags: ArrayList<Hashtag> = ArrayList()
            if (noteHashtags != "") {
                val hashtagsString: List<String> = noteHashtags.split(",").map { it.trim() }
                hashtagsString.forEach { hashtags.add(Hashtag(0,currentNote.note.id,it)) }
            }
            noteViewModel.updateNote(note, hashtags)


            view.findNavController().navigate(R.id.action_updateNoteFragment_to_homeFragment)
        }
    }

    private fun deleteNote() {
        AlertDialog.Builder(activity).apply {
            setTitle("Delete Note")
            setMessage("Are you sure to delete this note?")
            setPositiveButton("Delete") { _,_ ->
                noteViewModel.deleteNote(currentNote.note)
                view?.findNavController()?.navigate(R.id.action_updateNoteFragment_to_homeFragment)
            }
            setNegativeButton("Cancel", null)
        }.create().show()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        menu.clear()
        inflater.inflate(R.menu.update_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.delete_menu -> {
                deleteNote()
            }
            android.R.id.home -> {
                view?.findNavController()?.navigate(R.id.action_updateNoteFragment_to_homeFragment)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}