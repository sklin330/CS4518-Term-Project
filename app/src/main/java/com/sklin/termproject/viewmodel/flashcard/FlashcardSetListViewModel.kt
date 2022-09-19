package com.sklin.termproject.viewmodel.flashcard

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.sklin.termproject.dataclass.FlashcardSet

class FlashcardSetListViewModel : ViewModel() {

    private val flashcardSetList = generateFlashcard(20)
    private val flashcardSetLiveList = MutableLiveData(flashcardSetList)

    fun getLiveFlashcardList(): LiveData<List<FlashcardSet>> {
        return flashcardSetLiveList
    }

    fun getFlashcardList(): List<FlashcardSet> {
        return flashcardSetList
    }

    private fun generateFlashcard(num: Int): List<FlashcardSet> {
        var list = ArrayList<FlashcardSet>()
        for (i in 1..num) {
            var todo = FlashcardSet(i, "Flashcard Set $i")
            list.add(todo)
        }
        return list
    }
}