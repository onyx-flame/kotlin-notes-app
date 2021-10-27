package com.onyx.notes.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.onyx.notes.R
import com.onyx.notes.databinding.NoteLayoutAdapterBinding
import com.onyx.notes.fragments.HomeFragmentDirections
import com.onyx.notes.models.Note
import com.onyx.notes.models.NoteWithHashTags

class NoteAdapter: RecyclerView.Adapter<NoteAdapter.NoteViewHolder>() {

    class NoteViewHolder(val itemBinding: NoteLayoutAdapterBinding): RecyclerView.ViewHolder(itemBinding.root)

    private val differCallback =
        object : DiffUtil.ItemCallback<NoteWithHashTags>() {
            override fun areItemsTheSame(oldItem: NoteWithHashTags, newItem: NoteWithHashTags): Boolean {
                return oldItem.note.id == newItem.note.id
            }

            override fun areContentsTheSame(oldItem: NoteWithHashTags, newItem: NoteWithHashTags): Boolean {
                return oldItem == newItem
            }
        }


    val differ = AsyncListDiffer(this, differCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        return NoteViewHolder(
            NoteLayoutAdapterBinding.inflate(LayoutInflater.from(parent.context),parent, false)
        )
    }

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        val currentNote = differ.currentList[position]
        holder.itemBinding.tvNoteTitle.text = currentNote.note.noteTitle
        holder.itemBinding.tvNoteBody.text = currentNote.note.noteBody

        val random = java.util.Random()
        val color = Color.argb(
            255,
            random.nextInt(256),
            random.nextInt(256),
            random.nextInt(256)
        )
        holder.itemBinding.viewColor.setBackgroundColor(color)

        holder.itemView.setOnClickListener { view ->
            val direction =
                HomeFragmentDirections.actionHomeFragmentToUpdateNoteFragment(currentNote)
            view.findNavController().navigate(direction)
        }
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }
}