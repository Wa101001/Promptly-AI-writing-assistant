package com.example.promptly

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.view.ActionMode
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView

class MainActivity : AppCompatActivity() {

    private lateinit var textView: TextView  // Just for demo text

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        textView = findViewById(R.id.textView)

        textView.customSelectionActionModeCallback = object : ActionMode.Callback {
            override fun onCreateActionMode(mode: ActionMode, menu: Menu): Boolean {
                menu.add("✏️ Promptly").setOnMenuItemClickListener {
                    val start = textView.selectionStart
                    val end = textView.selectionEnd
                    val selectedText = textView.text.substring(start, end)

                    val bottomSheet = PromptlyToneBottomSheet(selectedText) { instruction ->
                        // 👇 Call GPT with selected text and instruction here later
                        GptHelper.callGpt(selectedText, instruction) { result ->
                            if (result != null) {
                                runOnUiThread {
                                    textView.text = textView.text.replaceRange(start, end, result)
                                }
                            }
                        }
                    }

                    bottomSheet.show(supportFragmentManager, "PromptlyBottomSheet")
                    true
                }
                return true
            }

            override fun onPrepareActionMode(mode: ActionMode, menu: Menu) = false
            override fun onActionItemClicked(mode: ActionMode, item: MenuItem) = false
            override fun onDestroyActionMode(mode: ActionMode) {}
        }
    }
}
