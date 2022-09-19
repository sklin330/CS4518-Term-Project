package com.sklin.termproject.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.sklin.termproject.R
import com.sklin.termproject.dataclass.Flashcard

class FlashcardAdapter (private val data: List<Flashcard>) :
    RecyclerView.Adapter<FlashcardAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private lateinit var flashcard: Flashcard

        private val title: TextView = view.findViewById(R.id.flashcard_title_text)
        private val card: LinearLayout = view.findViewById(R.id.card)

        fun bind(flashcard: Flashcard) {
            this.flashcard = flashcard
            title.text = this.flashcard.front

        }

    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.card_flashcard, viewGroup, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        val flashcard = data[position]
        viewHolder.bind(flashcard)
    }

    override fun getItemCount() = data.size
}