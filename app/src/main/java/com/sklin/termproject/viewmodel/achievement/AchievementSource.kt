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
import kotlin.collections.HashMap


private const val TAG = "AchievementSource"

@RequiresApi(Build.VERSION_CODES.O)
class AchievementSource private constructor(
    context: Context,
    userId: String
) {

    private var userId: String = userId
    private var username: String = ""

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
        Achievement("4", "Create 1 Flashcard Set", "Create a flashcard set!", 10, 1),
        Achievement("5", "Create 5 Flashcard Sets", "Create 5 flashcard sets!", 15, 5),
        Achievement("6", "Create 1 Flashcard", "Create a flashcard!", 10, 1),
        Achievement("7", "Create 5 Flashcards", "Create 5 flashcards!", 15, 5),
        Achievement("8", "Practice 10 Flashcards", "Practice flashcards 10 times!", 10, 10),
    )

    init {
        val firebaseDatabase = Firebase.database

        firebaseDatabase.getReference("Users").child(userId).get().addOnSuccessListener {
            Log.i(TAG, "Got user ${it.value}")
            checkLogin()
            updateStatsAndAchievements()
        }.addOnFailureListener{
            Log.e(TAG, "Error getting data", it)
        }

        fetchAchievements()

    }

    private var achievementMap: HashMap<String, Boolean> = HashMap()
    private val achievementLiveMap = MutableLiveData(achievementMap)

    private var userData: User = User(userId)
    private val userLiveData = MutableLiveData(userData)

    fun getLiveAchievementMap(): LiveData<HashMap<String, Boolean>> {
        fetchAchievements()
        return achievementLiveMap
    }

    fun getAchievementList(): List<Achievement> {
        return achievementList
    }

    fun fetchAchievements() {
        val firebaseDatabase = Firebase.database

        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                var map = dataSnapshot.getValue<HashMap<String, Boolean>>()
                Log.d(TAG, "fetchAchievements -> $map")
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

    fun fetchUserStats() {
        val firebaseDatabase = Firebase.database

        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val user = dataSnapshot.getValue<User>() ?: User(userId, "Guest")
                Log.d(TAG, "fetchUserStats -> $user")
                username = user.username.toString()
                lastLoggedIn = user.lastLoggedInDate.toString()
                numDaysLoggedIn = user.numDaysLoggedIn ?: 0
                numFlashcardSetCreated = user.numFlashcardSetCreated ?: 0
                numFlashcardCreated = user.numFlashcardCreated ?: 0
                numFlashcardPracticed = user.numFlashcardPracticed ?: 0
                userLiveData.postValue(user)
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
        val currDateStr = LocalDate.now().toString()
        val currDate = LocalDate.parse(currDateStr, format)

        val days = ChronoUnit.DAYS.between(pastDate, currDate)

        if(days > 0) {
            numDaysLoggedIn += 1
            lastLoggedIn = currDateStr
        }
    }

    fun updateStatsAndAchievements() {
        val firebaseDatabase = Firebase.database

        val currAchievementMap = HashMap<String, Boolean>()

        var sumPoints = 0
        with (currAchievementMap) {
            put("achievement_" + achievementList[0].id, numDaysLoggedIn >= achievementList[0].goalValue)
            put("achievement_" +achievementList[1].id, numDaysLoggedIn >= achievementList[1].goalValue)
            put("achievement_" +achievementList[2].id, numDaysLoggedIn >= achievementList[2].goalValue)
            put("achievement_" +achievementList[3].id, numDaysLoggedIn >= achievementList[3].goalValue)
            put("achievement_" +achievementList[4].id, numFlashcardSetCreated >= achievementList[4].goalValue)
            put("achievement_" +achievementList[5].id, numFlashcardSetCreated >= achievementList[5].goalValue)
            put("achievement_" +achievementList[6].id, numFlashcardCreated >= achievementList[6].goalValue)
            put("achievement_" +achievementList[7].id, numFlashcardCreated >= achievementList[7].goalValue)
            put("achievement_" +achievementList[8].id, numFlashcardPracticed >= achievementList[8].goalValue)

            for((key, value) in currAchievementMap) {
                if (value) {
                    sumPoints += achievementList[key.replace("achievement_", "").toInt()].totalPoints
                }
            }
        }

        val updatedUser = User(
            userId,
            username,
            sumPoints,
            lastLoggedIn,
            numDaysLoggedIn,
            numFlashcardSetCreated,
            numFlashcardCreated,
            numFlashcardPracticed
        )

        firebaseDatabase.getReference("Users").child(userId).setValue(updatedUser)
        firebaseDatabase.getReference("Achievements").child(userId).setValue(currAchievementMap)
    }

    fun incrementNumFlashcardSetCreated() {
        numFlashcardSetCreated++
        updateStatsAndAchievements()
    }

    fun incrementNumFlashcardCreated() {
        numFlashcardCreated++
        updateStatsAndAchievements()
    }

    fun incrementNumFlashcardPracticed() {
        numFlashcardPracticed++
        updateStatsAndAchievements()
    }

    companion object {
        @Volatile
        private var INSTANCE: AchievementSource? = null

        fun initialize(context: Context, userId: String) {
            if (INSTANCE == null) {
                INSTANCE = AchievementSource(context, userId)
            }
        }

        fun getDataSource(): AchievementSource {
            return INSTANCE
                ?: throw IllegalStateException("AchievementSource must be initialized")
        }
    }
}