package com.sklin.termproject

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.sklin.termproject.databinding.ActivityPracticeFlashcardBinding

class PracticeFlashcardActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPracticeFlashcardBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPracticeFlashcardBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}