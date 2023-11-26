package ru.quipy.projections.status

import org.springframework.data.mongodb.repository.MongoRepository
import java.util.UUID

interface ProjectProjection : MongoRepository<ProjectProjectionData, UUID> {
}