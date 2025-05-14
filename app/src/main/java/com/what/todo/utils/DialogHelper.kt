package com.what.todo.utils

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.drawable.GradientDrawable
import android.text.InputType
import android.view.Gravity
import android.view.ViewGroup
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.view.setPadding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.what.todo.R

object DialogHelper {
    fun showInputDialog(
        context: Context,
        title: String,
        hint: String,
        initialText: String = "",
        positiveButton: String = "Save ✨",
        negativeButton: String = "Cancel",
        onPositive: (String) -> Unit
    ) {
        // Create main container with padding
        val container = LinearLayout(context).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(24)
            gravity = Gravity.CENTER
        }

        // Add cute title emoji
        val titleEmoji = TextView(context).apply {
            text = "✨"
            textSize = 24f
            gravity = Gravity.CENTER
            layoutParams = LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            ).apply {
                bottomMargin = 16
            }
        }
        container.addView(titleEmoji)

        // Create TextInputLayout with cute styling
        val textInputLayout = TextInputLayout(context).apply {
            layoutParams = LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            setBoxBackgroundMode(TextInputLayout.BOX_BACKGROUND_OUTLINE)
            setBoxStrokeColorStateList(ColorStateList.valueOf(context.getColor(R.color.accent_purple)))
            setHintTextColor(ColorStateList.valueOf(context.getColor(R.color.accent_pink)))
            setHint(hint)
            
            // Add cute box background
            background = GradientDrawable().apply {
                cornerRadius = 20f
                setColor(ContextCompat.getColor(context, android.R.color.white))
            }
        }

        val editText = TextInputEditText(context).apply {
            layoutParams = LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            inputType = InputType.TYPE_CLASS_TEXT
            setText(initialText)
            setTextColor(context.getColor(R.color.text_primary))
            setPadding(20)
        }

        textInputLayout.addView(editText)
        container.addView(textInputLayout)

        MaterialAlertDialogBuilder(context, R.style.CuteDialog)
            .setTitle("$title ✨")
            .setView(container)
            .setPositiveButton("$positiveButton ✨") { _, _ ->
                onPositive(editText.text?.toString() ?: "")
            }
            .setNegativeButton("$negativeButton 🌸", null)
            .show()
    }

    fun showConfirmDialog(
        context: Context,
        title: String,
        message: String,
        positiveButton: String = "Yes ✨",
        negativeButton: String = "No",
        onPositive: () -> Unit
    ) {
        val container = LinearLayout(context).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(24)
            gravity = Gravity.CENTER
        }

        // Add message with cute styling
        val messageView = TextView(context).apply {
            text = "$message 🌸"
            textSize = 16f
            gravity = Gravity.CENTER
            setTextColor(context.getColor(R.color.text_primary))
            setPadding(20)
        }
        container.addView(messageView)

        MaterialAlertDialogBuilder(context, R.style.CuteDialog)
            .setTitle("$title ✨")
            .setView(container)
            .setPositiveButton("$positiveButton ✨") { _, _ -> onPositive() }
            .setNegativeButton("$negativeButton 🌸", null)
            .show()
    }
} 