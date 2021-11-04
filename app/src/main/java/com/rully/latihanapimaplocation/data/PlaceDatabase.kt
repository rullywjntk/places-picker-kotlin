package com.rully.latihanapimaplocation.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Place::class], version = 1)
abstract class PlaceDatabase : RoomDatabase() {
    abstract fun placeDao() : PlaceDao

    companion object {
        @Volatile
        private var INSTANCE: PlaceDatabase? = null

        @JvmStatic
        fun getDatabase(context: Context) : PlaceDatabase {
            if (INSTANCE == null) {
                synchronized(PlaceDatabase::class.java) {
                    INSTANCE = Room.databaseBuilder(context.applicationContext, PlaceDatabase::class.java, "place_database")
                        .build()
                }
            }
            return INSTANCE as PlaceDatabase
        }
    }
}