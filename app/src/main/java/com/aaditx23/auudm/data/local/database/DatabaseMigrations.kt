package com.aaditx23.auudm.data.local.database

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

val MIGRATION_1_2 = object : Migration(1, 2) {
    override fun migrate(db: SupportSQLiteDatabase) {
        // Add createdAt column with default value of current timestamp
        db.execSQL("ALTER TABLE receipts ADD COLUMN createdAt INTEGER NOT NULL DEFAULT 0")
    }
}

