package com.sklin.termproject.viewmodel.achievement

import android.content.Context
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import com.sklin.termproject.dataclass.Achievement
import com.sklin.termproject.dataclass.User
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import java.util.*


private const val TAG = "AchievementSource"

class AchievementSource private constructor(
    context: Context,
) {

    private lateinit var user: User

    private var lastLoggedIn = "2022-01-01"
    private var numDaysLoggedIn = 0
    private var numFlashcardSetCreated = 0
    private var numFlashcardCreated = 0
    private var numFlashcardPracticed = 0

    private val achievementList = listOf(
        Achievement("0", "1 Day Login", "Login for 1 day!", 10, 1),
        Achievement("1", "2 Day Login", "Login for 2 days!", 10, 2),
        Achievement("2", "5 Day Login", "Login for 5 days!", 20, 5),
        Achievement("3", "7 Day Login", "Login for 7 days!", 30, 7),
        Achievement("4", "Create 1 Flashcard Set", "Create a flashcard set", 10, 1),
        Achievement("5", "Create 5 Flashcard Sets", "Create 5 flashcard sets", 15, 5),
        Achievement("6", "Create 1 Flashcard", "Create a flashcard", 10, 1),
        Achievement("7", "Create 5 Flashcards", "Create 5 flashcards", 15, 5),
        Achievement("8", "Practice 10 Flashcards", "Practice flashcards 10 times", 10, 10),
    )

    private var achievementMap: HashMap<String, Boolean> = HashMap()
    private val achievementLiveMap = MutableLiveData(achievementMap)

    init {
        //TODO: Retrieve User ID
        val userId = "1"
        fetchUserStats(userId)
        fetchAchievements(userId)
    }

    fun getLiveAchievementMap(): LiveData<HashMap<String, Boolean>> {
        return achievementLiveMap
    }

    fun getAchievementList(): List<Achievement> {
        return achievementList
    }

    private fun fetchAchievements(userId: String) {
        val firebaseDatabase = Firebase.database

        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                var map = dataSnapshot.getValue<HashMap<String, Boolean>>()
                if (map == null) {
                    map = initAchievementsMap()
                }
                achievementLiveMap.postValue(map)
            }
            override fun onCancelled(databaseError: DatabaseError) {
                Log.d(TAG, "fetchAchievements:onCancelled", databaseError.toException())
            }
        }
        firebaseDatabase.getReference("Achievements").child(userId).addValueEventListener(postListener)
    }

    private fun fetchUserStats(userId: String) {
        val firebaseDatabase = Firebase.database

        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val userRes = dataSnapshot.getValue<User>()
                if (userRes != null) {
                    user = userRes
                } else {
                    user = User(userId, "USERNAME")
                }
            }
            override fun onCancelled(databaseError: DatabaseError) {
                Log.d(TAG, "fetchUserStats:onCancelled", databaseError.toException())
            }
        }
        firebaseDatabase.getReference("Users").child(userId).addValueEventListener(postListener)
    }

    fun initAchievementsMap(): HashMap<String, Boolean> {
        var map: HashMap<String, Boolean> = HashMap()
        for(i in achievementList.indices) {
            map[i.toString()] = false
        }
        return map
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun checkLogin() {
        val format = DateTimeFormatter.ofPattern("yyyy-MM-dd")

        val pastDate = LocalDate.parse(lastLoggedIn, format)
        val currDate = LocalDate.parse(LocalDate.now().toString(), format)

        val days = ChronoUnit.DAYS.between(pastDate, currDate)

        if(days > 0) {
            numDaysLoggedIn += 1
            updateStats()
            updateAchievements()
        }
    }

    fun updateAchievements() {
        val currAchievementMap = achievementLiveMap.value

        if (currAchievementMap != null) {
            currAchievementMap.put(achievementList[0].id, numDaysLoggedIn >= achievementList[0].goalValue)
            currAchievementMap.put(achievementList[1].id, numDaysLoggedIn >= achievementList[1].goalValue)
            currAchievementMap.put(achievementList[2].id, numDaysLoggedIn >= achievementList[2].goalValue)
            currAchievementMap.put(achievementList[3].id, numDaysLoggedIn >= achievementList[3].goalValue)
            currAchievementMap.put(achievementList[4].id, numFlashcardSetCreated >= achievementList[4].goalValue)
            currAchievementMap.put(achievementList[5].id, numFlashcardSetCreated >= achievementList[5].goalValue)
            currAchievementMap.put(achievementList[6].id, numFlashcardCreated >= achievementList[6].goalValue)
            currAchievementMap.put(achievementList[7].id, numFlashcardCreated >= achievementList[7].goalValue)
            currAchievementMap.put(achievementList[8].id, numFlashcardPracticed >= achievementList[8].goalValue)
        }

        val firebaseDatabase = Firebase.database
        user.id?.let { firebaseDatabase.getReference("Achievements").child(it).setValue(currAchievementMap) }
    }

    fun updateStats() {
        val firebaseDatabase = Firebase.database

        //TODO: how to get username of user
        val updatedUser = User(
            user.id,
            "username",
            user.totalPoints,
            user.lastLoggedInDate,
            user.numDaysLoggedIn,
            user.numFlashcardSetCreated,
            user.numFlashcardCreated,
            user.numFlashcardPracticed
        )

        user.id?.let { firebaseDatabase.getReference("Users").child(it) }?.setValue(updatedUser)
    }

    companion object {
        @Volatile
        private var INSTANCE: AchievementSource? = null

        fun initialize(context: Context) {
            if (INSTANCE == null) {
                INSTANCE = AchievementSource(context)
            }
        }

        fun getDataSource(): AchievementSource {
            return INSTANCE
                ?: throw IllegalStateException("AchievementSource must be initialized")
        }
    }
}