package com.nutrino.paggingwithhilt.Data.Mappers

import com.nutrino.paggingwithhilt.Data.Local.UserEntity
import com.nutrino.paggingwithhilt.Data.Remote.User

fun User.toEntity(): UserEntity{
    return UserEntity(
        id = this.id,
        name = this.name,
        username = this.username,
        email = this.email
    )
}