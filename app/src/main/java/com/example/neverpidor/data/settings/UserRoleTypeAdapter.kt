package com.example.neverpidor.data.settings

import com.example.neverpidor.domain.model.User
import com.google.gson.TypeAdapter
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonWriter
import java.io.IOException

class UserRoleTypeAdapter : TypeAdapter<User.Role>() {
    override fun write(out: JsonWriter, value: User.Role) {
        out.beginObject()
        out.name("name").value(value.name)
        out.endObject()
    }

    override fun read(`in`: JsonReader): User.Role {
        var name = ""
        `in`.beginObject()
        while (`in`.hasNext()) {
            when (`in`.nextName()) {
                "name" -> name = `in`.nextString()
                else -> `in`.skipValue()
            }
        }
        `in`.endObject()
        return when (name) {
            "noUser" -> User.Role.NoUser
            "admin" -> User.Role.Admin
            "customer" -> User.Role.Customer
            else -> throw IOException("Invalid role!")
        }
    }
}