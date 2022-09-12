package com.sklin.termproject.ui.flashcard

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.sklin.termproject.R

class PracticeFlashcardFragment : Fragment() {

    companion object {
        fun newInstance() = PracticeFlashcardFragment()
    }

    private lateinit var viewModel: PracticeFlashcardViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_practice_flashcard, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(PracticeFlashcardViewModel::class.java)
        // TODO: Use the ViewModel
    }

}