package com.sklin.termproject

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.sklin.termproject.adapter.EXTRA_ID
import com.sklin.termproject.adapter.EXTRA_TITLE
import com.sklin.termproject.adapter.FlashcardAdapter
import com.sklin.termproject.databinding.ActivityFlashcardListBinding
import com.sklin.termproject.dataclass.Flashcard
import com.sklin.termproject.dataclass.FlashcardSet
import com.sklin.termproject.viewmodel.flashcard.FlashcardListViewModel

const val REQUEST_CODE_CREATE = 2

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
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        viewModel = ViewModelProvider(this).get(FlashcardListViewModel::class.java)

        val flashcardSetTitle = intent.getStringExtra(EXTRA_TITLE)
        supportActionBar?.title = flashcardSetTitle

        val flashcardSetID = intent.getStringExtra(EXTRA_ID) ?: ""
        viewModel.setFlashcardSetId(flashcardSetID)

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
        menuInflater.inflate(R.menu.flashcard_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.action_add -> {
                val intent = Intent(this, CreateFlashcardActivity::class.java)
                startActivityForResult(intent, REQUEST_CODE_CREATE)
            }
            android.R.id.home -> { finish() }
        }
        return true
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == RESULT_CREATED) {
            var front = data?.getStringExtra(EXTRA_FRONT)
            var back = data?.getStringExtra(EXTRA_BACK)

            if (front != null && back != null) {
                viewModel.persistFlashcard(front, back)
            }
            return
        }
        return
    }

}