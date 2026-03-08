package com.rama.adiskide.adapters

import android.app.AlertDialog
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.rama.adiskide.R
import com.rama.adiskide.Task
import com.rama.adiskide.TaskType
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
        val icon = view.findViewById<ImageView>(R.id.task_icon)

        label.text = task.label

        if (task.type == TaskType.ROUTINE) {
            icon.setImageResource(R.drawable.icon_seedlings)
        } else {
            icon.setImageResource(R.drawable.icon_fire)
        }

        // Complete task (remove from list only)
        val completeTaskButton = view.findViewById<FrameLayout>(R.id.complete_task)
        completeTaskButton.setOnClickListener {
            tasks.remove(task)
            notifyDataSetChanged()
        }

        // Delete task (remove from list + DB)
        val deleteTaskButton = view.findViewById<FrameLayout>(R.id.edit_task)
        deleteTaskButton.setOnClickListener {
            val dialogView = LayoutInflater.from(context)
                .inflate(R.layout.dialog_task_edit, null)
            val dialog = AlertDialog.Builder(context)
                .setView(dialogView)
                .create()

            val deleteButton = dialogView.findViewById<WdButton>(R.id.delete_button)
            val cancelButton = dialogView.findViewById<WdButton>(R.id.cancel_button)

            deleteButton.setOnClickListener {
                db.delete("tasks", "id = ?", arrayOf(task.id.toString()))
                tasks.remove(task)
                notifyDataSetChanged()
                dialog.dismiss()
            }

            cancelButton.setOnClickListener {
                dialog.dismiss()
            }

            dialog.show()
        }

        return view
    }
}