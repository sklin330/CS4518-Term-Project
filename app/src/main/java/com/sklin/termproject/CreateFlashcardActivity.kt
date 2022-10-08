package com.sklin.termproject

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.media.MediaPlayer
import android.media.MediaRecorder
import android.os.Bundle
import android.os.Environment
import android.text.Editable
import android.text.TextWatcher
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.ViewModelProvider
import com.sklin.termproject.databinding.ActivityEditCreateFlashcardBinding
import com.sklin.termproject.viewmodel.flashcard.CreateFlashcardViewModel
import java.io.File
import java.io.IOException


const val RESULT_CREATED = 3
const val EXTRA_FRONT = "com.sklin.termproject.flashcard_front"
const val EXTRA_BACK = "com.sklin.termproject.flashcard_back"
const val EXTRA_AUDIO_PATH = "com.sklin.termproject.flashcard_audio_path"

class CreateFlashcardActivity : AppCompatActivity() {

    private var audioFilePath: String? = null
    private var recorder = MediaRecorder()
    private var mediaPlayer = MediaPlayer()
    private var recording = true
    private var listening = true

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

        binding.frontTextView.setText(viewModel.getFront())
        binding.frontTextView.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                viewModel.setFront(binding.frontTextView.toString())
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        binding.backTextView.setText(viewModel.getBack())
        binding.backTextView.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                viewModel.setBack(binding.backTextView.toString())
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        audioFilePath = viewModel.getAudioPath()

        val audioDirectory = File(applicationContext.filesDir, "audio")
        if (!audioDirectory.exists()) {
            audioDirectory.mkdir()
        }

        binding.recordButton.setOnClickListener{
            if(recording && checkPermissions()) {
                val audioID = "" + ".mp3"

                val audioFile = File(audioDirectory, audioID)
                audioFilePath = audioFile.path

                recorder = MediaRecorder()
                recorder.setAudioSource(MediaRecorder.AudioSource.MIC)
                recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP)
                recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC)
                recorder.setOutputFile(audioFilePath)

                try {
                    recorder.prepare()
                    recorder.start()
                    binding.recordButton.setColorFilter(resources.getColor(android.R.color.holo_red_light))
                    binding.audioButton.alpha = 0.3f
                    binding.audioButton.isEnabled = false
                }
                catch(e:IOException) {
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
                viewModel.setAudioPath(audioFilePath!!)
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
                mediaPlayer.setDataSource(audioFilePath)
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
                val data = Intent().apply {
                    putExtra(EXTRA_FRONT, binding.frontTextView.text.toString())
                    putExtra(EXTRA_BACK, binding.backTextView.text.toString())
                    if(audioFilePath != null) {
                        putExtra(EXTRA_AUDIO_PATH, audioFilePath)
                    }
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