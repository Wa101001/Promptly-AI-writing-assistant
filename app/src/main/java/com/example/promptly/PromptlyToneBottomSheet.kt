package com.example.promptly

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button

class PromptlyToneBottomSheet(
    private val selectedText: String,
    private val onToneSelected: (String) -> Unit
) : BottomSheetDialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.bottom_sheet_tone_picker, container, false)

        val friendlyButton = view.findViewById<Button>(R.id.btn_friendly)
        val formalButton = view.findViewById<Button>(R.id.btn_formal)
        val funnyButton = view.findViewById<Button>(R.id.btn_funny)
        val otherButton = view.findViewById<Button>(R.id.btn_other)

        friendlyButton.setOnClickListener {
            onToneSelected("Rewrite the text in a friendly and casual tone.")
            dismiss()
        }

        formalButton.setOnClickListener {
            onToneSelected("Rewrite the text in a formal and professional tone.")
            dismiss()
        }

        funnyButton.setOnClickListener {
            onToneSelected("Rewrite the text in a funny and humorous way.")
            dismiss()
        }

        otherButton.setOnClickListener {
            showOtherToneDialog(requireContext())
        }

        return view
    }

    private fun showOtherToneDialog(context: Context) {
        val builder = AlertDialog.Builder(context)
        builder.setTitle("Custom Tone")

        val input = android.widget.EditText(context)
        input.hint = "Enter custom instruction"

        builder.setView(input)

        builder.setPositiveButton("OK") { dialog, _ ->
            val userInstruction = input.text.toString()
            if (userInstruction.isNotBlank()) {
                onToneSelected(userInstruction)
            }
            dismiss()
        }

        builder.setNegativeButton("Cancel") { dialog, _ ->
            dialog.dismiss()
        }

        builder.show()
    }
}
