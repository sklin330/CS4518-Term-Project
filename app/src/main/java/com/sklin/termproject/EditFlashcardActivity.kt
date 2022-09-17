package com.sklin.termproject

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.sklin.termproject.databinding.ActivityEditCreateFlashcardBinding

class EditFlashcardActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEditCreateFlashcardBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditCreateFlashcardBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}