package com.rama.adiskide.activities

import android.os.Bundle
import android.view.View
import android.widget.ListView
import com.rama.adiskide.CsActivity
import com.rama.adiskide.DatabaseHelper
import com.rama.adiskide.R
import com.rama.adiskide.adapters.TaskAdapter

class MainActivity : CsActivity() {
    private lateinit var listView: ListView
    private val dbHelper by lazy { DatabaseHelper(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.view_home)

        val root = findViewById<View>(R.id.root)
        applyEdgeToEdgePadding(root)

        listView = findViewById(R.id.task_list)

        val db = dbHelper.readableDatabase

        val workouts = dbHelper.getWorkouts(db)

        val tasks = if (workouts.isNotEmpty()) {
            dbHelper.getAllWorkoutTasks(db)
        } else {
            dbHelper.getAllTasks(db)
        }

        val adapter = TaskAdapter(this, tasks, db)
        listView.adapter = adapter
    }

    override fun onDestroy() {
        dbHelper.close()
        super.onDestroy()
    }
}
