package com.example.submissiongithubapp.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface FavoriteUserDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(favUser: FavoriteUser)

    @Delete
    fun delete(favUser: FavoriteUser)

    @Query("SELECT * FROM favoriteuser WHERE username = :param")
    fun getUsersByUsername(param: String) : LiveData<FavoriteUser>

    @Query("SELECT * FROM favoriteuser ORDER BY username ASC")
    fun getAllFavUser(): LiveData<List<FavoriteUser>>
}