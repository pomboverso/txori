package com.rama.adiskide

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.content.ContentValues

class DatabaseHelper(context: Context) :
    SQLiteOpenHelper(context, "tasks.db", null, 10) {

    override fun onCreate(db: SQLiteDatabase) {

        db.execSQL(
            """
        CREATE TABLE tasks (
            id INTEGER PRIMARY KEY AUTOINCREMENT,
            label TEXT,
            duration INTEGER,
            task_order INTEGER,
            task_group INTEGER,
            completion_count INTEGER
        )
        """.trimIndent()
        )

        insertInitialTasks(db)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS tasks")
        onCreate(db)
    }

    private fun insertTask(
        db: SQLiteDatabase,
        label: String,
        duration: Int,
        task_order: Int,
        task_group: Int,
        completion_count: Int,
    ) {

        val values = ContentValues().apply {
            put("label", label)
            put("duration", duration)
            put("task_order", task_order)
            put("task_group", task_group)
            put("completion_count", completion_count)
        }

        db.insert("tasks", null, values)
    }

    private fun insertInitialTasks(db: SQLiteDatabase) {
        // Group 1
        insertTask(db, "Chest Opener", 90, 1, 1, 0)
        insertTask(db, "Break", 60, 2, 1, 0)
        insertTask(db, "Dead Hang", 60, 3, 1, 0)
        insertTask(db, "Break", 60, 4, 1, 0)

        // Group 2
        insertTask(db, "Pull-Up", 8, 1, 2, 0)
        insertTask(db, "Break", 60, 2, 2, 0)
        insertTask(db, "Push-Up", 40, 3, 2, 0)
        insertTask(db, "Break", 60, 4, 2, 0)
        insertTask(db, "Pull-Up", 8, 5, 2, 0)
        insertTask(db, "Break", 60, 6, 2, 0)
        insertTask(db, "Push-Up", 40, 7, 2, 0)
        insertTask(db, "Break", 60, 8, 2, 0)
        insertTask(db, "Pull-Up", 8, 9, 2, 0)
        insertTask(db, "Break", 60, 10, 2, 0)
        insertTask(db, "Push-Up", 40, 11, 2, 0)
        insertTask(db, "Break", 120, 12, 2, 0)

        // Group 3
        insertTask(db, "Chin-Up", 8, 1, 3, 0)
        insertTask(db, "Break", 60, 2, 3, 0)
        insertTask(db, "Chin-Up", 8, 3, 3, 0)
        insertTask(db, "Break", 60, 4, 3, 0)
        insertTask(db, "Chin-Up", 8, 5, 3, 0)
        insertTask(db, "Break", 120, 6, 3, 0)
        insertTask(db, "Hip Thrust", 60, 7, 3, 0)
        insertTask(db, "Break", 60, 8, 3, 0)
        insertTask(db, "Hip Thrust", 60, 9, 3, 0)
        insertTask(db, "Break", 60, 10, 3, 0)

        // Group 4
        insertTask(db, "Wall Sit", 60, 1, 4, 0)
        insertTask(db, "Break", 120, 2, 4, 0)
        insertTask(db, "Wall Sit", 60, 3, 4, 0)
        insertTask(db, "Break", 120, 4, 4, 0)
        insertTask(db, "Split Stretch", 60, 5, 4, 0)
        insertTask(db, "Break", 60, 6, 4, 0)

        // Group 5
        insertTask(db, "Crunches", 15, 1, 5, 0)
        insertTask(db, "Break", 30, 2, 5, 0)
        insertTask(db, "Flutter Kicks", 60, 3, 5, 0)
        insertTask(db, "Break", 30, 4, 5, 0)
        insertTask(db, "Plank", 60, 5, 5, 0)
        insertTask(db, "Break", 60, 6, 5, 0)
        insertTask(db, "Dead Hang", 60, 7, 5, 0)
    }
}