package ru.playzone.database.users

import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction

object Users: Table() {
    val login = Users.varchar("login", 25)
    val password = Users.varchar("password", 25)
    val username = Users.varchar("username", 25)
    val email = Users.varchar("email", 25)

    //create user
    fun insert(user: UserDTO) {
        transaction {
            Users.insert {
                it[login] = user.login
                it[password] = user.password
                it[username] = user.username
                it[email] = user.email ?: "default_email"
            }
        }
    }

    //find user
    fun fetchUser(login: String): UserDTO? {
        return try {
            transaction {
                val receivedUser = Users.select { Users.login.eq(login) }.single()
                UserDTO(
                    login = receivedUser[Users.login],
                    password = receivedUser[Users.password],
                    username = receivedUser[Users.username],
                    email = receivedUser[Users.email]
                )
            }
        }
        catch (e: Exception) {
            null
        }
    }
}