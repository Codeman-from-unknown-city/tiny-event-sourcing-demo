package ru.quipy.projections.user

import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.data.mongodb.repository.Query
import org.springframework.data.mongodb.repository.Update
import java.util.Optional
import java.util.UUID

interface UsersRepository : MongoRepository<User, UUID> {
    fun findByName(name: String) : Optional<User>
    fun findByNickname(nickname: String) : Optional<User>
    fun findAllByNameRegex(nicknameRegex: String) : List<User>
    fun findAllByNicknameRegex(nicknameRegex: String) : List<User>
    @Query("{'id' : ?0}")
    @Update("{'\$set': {'name': ?1}}")
    fun updateUserNameById(id: UUID, name: String)
}