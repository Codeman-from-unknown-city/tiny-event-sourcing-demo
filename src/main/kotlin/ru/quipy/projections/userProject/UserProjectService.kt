package ru.quipy.projections.userProject

import org.springframework.stereotype.Service;
import ru.quipy.aggregate.project.ProjectAggregate
import ru.quipy.api.project.*
import ru.quipy.api.user.UserCreatedEvent
import ru.quipy.streams.annotation.AggregateSubscriber;
import ru.quipy.streams.annotation.SubscribeEvent;
import java.lang.IllegalStateException
import java.util.Optional
import java.util.UUID

@Service
@AggregateSubscriber(
    aggregateClass = ProjectAggregate::class,
    subscriberName = "user-project-sub"
)
class UserProjectService(
    private val userProjectRepository: UserProjectRepository,
) {
    @SubscribeEvent
    fun addUserToProject(event: AddUserToProjectEvent) {
        val user = userProjectRepository.findById(event.userId).get()
        if (user.projects.contains(event.projectId)){
            throw IllegalStateException("User already exist")
        }

        user.projects.add(event.projectId)
        userProjectRepository.save(user)
    }


    @SubscribeEvent
    fun addUserToProject(event: UserCreatedEvent) {
        userProjectRepository.save(UserProject(event.userId, ArrayList()))
    }

    fun getAllUserProjects(userId: UUID): List<UUID> {
        return userProjectRepository.findById(userId).get().projects;
    }
}