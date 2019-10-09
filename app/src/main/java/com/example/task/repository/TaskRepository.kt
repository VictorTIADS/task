package com.example.task.repository

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import com.example.task.constants.DataBaseConstants
import com.example.task.entities.TaskEntity

class TaskRepository private constructor(context: Context) {

    private var mTaskDataBaseHelper: TaskDataBaseHelper = TaskDataBaseHelper(context)


    companion object {

        fun getInstance(context: Context): TaskRepository {
            if (INSTANCE == null) {
                INSTANCE = TaskRepository(context)
            }
            return INSTANCE as TaskRepository

        }

        private var INSTANCE: TaskRepository? = null
    }

    fun get(id: Int): TaskEntity? {
        var taskEntity: TaskEntity? = null
        try {
            val cursor: Cursor

            val db = mTaskDataBaseHelper.readableDatabase

            val projection = arrayOf(
                DataBaseConstants.TASK.COLUMNS.ID,
                DataBaseConstants.TASK.COLUMNS.USERID,
                DataBaseConstants.TASK.COLUMNS.PRIORITYID,
                DataBaseConstants.TASK.COLUMNS.DESCRIPTION,
                DataBaseConstants.TASK.COLUMNS.DUEDATE,
                DataBaseConstants.TASK.COLUMNS.COMPLETE
            )

            val selection = "${DataBaseConstants.TASK.COLUMNS.ID} = ? "


            val selectionArgs = arrayOf(id.toString())

            cursor = db.query(
                DataBaseConstants.TASK.TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                null
            )

            if (cursor.count > 0) {
                cursor.moveToFirst()
                val taskId = cursor.getInt(cursor.getColumnIndex(DataBaseConstants.TASK.COLUMNS.ID))
                val userId =
                    cursor.getInt(cursor.getColumnIndex(DataBaseConstants.TASK.COLUMNS.USERID))
                val priorityId =
                    cursor.getInt(cursor.getColumnIndex(DataBaseConstants.TASK.COLUMNS.PRIORITYID))
                val description =
                    cursor.getString(cursor.getColumnIndex(DataBaseConstants.TASK.COLUMNS.DESCRIPTION))
                val dueDate =
                    cursor.getString(cursor.getColumnIndex(DataBaseConstants.TASK.COLUMNS.DUEDATE))
                val complete =
                    (cursor.getInt(cursor.getColumnIndex(DataBaseConstants.TASK.COLUMNS.COMPLETE)) == 1)

                taskEntity = TaskEntity(id, userId, priorityId, description, dueDate, complete)

            }

            cursor.close()
        } catch (e: Exception) {
            return taskEntity
        }
        return taskEntity

    }


    fun getList(userId: Int,taskFilter:Int): MutableList<TaskEntity> {

        val list = mutableListOf<TaskEntity>()

        try {
            val cursor: Cursor
            val dp = mTaskDataBaseHelper.readableDatabase


            cursor = dp.rawQuery(
                "SELECT * FROM ${DataBaseConstants.TASK.TABLE_NAME} WHERE ${DataBaseConstants.TASK.COLUMNS.USERID} = $userId AND ${DataBaseConstants.TASK.COLUMNS.COMPLETE} = $taskFilter",
                null
            )
            if (cursor.count > 0) {
//                cursor.moveToFirst()
                while (cursor.moveToNext()) {
                    val id = cursor.getInt(cursor.getColumnIndex(DataBaseConstants.TASK.COLUMNS.ID))
                    val priorityId =
                        cursor.getInt(cursor.getColumnIndex(DataBaseConstants.TASK.COLUMNS.PRIORITYID))
                    val description =
                        cursor.getString(cursor.getColumnIndex(DataBaseConstants.TASK.COLUMNS.DESCRIPTION))
                    val dueDate =
                        cursor.getString(cursor.getColumnIndex(DataBaseConstants.TASK.COLUMNS.DUEDATE))
                    val complete =
                        (cursor.getInt(cursor.getColumnIndex(DataBaseConstants.TASK.COLUMNS.COMPLETE)) == 1)
                    list.add(TaskEntity(id, userId, priorityId, description, dueDate, complete))
                }
            }
            cursor.close()
        } catch (e: Exception) {
            return list
        }
        return list
    }


    fun insert(task:TaskEntity){
        try {
            //select update insert delete
            val db = mTaskDataBaseHelper.writableDatabase
            val complete :Int  = if(task.complete)1 else 0
            val insertValues = ContentValues()

            insertValues.put(DataBaseConstants.TASK.COLUMNS.USERID, task.userId)
            insertValues.put(DataBaseConstants.TASK.COLUMNS.PRIORITYID, task.priorityId)
            insertValues.put(DataBaseConstants.TASK.COLUMNS.DESCRIPTION, task.description)
            insertValues.put(DataBaseConstants.TASK.COLUMNS.DUEDATE, task.dueDate)
            insertValues.put(DataBaseConstants.TASK.COLUMNS.COMPLETE, complete)




            db.insert(DataBaseConstants.TASK.TABLE_NAME, null, insertValues)

        }catch (e:java.lang.Exception){
            throw e
        }
    }

    fun update(task:TaskEntity){
        try {
            //select update insert delete
            val db = mTaskDataBaseHelper.writableDatabase
            val complete :Int  = if(task.complete)1 else 0
            val updateValues = ContentValues()

            updateValues.put(DataBaseConstants.TASK.COLUMNS.USERID, task.userId)
            updateValues.put(DataBaseConstants.TASK.COLUMNS.PRIORITYID, task.priorityId)
            updateValues.put(DataBaseConstants.TASK.COLUMNS.DESCRIPTION, task.description)
            updateValues.put(DataBaseConstants.TASK.COLUMNS.DUEDATE, task.dueDate)
            updateValues.put(DataBaseConstants.TASK.COLUMNS.COMPLETE, complete)


            val selection = "${DataBaseConstants.TASK.COLUMNS.ID} = ? "


            val selectionArgs = arrayOf(task.id.toString())

            db.update(DataBaseConstants.TASK.TABLE_NAME,updateValues,selection,selectionArgs)
        }catch (e:java.lang.Exception){
            throw e
        }
    }

    fun delete(id:Int){
        try {
            val db = mTaskDataBaseHelper.writableDatabase

            val where = "${DataBaseConstants.TASK.COLUMNS.ID} = ? "


            val whereArgs = arrayOf(id.toString())

            db.delete(DataBaseConstants.TASK.TABLE_NAME,where,whereArgs)
        }catch (e:java.lang.Exception){
            throw e
        }
    }
}