package com.sklin.termproject

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.sklin.termproject.databinding.ActivityFlashcardListBinding
import com.sklin.termproject.viewmodel.flashcard.FlashcardListViewModel

class FlashcardListActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var flashcardAdapter: FlashcardAdapter

    private lateinit var viewModel: FlashcardListViewModel
    private lateinit var binding: ActivityFlashcardListBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityFlashcardListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        getSupportActionBar()?.setDisplayHomeAsUpEnabled(true);
        getSupportActionBar()?.setDisplayShowHomeEnabled(true);

        viewModel = ViewModelProvider(this).get(FlashcardListViewModel::class.java)

        val flashcardTitle = intent.getStringExtra(EXTRA_TITLE)
        getSupportActionBar()?.setTitle(flashcardTitle);

        recyclerView = binding.flashcardRecyclerView
        recyclerView.layoutManager = LinearLayoutManager(this)
        flashcardAdapter = FlashcardAdapter(viewModel.getFlashcardList())
        recyclerView.adapter = flashcardAdapter

        val flashcardLiveData = viewModel.getLiveFlashcardList()

        flashcardLiveData.observe(this) {
            it?.let {
                flashcardAdapter = FlashcardAdapter(it)
                recyclerView.adapter = flashcardAdapter
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.action_add -> {
                val intent = Intent(this, CreateFlashcardActivity::class.java)
                startActivity(intent)
            }
            android.R.id.home -> { finish() }
        }
        return true
    }

}