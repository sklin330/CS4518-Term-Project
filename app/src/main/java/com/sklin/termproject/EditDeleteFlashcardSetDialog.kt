package com.sklin.termproject

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import android.view.View
import android.view.Window
import android.widget.Button
import androidx.annotation.RequiresApi
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import com.sklin.termproject.adapter.EXTRA_SET_ID
import com.sklin.termproject.adapter.EXTRA_TITLE
import com.sklin.termproject.dataclass.Flashcard
import com.sklin.termproject.dataclass.FlashcardList
import com.sklin.termproject.dataclass.FlashcardSet
import com.sklin.termproject.viewmodel.achievement.AchievementSource

class EditDeleteFlashcardSetDialog {

    private lateinit var dialog: Dialog
    private lateinit var mContext: Context

    @RequiresApi(Build.VERSION_CODES.O)
    fun showDialog(context: Context, flashcardSet: FlashcardSet) {
        dialog = Dialog(context)
        mContext = context

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(true)
        dialog.setContentView(R.layout.dialog_edit_delete_flashcard_set_menu)
        dialog.setCanceledOnTouchOutside(true)

        var practiceButton: Button = dialog.findViewById(R.id.practice_button)
        var editButton: Button = dialog.findViewById(R.id.edit_button)
        var deleteButton: Button = dialog.findViewById(R.id.delete_button)

        practiceButton.setOnClickListener {
            val firebaseDatabase = Firebase.database

            flashcardSet.id?.let { it1 ->
                firebaseDatabase.getReference("Flashcard").child(it1).get().addOnSuccessListener {
                    var list = mutableListOf<Flashcard>()
                    for (flashcardSnapshot in it.children) {
                        val flashcard = flashcardSnapshot.getValue<Flashcard>()
                        if (flashcard != null) {
                            //Log.d(TAG, "fetchFlashcards:onDataChange -> $flashcardSnapshot")
                            list.add(flashcard)
                        }
                    }
                    val flashcards = FlashcardList(list.toList())
                    val intent = Intent(context, PracticeFlashcardActivity::class.java)
                    intent.putExtra(EXTRA_SET_ID, flashcards)
                    dialog.dismiss()
                    val achievementSource = AchievementSource.getDataSource()
                    achievementSource.incrementNumFlashcardPracticed()
                    context?.startActivity(intent)
                }.addOnFailureListener{
                    Log.e("EditDeleteFlashcardSetDialog", "Error getting data", it)
                }
            }
        }

        editButton.setOnClickListener {
            val intent = Intent(context, FlashcardListActivity::class.java)
            intent.putExtra(EXTRA_TITLE, flashcardSet.title)
            intent.putExtra(EXTRA_SET_ID, flashcardSet.id)
            context?.startActivity(intent)
        }

        deleteButton.setOnClickListener {
            val userid = Firebase.auth.currentUser?.uid
            val firebaseDatabase = Firebase.database
            if (userid != null) {
                firebaseDatabase.getReference("FlashcardSet")
                    .child(userid).removeValue()
            }
            dialog.dismiss()
        }

        dialog.show()
    }

    fun getWindow(): Window? {
        return dialog.window
    }
}