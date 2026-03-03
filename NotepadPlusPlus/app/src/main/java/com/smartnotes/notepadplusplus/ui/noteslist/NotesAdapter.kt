package com.smartnotes.notepadplusplus.ui.noteslist

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.smartnotes.notepadplusplus.data.database.NoteEntity
import com.smartnotes.notepadplusplus.databinding.ItemNoteBinding
import com.smartnotes.notepadplusplus.databinding.ItemNoteGridBinding
import com.smartnotes.notepadplusplus.utils.DateUtils
import com.smartnotes.notepadplusplus.utils.RichTextUtils

class NotesAdapter(
    private var isGrid: Boolean = false,
    private val onClick: (NoteEntity) -> Unit,
    private val onLongClick: (NoteEntity, View) -> Unit
) : ListAdapter<NoteEntity, RecyclerView.ViewHolder>(DiffCB()) {

    companion object {
        private const val TYPE_LIST = 0
        private const val TYPE_GRID = 1
    }

    fun setGridMode(grid: Boolean) {
        if (isGrid == grid) return
        isGrid = grid
        notifyItemRangeChanged(0, itemCount)
    }

    override fun getItemViewType(position: Int) = if (isGrid) TYPE_GRID else TYPE_LIST

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == TYPE_GRID) {
            GridVH(ItemNoteGridBinding.inflate(LayoutInflater.from(parent.context), parent, false))
        } else {
            ListVH(ItemNoteBinding.inflate(LayoutInflater.from(parent.context), parent, false))
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val note = getItem(position)
        when (holder) {
            is ListVH -> holder.bind(note)
            is GridVH -> holder.bind(note)
        }
    }

    inner class ListVH(private val b: ItemNoteBinding) : RecyclerView.ViewHolder(b.root) {
        fun bind(note: NoteEntity) {
            b.tvTitle.text = note.title.ifBlank { "Untitled" }
            b.tvContent.text = note.content.ifBlank { "No content" }
            b.tvDate.text = DateUtils.formatDate(note.updatedAt)
            b.tvWordCount.text = if (note.wordCount > 0)
                "${note.wordCount} words · ${RichTextUtils.estimateReadTime(note.wordCount)}"
            else ""
            b.ivPin.visibility = if (note.isPinned) View.VISIBLE else View.GONE
            b.ivLock.visibility = if (note.isLocked) View.VISIBLE else View.GONE
            b.ivReminder.visibility = if (note.reminderTime > System.currentTimeMillis()) View.VISIBLE else View.GONE

            if (note.label.isNotBlank()) {
                b.tvLabel.text = note.label
                b.tvLabel.visibility = View.VISIBLE
            } else {
                b.tvLabel.visibility = View.GONE
            }

            // Safe color parsing
            val cardColor = try { Color.parseColor(note.color) } catch (e: Exception) { Color.WHITE }
            b.cardView.setCardBackgroundColor(cardColor)

            b.root.setOnClickListener { onClick(note) }
            b.root.setOnLongClickListener { onLongClick(note, it); true }
        }
    }

    inner class GridVH(private val b: ItemNoteGridBinding) : RecyclerView.ViewHolder(b.root) {
        fun bind(note: NoteEntity) {
            b.tvTitle.text = note.title.ifBlank { "Untitled" }
            b.tvContent.text = note.content.ifBlank { "" }
            b.tvDate.text = DateUtils.formatDate(note.updatedAt)
            b.ivPin.visibility = if (note.isPinned) View.VISIBLE else View.GONE
            b.ivLock.visibility = if (note.isLocked) View.VISIBLE else View.GONE

            val cardColor = try { Color.parseColor(note.color) } catch (e: Exception) { Color.WHITE }
            b.cardView.setCardBackgroundColor(cardColor)

            b.root.setOnClickListener { onClick(note) }
            b.root.setOnLongClickListener { onLongClick(note, it); true }
        }
    }

    class DiffCB : DiffUtil.ItemCallback<NoteEntity>() {
        override fun areItemsTheSame(a: NoteEntity, b: NoteEntity) = a.id == b.id
        override fun areContentsTheSame(a: NoteEntity, b: NoteEntity) = a == b
    }
}
