package com.aaditx23.auudm.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.aaditx23.auudm.data.local.dao.ReceiptDao
import com.aaditx23.auudm.data.local.entity.ReceiptEntity

@Database(entities = [ReceiptEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase() {

    abstract fun receiptDao(): ReceiptDao

}
