package com.nutrino.paggingwithhilt.Data.Mappers

import com.nutrino.paggingwithhilt.Data.Local.UserEntity
import com.nutrino.paggingwithhilt.Data.Remote.Address
import com.nutrino.paggingwithhilt.Data.Remote.Company
import com.nutrino.paggingwithhilt.Data.Remote.Geo
import com.nutrino.paggingwithhilt.Data.Remote.User

fun UserEntity.toDomian(): User {
    return User(
        id = this.id ,
        name = this.name,
        username = this.username,
        email = this.email,
        address = Address("", geo = Geo("", ""), street = "", suite = "", zipcode = ""),
        phone = "",
        website = "",
        company = Company("","","")
    )

}