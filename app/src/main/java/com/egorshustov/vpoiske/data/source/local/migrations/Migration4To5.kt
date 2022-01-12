package com.egorshustov.vpoiske.data.source.local.migrations

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

class Migration4To5 : Migration(4, 5) {
    override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL("ALTER TABLE 'searches' ADD COLUMN 'home_town' TEXT NOT NULL DEFAULT ''")
        database.execSQL("ALTER TABLE 'users' ADD COLUMN 'home_town' TEXT NOT NULL DEFAULT ''")
    }
}