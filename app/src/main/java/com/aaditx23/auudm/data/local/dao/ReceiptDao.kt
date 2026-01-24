package com.aaditx23.auudm.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.aaditx23.auudm.data.local.entity.ReceiptEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ReceiptDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(receipt: ReceiptEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(receipts: List<ReceiptEntity>)

    @Query("DELETE FROM receipts")
    suspend fun deleteAll()

    @Query("DELETE FROM receipts WHERE id = :id")
    suspend fun deleteById(id: String)

    @Query("SELECT * FROM receipts ORDER BY id DESC")
    fun getAllReceipts(): Flow<List<ReceiptEntity>>

    @Query("SELECT * FROM receipts WHERE " +
            "donorName LIKE '%' || :query || '%' OR " +
            "id LIKE '%' || :query || '%' " +
            "ORDER BY id DESC")
    fun searchReceipts(query: String): Flow<List<ReceiptEntity>>

    @Query("SELECT * FROM receipts WHERE " +
            "(donorName LIKE '%' || :query || '%' OR id LIKE '%' || :query || '%') " +
            "AND (:month IS NULL OR ',' || month || ',' LIKE '%' || ',' || :month || ',' || '%') " +
            "AND (:medium IS NULL OR medium = :medium) " +
            "ORDER BY id DESC")
    fun searchReceiptsWithFilters(
        query: String,
        month: Int?,
        medium: Int?
    ): Flow<List<ReceiptEntity>>

    @Query("SELECT * FROM receipts WHERE id = :id")
    fun getReceiptById(id: String): Flow<ReceiptEntity>

    @Query("UPDATE receipts SET isSynced = :synced WHERE id = :id")
    suspend fun updateSyncStatus(id: String, synced: Boolean)

    @Query("SELECT * FROM receipts WHERE isSynced = 0 ORDER BY id DESC")
    fun getUnsyncedReceipts(): Flow<List<ReceiptEntity>>
}
