package com.sklin.termproject.viewmodel.achievement

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.sklin.termproject.dataclass.Achievement
import com.sklin.termproject.dataclass.Flashcard
import com.sklin.termproject.dataclass.User

class AchievementViewModel : ViewModel() {


    private val userList = listOf(
        User("0", "Smera Gora",  30),
        User("1", "Shannen Lin",  20),
        User("2", "Frank McShan",  10),
    )

    private val userLiveList = MutableLiveData(userList)

    fun getLiveUserList(): LiveData<List<User>> {
        return userLiveList
    }

    fun getUserList(): List<User> {
        return userList
    }

}