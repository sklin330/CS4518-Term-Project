package com.sklin.termproject

import android.Manifest
import android.content.pm.PackageManager
import android.media.MediaPlayer
import android.media.MediaRecorder
import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.view.Menu
import android.view.MenuItem
import androidx.core.app.ActivityCompat
import androidx.lifecycle.ViewModelProvider
import com.sklin.termproject.databinding.ActivityEditCreateFlashcardBinding
import com.sklin.termproject.viewmodel.flashcard.CreateFlashcardViewModel
import java.io.File
import java.io.IOException


const val RESULT_CREATED = 3
const val EXTRA_FRONT = "com.sklin.termproject.flashcard_front"
const val EXTRA_BACK = "com.sklin.termproject.flashcard_back"

class CreateFlashcardActivity : AppCompatActivity() {

    private var audioPath: String? = null
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

        binding.recordButton.setOnClickListener{

            if(recording && checkPermissions()) {

                val file =
                    File(Environment.getExternalStorageDirectory().absolutePath)

                val fileOut = File(file, "audio.mp3")
                if (!fileOut.exists()) {
                    fileOut.mkdir()
                }
                audioPath = fileOut.path

                recorder = MediaRecorder()

                recorder.setAudioSource(MediaRecorder.AudioSource.MIC)
                //sets output to mp3 format
                recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP)
                recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC)

                recorder.setOutputFile(audioPath)


                try {
                    recorder.prepare()
                    recorder.start()
                }
                catch(e:IOException) {
                    throw IllegalArgumentException(e)
                }

                recording = false
            }

            else if(recording && !checkPermissions()){
                ActivityCompat.requestPermissions(
                    this, arrayOf(
                        Manifest.permission.RECORD_AUDIO, Manifest.permission.WRITE_EXTERNAL_STORAGE
                    ), 1
                )
            }

            else{
                recorder.stop()
                recorder.release()

                recording = true
            }

        }

        binding.audioButton.setOnClickListener{

            if(listening){
                mediaPlayer = MediaPlayer()
                mediaPlayer.setDataSource(audioPath)
                mediaPlayer.prepare()
                mediaPlayer.start()

                listening = false
            }
            else{
                mediaPlayer.stop()
                mediaPlayer.release()
            }

        }

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