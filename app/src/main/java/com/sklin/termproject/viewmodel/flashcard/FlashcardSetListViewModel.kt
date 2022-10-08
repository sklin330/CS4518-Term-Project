package com.sklin.termproject.viewmodel.flashcard

import android.util.Log
import androidx.core.app.ActivityCompat.startActivityForResult
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import com.sklin.termproject.LoginActivity
import com.sklin.termproject.dataclass.FlashcardSet

private const val TAG = "FlashcardSetListViewModel"

class FlashcardSetListViewModel : ViewModel() {

    private val flashcardSetList = listOf<FlashcardSet>()
    private val flashcardSetLiveList = MutableLiveData(flashcardSetList)

    fun getLiveFlashcardSetList(): LiveData<List<FlashcardSet>> {
        fetchFlashcardSets()
        return flashcardSetLiveList
    }

    fun getFlashcardSetList(): List<FlashcardSet> {
        return flashcardSetList
    }

    private fun fetchFlashcardSets() {
        val userid = Firebase.auth.currentUser?.uid

        val firebaseDatabase = Firebase.database
        val databaseReference = userid?.let { firebaseDatabase.getReference("FlashcardSet").child(it) }

        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                var list = mutableListOf<FlashcardSet>()
                for (flashcardSetSnapshot in dataSnapshot.children) {
                    val flashcardSet = flashcardSetSnapshot.getValue<FlashcardSet>()
                    if (flashcardSet != null) {
                        Log.d(TAG, "fetchFlashcardSets:onDataChange -> $flashcardSetSnapshot")
                        list.add(flashcardSet)
                    }
                }
                flashcardSetLiveList.postValue(list)
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.d(TAG, "fetchFlashcardSets:onCancelled", databaseError.toException())
            }
        }
        if (databaseReference != null) {
            databaseReference.addValueEventListener(postListener)
        }
    }
}