package com.nutrino.paggingwithhilt.Data.Local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [UserEntity::class], version = 1)
abstract class UserDatabase: RoomDatabase() {
    abstract fun UserDao(): UserDao
}