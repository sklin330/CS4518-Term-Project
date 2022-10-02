package com.sklin.termproject

import android.app.Dialog
import android.content.Context
import android.view.View
import android.view.Window
import android.widget.Button

class EditDeleteFlashcardDialog {

    private lateinit var dialog: Dialog
    private lateinit var mContext: Context

    fun showDialog(context: Context) {
        dialog = Dialog(context)
        mContext = context

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(true)
        dialog.setContentView(R.layout.dialog_edit_delete_menu)
        dialog.setCanceledOnTouchOutside(true)

        var editButton: Button = dialog.findViewById(R.id.edit_button)
        var deleteButton: Button = dialog.findViewById(R.id.delete_button)

        editButton.setOnClickListener { view: View ->

        }

        deleteButton.setOnClickListener { view: View ->

        }

        dialog.show()
    }

}