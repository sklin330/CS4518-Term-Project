package com.sklin.termproject

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.lifecycle.ViewModelProvider
import com.sklin.termproject.databinding.ActivityEditCreateFlashcardBinding
import com.sklin.termproject.viewmodel.flashcard.CreateFlashcardViewModel

const val RESULT_CREATED = 3
const val EXTRA_FRONT = "com.sklin.termproject.flashcard_front"
const val EXTRA_BACK = "com.sklin.termproject.flashcard_back"

class CreateFlashcardActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEditCreateFlashcardBinding
    private lateinit var viewModel: CreateFlashcardViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditCreateFlashcardBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.title = "Create Flashcard"

        viewModel = ViewModelProvider(this).get(CreateFlashcardViewModel::class.java)

        binding.submitButton.setOnClickListener {
            if (binding.frontTextView.text.isNotEmpty() && binding.backTextView.text.isNotEmpty()) {
                val data = Intent().apply {
                    putExtra(EXTRA_FRONT, binding.frontTextView.text.toString())
                    putExtra(EXTRA_BACK, binding.backTextView.text.toString())
                }
                setResult(RESULT_CREATED, data)
                finish()
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.empty, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            android.R.id.home -> { finish() }
        }
        return true
    }
}