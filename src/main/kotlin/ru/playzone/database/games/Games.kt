package ru.playzone.database.games

import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import ru.playzone.database.tokens.TokenDTO

object Games: Table() {
    val gameId = Games.varchar("gameId", 100)
    val name = Games.varchar("name", 100)
    val backdrop = Games.varchar("backdrop", 50).nullable()
    val logo = Games.varchar("logo", 50)
    val description = Games.varchar("description", 500)
    val downloadCount = Games.integer("download_count")
    val version = Games.varchar("version", 15)
    val weight = Games.varchar("weight", 10)

    //insert game
    fun insert(gameDTO: GameDTO) {
        transaction {
            Games.insert {
                it[gameId] = gameDTO.gameId
                it[name] = gameDTO.name
                it[backdrop] = gameDTO.backdrop
                it[logo] = gameDTO.logo
                it[description] = gameDTO.description
                it[downloadCount] = gameDTO.downloadCount
                it[version] = gameDTO.version
                it[weight] = gameDTO.weight
            }
        }
    }

    fun fetchGames(): List<GameDTO> {
        return try {
            transaction {
                Games.selectAll().toList().map {
                    GameDTO(
                        gameId = it[gameId],
                        name = it[name],
                        backdrop = it[backdrop],
                        logo = it[logo],
                        description = it[description],
                        downloadCount = it[downloadCount],
                        version = it[version],
                        weight = it[weight],
                    )
                }
            }
        } catch (e: Exception) {
            emptyList()
        }
    }
}