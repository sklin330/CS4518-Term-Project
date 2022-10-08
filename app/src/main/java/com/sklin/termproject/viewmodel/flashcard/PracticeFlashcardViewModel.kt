package com.sklin.termproject.viewmodel.flashcard

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import com.sklin.termproject.dataclass.Flashcard

class PracticeFlashcardViewModel : ViewModel() {

    private var flashcardList = listOf<Flashcard>()
    private var flashcardSetId = "-1"
    private var index = 0
    private var isFront = true

    fun setIndex(index: Int) {
        this.index = index
    }

    fun getIndex(): Int {
        return index
    }

    fun setIsFront(isFront: Boolean) {
        this.isFront = isFront
    }

    fun getIsFront(): Boolean {
        return isFront
    }

    fun setFlashcardList(flashcardList: List<Flashcard>) {
        this.flashcardList = flashcardList
    }

    fun getFlashcardList(): List<Flashcard> {
        return flashcardList
    }

    fun setFlashcardSetId(id: String) {
        this.flashcardSetId = id
    }


}