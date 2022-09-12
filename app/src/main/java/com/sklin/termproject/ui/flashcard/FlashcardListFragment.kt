package com.sklin.termproject.ui.flashcard

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.sklin.termproject.R

class FlashcardListFragment : Fragment() {

    companion object {
        fun newInstance() = FlashcardListFragment()
    }

    private lateinit var viewModel: FlashcardListViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_flashcard_list, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(FlashcardListViewModel::class.java)
        // TODO: Use the ViewModel
    }

}