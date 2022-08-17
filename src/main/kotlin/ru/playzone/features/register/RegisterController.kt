package ru.playzone.features.register

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import org.jetbrains.exposed.exceptions.ExposedSQLException
import ru.playzone.database.tokens.TokenDTO
import ru.playzone.database.tokens.Tokens
import ru.playzone.database.users.UserDTO
import ru.playzone.database.users.Users
import ru.playzone.utils.isValidEmail
import java.lang.Exception
import java.util.*

class RegisterController(private val call: ApplicationCall) {

    suspend fun registerNewUser() {

        val registerReceiveRemote = call.receive<RegisterReceiveRemote>()
        if(!registerReceiveRemote.email.isValidEmail()) {
            call.respond(HttpStatusCode.BadRequest, "Email is not valid")
        }

        //fetch user
        val userDTO = Users.fetchUser(registerReceiveRemote.login)
        //check user exists
        if (userDTO != null) {
            call.respond(HttpStatusCode.Conflict, "User already exists")
        } else {
            val token = UUID.randomUUID().toString() //generate token

            try {
                //add new user in database
                Users.insert(UserDTO(
                    login = registerReceiveRemote.login,
                    password = registerReceiveRemote.password,
                    username = "",
                    email = registerReceiveRemote.email
                ))
            } catch (e: ExposedSQLException) {
                call.respond(HttpStatusCode.Conflict, "User already exists")
            } catch (e: Exception) {
                call.respond(HttpStatusCode.BadRequest, "Can't create user ${e.localizedMessage}")
            }

            //add token in database
            Tokens.insert(TokenDTO(
                rowId = UUID.randomUUID().toString(), //generate id for token in database
                login = registerReceiveRemote.login,
                token = token
            ))
            //return a token to client
            call.respond(RegisterResponseRemote(token = token))
        }
    }
}