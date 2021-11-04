package com.rully.latihanapimaplocation.helper

import android.content.Context
import androidx.lifecycle.LiveData
import com.rully.latihanapimaplocation.data.Place
import com.rully.latihanapimaplocation.data.PlaceDao
import com.rully.latihanapimaplocation.data.PlaceDatabase
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class DatabaseHelper(context: Context) {

    private val placeDao: PlaceDao

    private val executorService: ExecutorService = Executors.newSingleThreadExecutor()

    init {
        val db = PlaceDatabase.getDatabase(context)
        placeDao = db.placeDao()
    }

    fun getAll(): LiveData<List<Place>> = placeDao.getAllPlaces()

    fun insert(place: Place) {
        executorService.execute { placeDao.insert(place) }
    }

    fun update(place: Place) {
        executorService.execute { placeDao.update(place) }
    }

    fun delete(place: Place) {
        executorService.execute { placeDao.delete(place) }
    }

}