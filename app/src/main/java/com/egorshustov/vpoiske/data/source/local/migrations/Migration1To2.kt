package com.egorshustov.vpoiske.data.source.local.migrations

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

class Migration1To2 : Migration(1, 2) {
    override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL("ALTER TABLE 'users' ADD COLUMN 'found_unix_millis' INTEGER NOT NULL DEFAULT -1")
    }
}