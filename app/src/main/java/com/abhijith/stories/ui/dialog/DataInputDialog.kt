package com.abhijith.stories.ui.dialog

import android.app.AlertDialog
import android.content.Context
import android.widget.EditText
import android.widget.Toast

class DataInputDialog(
    val context: Context,
    val onInput:(str:String)->Unit,
    val onCancel:()->Unit
) {
    val alert: AlertDialog.Builder = AlertDialog.Builder(context)

    fun show() {
        alert.setTitle("Title")
        alert.setMessage("Message")
        val input = EditText(context)
        alert.setView(input)

        alert.setPositiveButton("Ok") { dialog, whichButton ->
            onInput(input.text.toString())
        }

        alert.setNegativeButton("Cancel") { dialog, whichButton ->
            onCancel()
        }

        alert.show()
        Toast.makeText(context, "Clicked!", Toast.LENGTH_SHORT).show()
    }
}