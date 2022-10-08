package com.sklin.termproject

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.ViewModelProvider
import com.sklin.termproject.adapter.FlashcardSetAdapter
import com.sklin.termproject.databinding.ActivityPracticeFlashcardBinding
import com.sklin.termproject.dataclass.Flashcard
import com.sklin.termproject.dataclass.FlashcardSet
import com.sklin.termproject.viewmodel.flashcard.CreateFlashcardViewModel
import com.sklin.termproject.viewmodel.flashcard.PracticeFlashcardViewModel

class PracticeFlashcardActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPracticeFlashcardBinding
    private lateinit var viewModel: PracticeFlashcardViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPracticeFlashcardBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.title = "Practice Flashcards"

        viewModel = ViewModelProvider(this).get(PracticeFlashcardViewModel::class.java)

        val flashcardSet = intent.getSerializableExtra("flashcardSet") as? FlashcardSet
        if (flashcardSet != null) {
            flashcardSet.id?.let { viewModel.setFlashcardSetId(it) }

            val flashcardLiveData = viewModel.getLiveFlashcardList()

            flashcardLiveData.observe(this) {
                it?.let {
                    var currentFlashcard: Flashcard = it.get(viewModel.getIndex())
                    binding.flashcardText.text = currentFlashcard.front
                    binding.progressTextView.text = "${(viewModel.getIndex() + 1)} / ${it.size}"
                    binding.practiceFlashcard.setOnClickListener {
                        if (viewModel.getIsFront()) {
                            binding.flashcardText.text = currentFlashcard.back
                            viewModel.setIsFront(false)
                        } else {
                            binding.flashcardText.text = currentFlashcard.front
                            viewModel.setIsFront(true)
                        }
                    }
                }
            }
//
//            if ((flashcardLiveData.value?.size ?: 0) != 0) {
//                var currentFlashcard: Flashcard
//                currentFlashcard = (flashcardLiveData.value?.get(0) ?: null) as Flashcard
//
//                viewModel.setIndex(0)
//
//
//            }
        }



        //val currentCard = viewModel.getCurrentFlashcard()
//        binding.flashcardText.text = currentCard.front
//        binding.progressTextView.text = viewModel.getCardNumber().toString() + "/" + viewModel.getTotal().toString()





    }
}