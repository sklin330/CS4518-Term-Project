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
import com.sklin.termproject.dataclass.FlashcardSet

private const val TAG = "FlashcardListViewModel"

class FlashcardListViewModel : ViewModel() {

    private val flashcardList = listOf<Flashcard>()
    private val flashcardLiveList = MutableLiveData(flashcardList)
    private var flashcardSetId = "-1"

    fun getLiveFlashcardList(): LiveData<List<Flashcard>> {
        fetchFlashcards()
        return flashcardLiveList
    }

    fun getFlashcardList(): List<Flashcard> {
        return flashcardList
    }

    fun setFlashcardSetId(id: String) {
        this.flashcardSetId = id
    }

    private fun fetchFlashcards() {
        val firebaseDatabase = Firebase.database
        val databaseReference = firebaseDatabase.getReference("Flashcard").child(flashcardSetId)

        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                var list = mutableListOf<Flashcard>()
                for (flashcardSnapshot in dataSnapshot.children) {
                    val flashcard = flashcardSnapshot.getValue<Flashcard>()
                    if (flashcard != null) {
                        Log.d(TAG, "fetchFlashcards:onDataChange -> $flashcardSnapshot")
                        list.add(flashcard)
                    }
                }
                flashcardLiveList.postValue(list)
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.d(TAG, "fetchFlashcardSets:onCancelled", databaseError.toException())
            }
        }
        databaseReference.addValueEventListener(postListener)
    }

    fun persistFlashcard(id: String? = "", front: String, back: String, audioPath: String) {
        val firebaseDatabase = Firebase.database
        val databaseReference = firebaseDatabase.reference
        var newID = ""
        if(id.equals("")) {
            newID = databaseReference.push().key ?: ""
        } else {
            if (id != null) {
                newID = id
            }
        }

        Log.d(TAG, "flashcardID -> $newID")
        val newFlashcard = Flashcard(newID, front, back, audioPath)

        databaseReference.child("Flashcard").child(flashcardSetId).child(newID)
            .setValue(newFlashcard)
    }
}