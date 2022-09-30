package com.sklin.termproject

import android.app.Dialog
import android.content.Context
import android.view.View
import android.view.Window
import android.widget.Button
import android.widget.TextView
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.sklin.termproject.dataclass.FlashcardSet

class AddFlashcardDialog {

    private lateinit var dialog: Dialog
    private lateinit var mContext: Context

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
            val firebaseDatabase = Firebase.database
            val databaseReference = firebaseDatabase.reference
            val id = databaseReference.push().key ?: ""

            val title = titleEditTextView.text.toString()
            val newFlashcardSet = FlashcardSet(id, title)

            //TODO: Retrieve User ID
            val userid = "1"

            databaseReference.child("FlashcardSet").child(userid).child(id).setValue(newFlashcardSet)

            dialog.dismiss()
        }

        dialog.show()
    }

    fun getWindow(): Window? {
        return dialog.window
    }

}