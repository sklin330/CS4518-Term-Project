package com.sklin.termproject.ui.flashcard

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.sklin.termproject.R

class EditFlashcardFragment : Fragment() {

    companion object {
        fun newInstance() = EditFlashcardFragment()
    }

    private lateinit var viewModel: EditFlashcardViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_create_edit_flashcard, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(EditFlashcardViewModel::class.java)
        // TODO: Use the ViewModel
    }

}