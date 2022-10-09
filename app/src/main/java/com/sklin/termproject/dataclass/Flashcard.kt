package com.sklin.termproject.dataclass

import java.io.Serializable

data class Flashcard(var id: String? = "", var front: String? = "", var back: String? = "", var audioPath: String? = ""): Serializable
