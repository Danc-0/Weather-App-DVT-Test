package com.danc.weatherdvt.data.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.danc.weatherdvt.domain.models.local.SavedWeatherItem
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Database(entities = arrayOf(SavedWeatherItem::class), version = 1, exportSchema = false)
abstract class WeatherDatabase : RoomDatabase() {

    abstract fun weatherDao(): WeatherDao

    private class WeatherDatabaseCallback(
        private val scope: CoroutineScope
    ) : RoomDatabase.Callback() {

        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            INSTANCE?.let { database ->
                scope.launch {
                    populateDatabase(database.weatherDao())
                }
            }
        }

        suspend fun populateDatabase(weatherDao: WeatherDao) {
            weatherDao.deleteAll()
            val localWeatherItem = SavedWeatherItem(
                0,
                "",
                "",
                0.0,
                0.0,
                0,
                0,
                0,
                0.0,
                0.0,
                0.0
            )
            weatherDao.addFavourite(localWeatherItem)

        }

        companion object {

            @Volatile
            private var INSTANCE: WeatherDatabase? = null

            fun getWeatherDatabase(
                context: Context,
                applicationScope: CoroutineScope
            ): WeatherDatabase {
                return INSTANCE ?: synchronized(this) {
                    val instance = Room.databaseBuilder(
                        context.applicationContext,
                        WeatherDatabase::class.java,
                        "weather_database"
                    )
                        .fallbackToDestructiveMigration()
                        .addCallback(WeatherDatabaseCallback(applicationScope))
                        .build()
                    INSTANCE = instance
                    instance
                }
            }
        }
    }
}

