package ru.quipy.projections.userProject

import org.springframework.data.mongodb.repository.MongoRepository
import java.util.UUID

interface UserProjectRepository : MongoRepository<UserProject, UUID> {
}