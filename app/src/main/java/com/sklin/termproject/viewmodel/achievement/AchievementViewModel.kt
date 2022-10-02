package com.sklin.termproject.viewmodel.achievement

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.sklin.termproject.dataclass.Achievement
import com.sklin.termproject.dataclass.Flashcard
import com.sklin.termproject.dataclass.User

class AchievementViewModel : ViewModel() {

    private val achievementList = listOf(
        Achievement("0", "1 Day Login", "Login for 1 day!", 10, true),
        Achievement("1", "2 Day Login", "Login for 2 days!", 10, true),
        Achievement("2", "5 Day Login", "Login for 5 days!", 10, false),
        Achievement("3", "7 Day Login", "Login for 7 days!", 20, false),
        Achievement("4", "Create 1 Flashcard Set", "Create a new flashcard set", 10, true),
    )
    private val achievementLiveList = MutableLiveData(achievementList)

    private val userList = listOf(
        User("0", "Smera Gora",  30),
        User("1", "Shannen Lin",  20),
        User("2", "Frank McShan",  10),
    )
    private val userLiveList = MutableLiveData(userList)

    fun getLiveAchievementList(): LiveData<List<Achievement>> {
        return achievementLiveList
    }

    fun getAchievementList(): List<Achievement> {
        return achievementList
    }

    fun getLiveUserList(): LiveData<List<User>> {
        return userLiveList
    }

    fun getUserList(): List<User> {
        return userList
    }

}