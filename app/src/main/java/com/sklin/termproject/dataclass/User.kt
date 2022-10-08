package com.sklin.termproject.dataclass

data class User(
    var id: String? = "",
    var username: String? = "",
    var totalPoints: Int? = 0,
    var lastLoggedInDate: String? = "2022-01-01",
    var numDaysLoggedIn: Int? = 0,
    var numFlashcardSetCreated: Int? = 0,
    var numFlashcardCreated: Int? = 0,
    var numFlashcardPracticed: Int? = 0)
