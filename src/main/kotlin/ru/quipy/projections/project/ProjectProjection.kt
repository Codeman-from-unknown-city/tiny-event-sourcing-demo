package ru.quipy.projections.project

import org.springframework.data.mongodb.repository.MongoRepository
import java.util.UUID

interface ProjectProjection : MongoRepository<ProjectProjectionData, UUID> {
}