package com.sklin.termproject

import android.content.Intent
import android.media.MediaPlayer
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModelProvider
import com.sklin.termproject.adapter.EXTRA_SET_ID
import com.sklin.termproject.adapter.FlashcardSetAdapter
import com.sklin.termproject.databinding.ActivityPracticeFlashcardBinding
import com.sklin.termproject.dataclass.Flashcard
import com.sklin.termproject.dataclass.FlashcardList
import com.sklin.termproject.dataclass.FlashcardSet
import com.sklin.termproject.viewmodel.achievement.AchievementSource
import com.sklin.termproject.viewmodel.flashcard.CreateFlashcardViewModel
import com.sklin.termproject.viewmodel.flashcard.PracticeFlashcardViewModel

class PracticeFlashcardActivity : AppCompatActivity() {

    private var mediaPlayer = MediaPlayer()
    private var listening = true
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

        val flashcardList = intent.getSerializableExtra(EXTRA_SET_ID) as? FlashcardList
        if (flashcardList != null) {
            viewModel.setFlashcardList(flashcardList.list)
        }

        updateFlashcard()

        binding.leftButton.setOnClickListener {
            if (viewModel.getIndex() != 0) {
                viewModel.setIndex(viewModel.getIndex() - 1)
                updateFlashcard()
            }
        }

        binding.rightButton.setOnClickListener {
            if (viewModel.getIndex() != viewModel.getFlashcardList().size - 1) {
                viewModel.setIndex(viewModel.getIndex() + 1)
                updateFlashcard()
            }
        }

        var currentFlashcard: Flashcard = viewModel.getFlashcardList().get(viewModel.getIndex())
        binding.audioButton.isEnabled = currentFlashcard.audioPath != ""
        binding.audioButton.alpha = if (currentFlashcard.audioPath != "") 1f else 0.3f

        binding.audioButton.setOnClickListener {
            var currentFlashcard: Flashcard = viewModel.getFlashcardList().get(viewModel.getIndex())
            mediaPlayer = MediaPlayer()
            mediaPlayer.setDataSource(currentFlashcard.audioPath)
            mediaPlayer.prepare()
            mediaPlayer.start()
        }

    }

    private fun updateFlashcard() {
        var currentFlashcard: Flashcard = viewModel.getFlashcardList().get(viewModel.getIndex())
        binding.flashcardText.text = currentFlashcard.front
        binding.progressTextView.text = "${(viewModel.getIndex() + 1)} / ${viewModel.getFlashcardList().size}"
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

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.empty, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            android.R.id.home -> {
                finish()
            }
        }
        return true
    }
}