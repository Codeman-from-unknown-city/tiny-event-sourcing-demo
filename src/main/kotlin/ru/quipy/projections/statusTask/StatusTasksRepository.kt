package ru.quipy.projections.statusTask

import org.springframework.data.mongodb.repository.MongoRepository
import java.util.Optional
import java.util.UUID

interface StatusTasksRepository : MongoRepository<Status, UUID> {
    fun updateById(id: UUID, status: Status)
    fun findStatusByTasksContaining(taskId: UUID): Optional<Status>
}