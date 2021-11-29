package com.example.anitultimateteambuilder.database

import android.content.Context
import android.provider.ContactsContract
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

@Database(entities = [DataBasePlayer::class,DataBaseGameResult::class,DataBaseGameResultTeamOne::class,DataBaseGameResultTeamTwo::class], version = 6, exportSchema = false)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {

    abstract val DatabaseDao: DatabaseDao

    companion object {

        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            synchronized(this) {

                val MIGRATION_1_2 = object : Migration(1, 2) {
                    override fun migrate(database: SupportSQLiteDatabase) {
                        database.execSQL("ALTER TABLE players ADD COLUMN rarity INTEGER NOT NULL DEFAULT 0")
                    }
                }

                val MIGRATION_2_3 = object : Migration(2, 3){
                    override fun migrate(database: SupportSQLiteDatabase) {
                        database.execSQL("CREATE TABLE IF NOT EXISTS `game_results` (`id` INTEGER NOT NULL, `date` INTEGER NOT NULL, `teamOne` TEXT NOT NULL, `teamOneScore` INTEGER NOT NULL DEFAULT 0, `teamTwo` TEXT NOT NULL, `teamTwoScore` INTEGER NOT NULL DEFAULT 0,PRIMARY KEY(`id`))")
                    }
                }

                val MIGRATION_3_4 = object : Migration(3, 4){
                    override fun migrate(database: SupportSQLiteDatabase) {
                        // removing columns
                        database.execSQL("CREATE TABLE game_results_backup (id INTEGER NOT NULL, `date` INTEGER  NOT NULL, `teamOneScore` INTEGER  NOT NULL DEFAULT 0, `teamTwoScore` INTEGER  NOT NULL DEFAULT 0,  PRIMARY KEY (id))")
                        database.execSQL("INSERT INTO game_results_backup SELECT id, date, teamOneScore, teamTwoScore  FROM game_results")
                        database.execSQL("DROP TABLE game_results")
                        database.execSQL("ALTER TABLE game_results_backup RENAME to game_results")
                        // adding table
                        database.execSQL("CREATE TABLE IF NOT EXISTS `game_results_teams` (`id` INTEGER NOT NULL, `gameResultId` INTEGER NOT NULL, `teamNumber` INTEGER NOT NULL DEFAULT 0, `player` TEXT NOT NULL,PRIMARY KEY(`id`), FOREIGN KEY (gameResultId) REFERENCES game_results(id) ON UPDATE NO ACTION ON DELETE CASCADE , FOREIGN KEY (player) REFERENCES players(name) ON UPDATE NO ACTION ON DELETE NO ACTION)")
                    }
                }

                val MIGRATION_4_5 = object : Migration(4, 5){
                    override fun migrate(database: SupportSQLiteDatabase) {
                        // removing columns
                        database.execSQL("CREATE TABLE game_results_teams_backup (`id` INTEGER NOT NULL, `gameResultId` INTEGER NOT NULL, `player` TEXT NOT NULL,PRIMARY KEY(`id`), FOREIGN KEY (gameResultId) REFERENCES game_results(id) ON UPDATE NO ACTION ON DELETE CASCADE , FOREIGN KEY (player) REFERENCES players(name) ON UPDATE NO ACTION ON DELETE NO ACTION)")
                        database.execSQL("INSERT INTO game_results_teams_backup SELECT id, gameResultId, player  FROM game_results_teams")
                        database.execSQL("DROP TABLE game_results_teams")
                        database.execSQL("ALTER TABLE game_results_teams_backup RENAME to game_results_teams_one")
                        // adding table
                        database.execSQL("CREATE TABLE IF NOT EXISTS `game_results_teams_two` (`id` INTEGER NOT NULL, `gameResultId` INTEGER NOT NULL, `player` TEXT NOT NULL,PRIMARY KEY(`id`), FOREIGN KEY (gameResultId) REFERENCES game_results(id) ON UPDATE NO ACTION ON DELETE CASCADE , FOREIGN KEY (player) REFERENCES players(name) ON UPDATE NO ACTION ON DELETE NO ACTION)")
                    }
                }

                val MIGRATION_5_6 = object : Migration(5, 6) {
                    override fun migrate(database: SupportSQLiteDatabase) {
                    }
                }

                var instance = INSTANCE

                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        AppDatabase::class.java,
                        "app_database"
                    )
                        .addMigrations(MIGRATION_1_2)
                        .addMigrations(MIGRATION_2_3)
                        .addMigrations(MIGRATION_3_4)
                        .addMigrations(MIGRATION_4_5)
                        .addMigrations(MIGRATION_5_6)
//                        .fallbackToDestructiveMigration()
                        .build()
                    INSTANCE = instance
                }
                return instance
            }
        }
    }
}