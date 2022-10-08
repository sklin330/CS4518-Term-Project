package com.sklin.termproject

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.View
import android.view.Window
import android.widget.Button
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.sklin.termproject.adapter.EXTRA_SET_ID
import com.sklin.termproject.adapter.EXTRA_TITLE
import com.sklin.termproject.dataclass.Flashcard

private const val TAG = "EditDeleteFlashcardDialog"

class EditDeleteFlashcardDialog {

    private lateinit var dialog: Dialog
    private lateinit var mContext: Context

    fun showDialog(context: Context, flashcard: Flashcard, flashcardSetId: String, flashcardSetTitle: String) {
        dialog = Dialog(context)
        mContext = context

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(true)
        dialog.setContentView(R.layout.dialog_edit_delete_menu)
        dialog.setCanceledOnTouchOutside(true)

        var editButton: Button = dialog.findViewById(R.id.edit_button)
        var deleteButton: Button = dialog.findViewById(R.id.delete_button)

        editButton.setOnClickListener {
            val intent = Intent(context, EditFlashcardActivity::class.java)
            intent.putExtra(EXTRA_FLASHCARD_ID, flashcard.id)
            Log.d(TAG, "flashcardID -> ${flashcard.id}")
            intent.putExtra(EXTRA_FRONT, flashcard.front)
            intent.putExtra(EXTRA_BACK, flashcard.back)
            intent.putExtra(EXTRA_AUDIO_PATH, flashcard.audioPath)
            intent.putExtra(EXTRA_TITLE, flashcardSetTitle)
            intent.putExtra(EXTRA_SET_ID, flashcardSetId)
            context.startActivity(intent)
        }

        deleteButton.setOnClickListener { view: View ->
            val firebaseDatabase = Firebase.database
            flashcard.id?.let {
                firebaseDatabase.getReference("Flashcard")
                    .child(flashcardSetId).child(it).removeValue()
            }
            dialog.dismiss()
        }

        dialog.show()
    }

    fun getWindow(): Window? {
        return dialog.window
    }

}