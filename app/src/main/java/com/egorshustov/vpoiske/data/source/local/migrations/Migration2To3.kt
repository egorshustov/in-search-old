package com.egorshustov.vpoiske.data.source.local.migrations

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

class Migration2To3 : Migration(2, 3) {
    override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL("ALTER TABLE 'users' ADD COLUMN 'photo_50' TEXT NOT NULL DEFAULT ''")
    }
}