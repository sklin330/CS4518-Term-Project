package com.sklin.termproject

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.media.MediaPlayer
import android.media.MediaRecorder
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.core.app.ActivityCompat
import androidx.lifecycle.ViewModelProvider
import com.sklin.termproject.adapter.EXTRA_SET_ID
import com.sklin.termproject.adapter.EXTRA_TITLE
import com.sklin.termproject.databinding.ActivityEditCreateFlashcardBinding
import com.sklin.termproject.viewmodel.flashcard.CreateFlashcardViewModel
import com.sklin.termproject.viewmodel.flashcard.EditFlashcardViewModel
import java.io.File
import java.io.IOException

const val EXTRA_FLASHCARD_ID = "com.sklin.termproject.flashcard_id"
private const val TAG = "EditFlashcardActivity"

class EditFlashcardActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEditCreateFlashcardBinding
    private lateinit var viewModel: EditFlashcardViewModel

    private var recorder = MediaRecorder()
    private var mediaPlayer = MediaPlayer()
    private var recording = true
    private var listening = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditCreateFlashcardBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.title = "Edit Flashcard"

        viewModel = ViewModelProvider(this).get(EditFlashcardViewModel::class.java)

        viewModel.setId(intent.getStringExtra(EXTRA_FLASHCARD_ID) ?: "")
        Log.d(TAG, "flashcardID -> ${intent.getStringExtra(EXTRA_FLASHCARD_ID)}")
        viewModel.setFront(intent.getStringExtra(EXTRA_FRONT) ?: "")
        viewModel.setBack(intent.getStringExtra(EXTRA_BACK) ?: "")
        viewModel.setAudioPath(intent.getStringExtra(EXTRA_AUDIO_PATH) ?: "")

        binding.frontTextView.setText(viewModel.getFront())
        binding.backTextView.setText(viewModel.getBack())

        viewModel.setSetId(intent.getStringExtra(EXTRA_SET_ID) ?: "")
        viewModel.setSetTitle(intent.getStringExtra(EXTRA_TITLE) ?: "")

        binding.frontTextView.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                viewModel.setFront(binding.frontTextView.toString())
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        binding.backTextView.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                viewModel.setBack(binding.backTextView.toString())
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        val audioDirectory = File(applicationContext.filesDir, "audio")
        if (!audioDirectory.exists()) {
            audioDirectory.mkdir()
        }

        binding.recordButton.setOnClickListener{
            if(recording && checkPermissions()) {
                val audioID = "" + ".mp3"

                val audioFile = File(audioDirectory, audioID)
                viewModel.setAudioPath(audioFile.path)

                recorder = MediaRecorder()
                recorder.setAudioSource(MediaRecorder.AudioSource.MIC)
                recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP)
                recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC)
                recorder.setOutputFile(viewModel.getAudioPath())

                try {
                    recorder.prepare()
                    recorder.start()

                    binding.recordButton.setColorFilter(resources.getColor(android.R.color.holo_red_light))
                    binding.audioButton.alpha = 0.3f
                    binding.audioButton.isEnabled = false
                }
                catch(e: IOException) {
                    throw IllegalArgumentException(e)
                }

                recording = false
            } else if(recording && !checkPermissions()){
                ActivityCompat.requestPermissions(
                    this, arrayOf(
                        Manifest.permission.RECORD_AUDIO, Manifest.permission.WRITE_EXTERNAL_STORAGE
                    ), 1
                )
            } else{
                recorder.stop()
                recorder.release()
                viewModel.setAudioPath(viewModel.getAudioPath())
                recording = true
                binding.recordButton.colorFilter = null
                binding.audioButton.alpha = 1f
                binding.audioButton.isEnabled = true
            }
        }

        binding.audioButton.isEnabled = viewModel.getAudioPath() != ""
        binding.audioButton.alpha = if (viewModel.getAudioPath() != "") 1f else 0.3f

        binding.audioButton.setOnClickListener{
            if(listening){
                mediaPlayer = MediaPlayer()
                mediaPlayer.setDataSource(viewModel.getAudioPath())
                mediaPlayer.prepare()
                mediaPlayer.start()

                listening = false
            } else{
                mediaPlayer.stop()
                mediaPlayer.release()
                listening = true
            }

        }

        binding.submitButton.setOnClickListener {
            if (binding.frontTextView.text.isNotEmpty() && binding.backTextView.text.isNotEmpty()) {
                val intent = Intent(this, FlashcardListActivity::class.java)
                intent.apply {
                    putExtra(EXTRA_FLASHCARD_ID, viewModel.getId())
                    Log.d(TAG, "flashcardID -> ${viewModel.getId()}")
                    putExtra(EXTRA_FRONT, binding.frontTextView.text.toString())
                    putExtra(EXTRA_BACK, binding.backTextView.text.toString())
                    putExtra(EXTRA_TITLE, viewModel.getSetTitle())
                    putExtra(EXTRA_SET_ID, viewModel.getSetId())
                    if(viewModel.getAudioPath() != "") {
                        putExtra(EXTRA_AUDIO_PATH, viewModel.getAudioPath())
                    }
                }
                startActivity(intent)
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
                val intent = Intent(this, FlashcardListActivity::class.java)
                intent.apply {
                    putExtra(EXTRA_TITLE, viewModel.getSetTitle())
                    putExtra(EXTRA_SET_ID, viewModel.getSetId())
                }
                startActivity(intent)
            }
        }
        return true
    }

    private fun checkPermissions(): Boolean {
        val first = ActivityCompat.checkSelfPermission(
            applicationContext,
            Manifest.permission.RECORD_AUDIO
        )
        val second = ActivityCompat.checkSelfPermission(
            applicationContext,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        )
        return first == PackageManager.PERMISSION_GRANTED &&
                second == PackageManager.PERMISSION_GRANTED
    }
}