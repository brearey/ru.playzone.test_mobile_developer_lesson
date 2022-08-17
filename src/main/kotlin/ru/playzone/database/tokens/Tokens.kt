package ru.playzone.database.tokens

import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction

object Tokens: Table() {
    val id = Tokens.varchar("id", 50)
    val login = Tokens.varchar("login", 25)
    val token = Tokens.varchar("token", 50)

    //insert token
    fun insert(tokenDTO: TokenDTO) {
        transaction {
            Tokens.insert {
                it[id] = tokenDTO.rowId
                it[login] = tokenDTO.login
                it[token] = tokenDTO.token
            }
        }
    }

    fun fetchTokens(): List<TokenDTO> {
        return try {
            transaction {
                Tokens.selectAll().toList().map {
                    TokenDTO(
                        rowId = it[Tokens.id],
                        login = it[Tokens.login],
                        token = it[Tokens.token]
                    )
                }
            }
        } catch (e: Exception) {
            emptyList()
        }
    }
}