package com.sklin.termproject

import android.app.Dialog
import android.content.Context
import android.view.View
import android.view.Window
import android.widget.Button

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

        createButton.setOnClickListener { view: View ->
            dialog.dismiss()
        }

        dialog.show()
    }

    fun getWindow(): Window? {
        return dialog.window
    }

}