package com.sklin.termproject.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.sklin.termproject.AddFlashcardSetDialog
import com.sklin.termproject.EditDeleteFlashcardDialog
import com.sklin.termproject.R
import com.sklin.termproject.dataclass.Flashcard

class FlashcardAdapter (private val data: List<Flashcard>, private val flashcardSetId: String, private val flashcardSetTitle: String, private val context: Context?) :
    RecyclerView.Adapter<FlashcardAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private lateinit var flashcard: Flashcard

        private val title: TextView = view.findViewById(R.id.flashcard_title_text)
        private val card: LinearLayout = view.findViewById(R.id.card)

        fun bind(flashcard: Flashcard, flashcardSetId: String, flashcardSetTitle: String, context: Context?) {
            this.flashcard = flashcard
            title.text = this.flashcard.front

            card.setOnClickListener {
                var editDeleteFlashcardDialog =  EditDeleteFlashcardDialog()
                if (context != null) {
                    editDeleteFlashcardDialog.showDialog(context, flashcard, flashcardSetId, flashcardSetTitle)
                }
                editDeleteFlashcardDialog.getWindow()?.setBackgroundDrawableResource(R.drawable.white_card_background)
            }

        }

    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.card_flashcard, viewGroup, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        val flashcard = data[position]
        viewHolder.bind(flashcard, flashcardSetId, flashcardSetTitle, context)
    }

    override fun getItemCount() = data.size
}
