package com.sklin.termproject

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.view.View
import android.view.Window
import android.widget.Button
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.sklin.termproject.adapter.EXTRA_SET_ID
import com.sklin.termproject.adapter.EXTRA_TITLE
import com.sklin.termproject.dataclass.FlashcardSet

class EditDeleteFlashcardSetDialog {

    private lateinit var dialog: Dialog
    private lateinit var mContext: Context

    fun showDialog(context: Context, title: String, flashcardSetId: String) {
        dialog = Dialog(context)
        mContext = context

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(true)
        dialog.setContentView(R.layout.dialog_edit_delete_menu)
        dialog.setCanceledOnTouchOutside(true)

        var practiceButton: Button = dialog.findViewById(R.id.practice_button)
        var editButton: Button = dialog.findViewById(R.id.edit_button)
        var deleteButton: Button = dialog.findViewById(R.id.delete_button)

        practiceButton.setOnClickListener {
            val intent = Intent(context, PracticeFlashcardActivity::class.java)
            intent.putExtra(EXTRA_SET_ID, flashcardSetId)
            context?.startActivity(intent)
        }

        editButton.setOnClickListener {
            val intent = Intent(context, FlashcardListActivity::class.java)
            intent.putExtra(EXTRA_TITLE, title)
            intent.putExtra(EXTRA_SET_ID, flashcardSetId)
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