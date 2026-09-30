package com.example.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface CloneDao {
    @Query("SELECT * FROM cloned_apps ORDER BY createdAt DESC")
    fun getAllClones(): Flow<List<CloneAppEntity>>

    @Query("SELECT * FROM cloned_apps WHERE id = :cloneId")
    suspend fun getCloneById(cloneId: Long): CloneAppEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertClone(clone: CloneAppEntity): Long

    @Update
    suspend fun updateClone(clone: CloneAppEntity)

    @Delete
    suspend fun deleteClone(clone: CloneAppEntity)

    @Query("UPDATE cloned_apps SET isRunning = :isRunning WHERE id = :cloneId")
    suspend fun updateRunningState(cloneId: Long, isRunning: Boolean)
}
