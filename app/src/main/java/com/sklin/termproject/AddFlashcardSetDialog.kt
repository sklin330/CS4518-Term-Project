package com.sklin.termproject

import android.app.Dialog
import android.content.Context
import android.os.Build
import android.view.View
import android.view.Window
import android.widget.Button
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.sklin.termproject.dataclass.FlashcardSet
import com.sklin.termproject.viewmodel.login.LoginViewModel
import com.sklin.termproject.viewmodel.achievement.AchievementSource

class AddFlashcardSetDialog {

    private lateinit var dialog: Dialog
    private lateinit var mContext: Context

    private lateinit var achievementSource: AchievementSource

    @RequiresApi(Build.VERSION_CODES.O)
    fun showDialog(context: Context) {
        dialog = Dialog(context)
        mContext = context

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(true)
        dialog.setContentView(R.layout.dialog_add_flashcard_set)
        dialog.setCanceledOnTouchOutside(true)

        var createButton: Button = dialog.findViewById(R.id.create_button)
        var titleEditTextView: TextView = dialog.findViewById(R.id.flashcard_set_title_edit_text)

        createButton.setOnClickListener { view: View ->
            if (titleEditTextView.text.isNotEmpty()) {
                val firebaseDatabase = Firebase.database
                val databaseReference = firebaseDatabase.reference
                val id = databaseReference.push().key ?: ""

                val title = titleEditTextView.text.toString()
                val newFlashcardSet = FlashcardSet(id, title)

                val userid = Firebase.auth.currentUser?.uid

                if (userid != null) {
                    databaseReference.child("FlashcardSet").child(userid).child(id)
                        .setValue(newFlashcardSet)
                }

                achievementSource = AchievementSource.getDataSource()
                achievementSource.incrementNumFlashcardSetCreated()

                dialog.dismiss()
            }
        }

        dialog.show()
    }

    fun getWindow(): Window? {
        return dialog.window
    }

}