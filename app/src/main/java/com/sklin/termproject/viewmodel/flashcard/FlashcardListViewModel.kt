package com.sklin.termproject.viewmodel.flashcard

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.sklin.termproject.dataclass.Flashcard

class FlashcardListViewModel : ViewModel() {

    private val flashcardList = generateFlashcard(20)
    private val flashcardLiveList = MutableLiveData(flashcardList)

    fun getLiveFlashcardList(): LiveData<List<Flashcard>> {
        return flashcardLiveList
    }

    fun getFlashcardList(): List<Flashcard> {
        return flashcardList
    }

    private fun generateFlashcard(num: Int): List<Flashcard> {
        var list = ArrayList<Flashcard>()
        for (i in 1..num) {
            var todo = Flashcard(i, "Question $i", "Answer $i")
            list.add(todo)
        }
        return list
    }
}