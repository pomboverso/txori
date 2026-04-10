package com.rama.adiskide

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.content.ContentValues

class DatabaseHelper(context: Context) :
    SQLiteOpenHelper(context, "tasks.db", null, 1) {

    override fun onCreate(db: SQLiteDatabase) {

        // TASKS
        db.execSQL(
            """
            CREATE TABLE tasks (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                label TEXT UNIQUE,
                duration INTEGER,
                completion_count INTEGER DEFAULT 0
            )
            """.trimIndent()
        )

        // GROUPS
        db.execSQL(
            """
            CREATE TABLE workouts (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                name TEXT
            )
            """.trimIndent()
        )

        // STEPS / ORDER
        db.execSQL(
            """
            CREATE TABLE workout_steps (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                workout_id INTEGER,
                task_id INTEGER,
                step_order INTEGER,
                FOREIGN KEY(workout_id) REFERENCES workouts(id),
                FOREIGN KEY(task_id) REFERENCES tasks(id)
            )
            """.trimIndent()
        )

        insertInitialData(db)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS workout_steps")
        db.execSQL("DROP TABLE IF EXISTS workouts")
        db.execSQL("DROP TABLE IF EXISTS tasks")
        onCreate(db)
    }

    // TASK HELPERS
    private fun getOrCreateTaskId(
        db: SQLiteDatabase,
        label: String,
        duration: Int
    ): Long {
        val cursor = db.rawQuery(
            "SELECT id FROM tasks WHERE label = ?",
            arrayOf(label)
        )

        if (cursor.moveToFirst()) {
            val id = cursor.getLong(0)
            cursor.close()
            return id
        }
        cursor.close()

        val values = ContentValues().apply {
            put("label", label)
            put("duration", duration)
        }

        return db.insert("tasks", null, values)
    }

    private fun insertWorkout(db: SQLiteDatabase, name: String): Long {
        val values = ContentValues().apply {
            put("name", name)
        }
        return db.insert("workouts", null, values)
    }

    private fun insertWorkoutStep(
        db: SQLiteDatabase,
        workoutId: Long,
        taskId: Long,
        order: Int
    ) {
        val values = ContentValues().apply {
            put("workout_id", workoutId)
            put("task_id", taskId)
            put("step_order", order)
        }

        db.insert("workout_steps", null, values)
    }

    // INITIAL DATA
    private fun insertInitialData(db: SQLiteDatabase) {

        // WORKOUT 1
        val workout1 = insertWorkout(db, "Upper Body Flow")

        var order = 1

        fun step(label: String, duration: Int) {
            val taskId = getOrCreateTaskId(db, label, duration)
            insertWorkoutStep(db, workout1, taskId, order++)
        }

        step("Chest Opener", 90)
        step("Break", 60)
        step("Dead Hang", 60)
        step("Break", 60)

        // Pull / Push cycle
        repeat(3) {
            step("Pull-Up", 8)
            step("Break", 60)
            step("Push-Up", 40)
            step("Break", 60)
        }

        step("Break", 120)

        // WORKOUT 2
        val workout2 = insertWorkout(db, "Strength Block")

        order = 1

        fun step2(label: String, duration: Int) {
            val taskId = getOrCreateTaskId(db, label, duration)
            insertWorkoutStep(db, workout2, taskId, order++)
        }

        repeat(3) {
            step2("Chin-Up", 8)
            step2("Break", 60)
        }

        step2("Chin-Up", 8)
        step2("Break", 120)

        step2("Hip Thrust", 60)
        step2("Break", 60)
        step2("Hip Thrust", 60)
        step2("Break", 60)

        // WORKOUT 3
        val workout3 = insertWorkout(db, "Mobility & Core")

        order = 1

        fun step3(label: String, duration: Int) {
            val taskId = getOrCreateTaskId(db, label, duration)
            insertWorkoutStep(db, workout3, taskId, order++)
        }

        step3("Wall Sit", 60)
        step3("Break", 120)
        step3("Wall Sit", 60)
        step3("Break", 120)
        step3("Split Stretch", 60)
        step3("Break", 60)

        step3("Crunches", 15)
        step3("Break", 30)
        step3("Flutter Kicks", 60)
        step3("Break", 30)
        step3("Plank", 60)
        step3("Break", 60)
        step3("Dead Hang", 60)
    }
}