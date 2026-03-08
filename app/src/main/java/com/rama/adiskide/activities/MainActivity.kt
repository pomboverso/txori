package com.rama.adiskide.activities

import android.os.Bundle
import android.view.View
import android.widget.ListView
import com.rama.adiskide.CsActivity
import com.rama.adiskide.DatabaseHelper
import com.rama.adiskide.R
import com.rama.adiskide.Task
import com.rama.adiskide.TaskType
import com.rama.adiskide.adapters.TaskAdapter

class MainActivity : CsActivity() {
    private lateinit var listView: ListView
    val dbHelper = DatabaseHelper(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.view_home)

        val root = findViewById<View>(R.id.root)
        applyEdgeToEdgePadding(root)

        // --- Views ---

        listView = findViewById(R.id.task_list)

        val db = dbHelper.readableDatabase

        val tasks = mutableListOf<Task>()
        val cursor = db.rawQuery("SELECT * FROM tasks WHERE completed = 0", null)

        while (cursor.moveToNext()) {

            val id = cursor.getLong(cursor.getColumnIndexOrThrow("id"))
            val label = cursor.getString(cursor.getColumnIndexOrThrow("label"))
            val typeString = cursor.getString(cursor.getColumnIndexOrThrow("type"))
            val type = TaskType.valueOf(typeString)
            val difficulty = cursor.getInt(cursor.getColumnIndexOrThrow("difficulty"))

            val dateCreation = cursor.getLong(cursor.getColumnIndexOrThrow("date_creation"))

            tasks.add(Task(id, type, label, difficulty, dateCreation, null))
        }

        cursor.close()

        val adapter = TaskAdapter(this, tasks, db)
        listView.adapter = adapter

    }
}