package com.rama.adiskide.adapters

import android.app.AlertDialog
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.rama.adiskide.R
import com.rama.adiskide.Task
import com.rama.adiskide.widgets.WdButton

class TaskAdapter(
    context: Context,
    private val tasks: MutableList<Task>,
    private val db: SQLiteDatabase
) : ArrayAdapter<Task>(context, 0, tasks) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = convertView ?: LayoutInflater.from(context)
            .inflate(R.layout.list_item_task, parent, false)

        val task = tasks[position]

        val label = view.findViewById<TextView>(R.id.task_label)
        label.text = task.label

        val duration = view.findViewById<TextView>(R.id.task_duration)
        duration.text = formatDuration(task.duration)

        // Long-press to open edit/delete dialog
        view.setOnLongClickListener {
            showEditDialog(task, position)
            true
        }

        return view
    }

    private fun formatDuration(seconds: Int): String {
        return if (seconds >= 60) {
            val m = seconds / 60
            val s = seconds % 60
            if (s == 0) "${m}m" else "${m}m ${s}s"
        } else {
            "${seconds}s"
        }
    }

    private fun showEditDialog(task: Task, position: Int) {
        val dialogView = LayoutInflater.from(context)
            .inflate(R.layout.dialog_task_edit, null)
        val dialog = AlertDialog.Builder(context)
            .setView(dialogView)
            .create()

        val labelInput = dialogView.findViewById<EditText>(R.id.label)
        val durationInput = dialogView.findViewById<EditText>(R.id.duration)

        val deleteButton = dialogView.findViewById<WdButton>(R.id.delete_button)
        val saveButton = dialogView.findViewById<WdButton>(R.id.add_button)
        val cancelButton = dialogView.findViewById<WdButton>(R.id.cancel_button)

        // Pre-fill current task data
        labelInput.setText(task.label)
        durationInput.setText(task.duration.toString())

        // Delete task
        deleteButton.setOnClickListener {
            showDeleteDialog(task, position, dialog)
        }

        // Save edited task
        saveButton.setOnClickListener {
            val newLabel = labelInput.text.toString().trim()
            val newDuration = durationInput.text.toString().toIntOrNull() ?: task.duration

            if (newLabel.isEmpty()) {
                labelInput.error = "Label cannot be empty"
                return@setOnClickListener
            }

            val values = ContentValues().apply {
                put("label", newLabel)
                put("duration", newDuration)
            }
            db.update("tasks", values, "id = ?", arrayOf(task.id.toString()))

            task.label = newLabel
            task.duration = newDuration
            notifyDataSetChanged()

            dialog.dismiss()
        }

        cancelButton.setOnClickListener { dialog.dismiss() }

        dialog.show()
    }

    private fun showDeleteDialog(task: Task, position: Int, parentDialog: AlertDialog) {
        val dialogView = LayoutInflater.from(context)
            .inflate(R.layout.dialog_task_delete, null)
        val dialog = AlertDialog.Builder(context)
            .setView(dialogView)
            .create()

        dialogView.findViewById<WdButton>(R.id.delete_button).setOnClickListener {
            // Remove from workout_steps first (FK constraint)
            db.delete("workout_steps", "task_id = ?", arrayOf(task.id.toString()))
            db.delete("tasks", "id = ?", arrayOf(task.id.toString()))
            tasks.removeAt(position)
            notifyDataSetChanged()
            dialog.dismiss()
            parentDialog.dismiss()
        }

        dialogView.findViewById<WdButton>(R.id.cancel_button).setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }
}
