package com.sklin.termproject.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.sklin.termproject.*
import com.sklin.termproject.dataclass.FlashcardSet

const val EXTRA_TITLE = "com.sklin.termproject.flashcard_set_title"
const val EXTRA_SET_ID = "com.sklin.termproject.flashcard_set_id"

class FlashcardSetAdapter (private val data: List<FlashcardSet>, private val context: Context?) :
    RecyclerView.Adapter<FlashcardSetAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private lateinit var flashcardSet: FlashcardSet

        private val title: TextView = view.findViewById(R.id.flashcard_title_text)
        private val card: LinearLayout = view.findViewById(R.id.card)

        fun bind(flashcardSet: FlashcardSet, context: Context?) {
            this.flashcardSet = flashcardSet
            title.text = this.flashcardSet.title
            card.setOnClickListener {
                var editDeleteFlashcardSetDialog =  EditDeleteFlashcardSetDialog()
                if (context != null && flashcardSet != null) {
                    editDeleteFlashcardSetDialog.showDialog(context, flashcardSet.title.toString(),
                        flashcardSet.id.toString()
                    )
                }
                editDeleteFlashcardSetDialog.getWindow()?.setBackgroundDrawableResource(R.drawable.white_card_background)
            }
        }

    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.card_flashcard, viewGroup, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        val flashcardSet = data[position]
        viewHolder.bind(flashcardSet, context)
    }

    override fun getItemCount() = data.size

}