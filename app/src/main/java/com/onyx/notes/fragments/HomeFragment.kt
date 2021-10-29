package com.onyx.notes.fragments

import android.content.res.Configuration
import android.os.Bundle
import android.view.*
import android.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.onyx.notes.MainActivity
import com.onyx.notes.R
import com.onyx.notes.adapter.NoteAdapter
import com.onyx.notes.databinding.FragmentHomeBinding
import com.onyx.notes.models.NoteWithHashTags
import com.onyx.notes.viewmodel.NoteViewModel
import com.onyx.notes.viewmodel.SortTypeViewModel

class HomeFragment : Fragment(R.layout.fragment_home), SearchView.OnQueryTextListener {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var noteViewModel: NoteViewModel
    private lateinit var noteAdapter: NoteAdapter
    private lateinit var sortTypeViewModel: SortTypeViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        (activity as MainActivity).supportActionBar?.setDisplayHomeAsUpEnabled(false)
        (activity as MainActivity).supportActionBar?.setDisplayShowHomeEnabled(false)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        noteViewModel = (activity as MainActivity).noteViewModel
        sortTypeViewModel = ViewModelProvider(requireActivity()).get(SortTypeViewModel::class.java)
        setUpRecyclerView()
        binding.fabAddNote.setOnClickListener { mView ->
            mView.findNavController().navigate(R.id.action_homeFragment_to_newNoteFragment)
        }
        binding.searchView.setOnQueryTextListener(this)
    }

    private fun setUpRecyclerView() {
        noteAdapter = NoteAdapter()
        val spanCount =
            if (resources.configuration.orientation != Configuration.ORIENTATION_PORTRAIT) {
                2
            } else {
                1
            }
        binding.recyclerView.apply {
            layoutManager = StaggeredGridLayoutManager(
                spanCount,
                StaggeredGridLayoutManager.VERTICAL
            )
            setHasFixedSize(true)
            adapter = noteAdapter
        }
        activity?.let {
            searchNotes(binding.searchView.query.toString().trim())
        }
    }

    private fun updateUI(note: List<NoteWithHashTags>) {
        if (note.isNotEmpty()) {
            binding.recyclerView.visibility = View.VISIBLE
            binding.tvNoNotesAvailable.visibility = View.GONE
        } else {
            binding.recyclerView.visibility = View.GONE
            binding.tvNoNotesAvailable.visibility = View.VISIBLE
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.home_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.sort_by_date -> {
                sortTypeViewModel.setSortByDate()
            }
            R.id.sort_by_name -> {
                sortTypeViewModel.setSortByName()
            }
        }
        searchNotes(binding.searchView.query.toString())
        return super.onOptionsItemSelected(item)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onQueryTextSubmit(query: String): Boolean {
        searchNotes(query.trim())
        return true
    }

    override fun onQueryTextChange(query: String): Boolean {
        searchNotes(query.trim())
        return true
    }

    private fun searchNotes(query: String = "") {
        if (sortTypeViewModel.isSortByName()) {
            if (query.startsWith("#")) {
                noteViewModel.getNotesSortedByNameByHashtag(query.substring(1)).observe(viewLifecycleOwner, { list ->
                    noteAdapter.differ.submitList(list)
                    updateUI(list)
                })
            } else {
                noteViewModel.getNotesSortedByName(query).observe(viewLifecycleOwner, { list ->
                    noteAdapter.differ.submitList(list)
                    updateUI(list)
                })
            }
        }
        if (sortTypeViewModel.isSortByDate()) {
            if (query.startsWith("#")) {
                noteViewModel.getNotesSortedByDateByHashtag(query.substring(1)).observe(viewLifecycleOwner, { list ->
                    noteAdapter.differ.submitList(list)
                    updateUI(list)
                })
            } else {
                noteViewModel.getNotesSortedByDate(query).observe(viewLifecycleOwner, { list ->
                    noteAdapter.differ.submitList(list)
                    updateUI(list)
                })
            }
        }
    }

    override fun onResume() {
        super.onResume()
        searchNotes(binding.searchView.query.toString().trim())
    }
}