package ru.quipy.projections.user

import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.data.mongodb.repository.Query
import org.springframework.data.mongodb.repository.Update
import java.util.Optional
import java.util.UUID

interface UserProjection : MongoRepository<UserProjectionData, UUID> {
    fun findByName(name: String) : Optional<UserProjectionData>
    fun findByNickname(nickname: String) : Optional<UserProjectionData>
    fun findAllByNameRegex(nicknameRegex: String) : MutableIterable<UserProjectionData>
    fun findAllByNicknameRegex(nicknameRegex: String) : MutableIterable<UserProjectionData>
    @Query("{'id' : ?0}")
    @Update("{'\$set': {'name': ?1}}")
    fun updateUserNameById(id: UUID, name: String)
}