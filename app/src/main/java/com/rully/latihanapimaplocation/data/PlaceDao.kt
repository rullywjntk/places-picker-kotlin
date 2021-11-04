package com.rully.latihanapimaplocation.data

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface PlaceDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(place: Place)

    @Update
    fun update(place: Place)

    @Delete
    fun delete(place: Place)

    @Query("SELECT * FROM place ORDER BY id ASC")
    fun getAllPlaces(): LiveData<List<Place>>
}