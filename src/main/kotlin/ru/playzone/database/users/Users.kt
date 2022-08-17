package ru.playzone.database.users

import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction

object Users: Table() {
    //Columns from Database
    private val login = Users.varchar("login", 25)
    private val password = Users.varchar("password", 25)
    private val username = Users.varchar("username", 25)
    private val email = Users.varchar("email", 25).nullable()

    //CRUD
    fun insert(userDTO: UserDTO) {
        transaction {
            Users.insert {
                it[login] = userDTO.login
                it[password] = userDTO.password
                it[email] = userDTO.email
                it[username] = userDTO.username
            }
        }
    }

    fun fetchUser(login: String): UserDTO? {
        return try {
            transaction {
                val user = Users.select { Users.login.eq(login) }.single()
                UserDTO(
                    login = user[Users.login],
                    password = user[Users.password],
                    email = user[Users.email],
                    username = user[Users.username]
                )
            }
        } catch (e: Exception) {
            null
        }
    }
}