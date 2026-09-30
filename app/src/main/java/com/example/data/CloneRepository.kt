package com.example.data

import kotlinx.coroutines.flow.Flow

class CloneRepository(private val cloneDao: CloneDao) {
    val allClones: Flow<List<CloneAppEntity>> = cloneDao.getAllClones()

    suspend fun getCloneById(id: Long): CloneAppEntity? = cloneDao.getCloneById(id)

    suspend fun insertClone(clone: CloneAppEntity): Long = cloneDao.insertClone(clone)

    suspend fun updateClone(clone: CloneAppEntity) = cloneDao.updateClone(clone)

    suspend fun deleteClone(clone: CloneAppEntity) = cloneDao.deleteClone(clone)

    suspend fun setRunningState(id: Long, isRunning: Boolean) = cloneDao.updateRunningState(id, isRunning)
}
