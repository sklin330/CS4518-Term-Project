package com.sklin.termproject.viewmodel.flashcard

import android.icu.text.CaseMap
import androidx.lifecycle.ViewModel

class EditFlashcardViewModel : ViewModel() {

    private var id = ""
    private var front = ""
    private var back = ""
    private var audioPath = ""

    private var setId = ""
    private var setTitle = ""

    fun getId(): String {
        return id
    }

    fun setId(id: String) {
        this.id = id
    }

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

    fun getSetId(): String {
        return setId
    }

    fun getSetTitle(): String {
        return setTitle
    }

    fun setSetTitle(setTitle: String) {
        this.setTitle = setTitle
    }

    fun setSetId(setId: String) {
        this.setId = setId
    }

}