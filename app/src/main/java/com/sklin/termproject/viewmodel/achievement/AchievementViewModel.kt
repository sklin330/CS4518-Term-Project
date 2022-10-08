package com.sklin.termproject.viewmodel.achievement

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
import com.sklin.termproject.dataclass.Achievement
import com.sklin.termproject.dataclass.Flashcard
import com.sklin.termproject.dataclass.FlashcardSet
import com.sklin.termproject.dataclass.User

private const val TAG = "AchievementViewModel"

class AchievementViewModel : ViewModel() {


    private val userList = listOf<User>()

    private val userLiveList = MutableLiveData(userList)

    fun getLiveUserList(): LiveData<List<User>> {
        fetchAllUsers()
        return userLiveList
    }

    fun getUserList(): List<User> {
        return userList
    }

    private fun fetchAllUsers() {

        val firebaseDatabase = Firebase.database
        val databaseReference = firebaseDatabase.getReference("Users")

        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                var list = mutableListOf<User>()
                for (userSnapshot in dataSnapshot.children) {
                    val user = userSnapshot.getValue<User>()
                    if (user != null) {
                        Log.d(TAG, "fetchAllUsers:onDataChange -> $user")
                        list.add(user)
                    }
                }
                list.sortedWith(compareBy { it.totalPoints })
                userLiveList.postValue(list)
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.d(TAG, "fetchAllUsers:onCancelled", databaseError.toException())
            }
        }
        databaseReference.addValueEventListener(postListener)
    }

}