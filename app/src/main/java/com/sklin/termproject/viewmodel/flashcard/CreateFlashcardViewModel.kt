package com.sklin.termproject.viewmodel.flashcard

import androidx.lifecycle.ViewModel

class CreateFlashcardViewModel : ViewModel() {

    private var front = ""
    private var back = ""
    private var audioPath = ""

    fun getFront(): String {
        return front
    }

    fun getBack(): String  {
        return back
    }

    fun getAudioPath(): String  {
        return audioPath
    }

    fun setFront(text: String) {
        this.front = text
    }

    fun setBack(text: String)  {
        this.back = text
    }

    fun setAudioPath(path: String)  {
        this.audioPath = path
    }

}