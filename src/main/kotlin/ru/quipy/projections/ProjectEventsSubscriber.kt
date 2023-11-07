package ru.quipy.projections

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import ru.quipy.api.project.MemberAssignedToTaskEvent
import ru.quipy.aggregate.project.ProjectAggregate
import ru.quipy.api.project.StatusCreatedEvent
import ru.quipy.api.project.TaskCreatedEvent
import ru.quipy.streams.AggregateSubscriptionsManager
import javax.annotation.PostConstruct

@Service
class ProjectEventsSubscriber {

    val logger: Logger = LoggerFactory.getLogger(ProjectEventsSubscriber::class.java)

    @Autowired
    lateinit var subscriptionsManager: AggregateSubscriptionsManager

    @PostConstruct
    fun init() {
        subscriptionsManager.createSubscriber(ProjectAggregate::class, "some-meaningful-name") {

            `when`(TaskCreatedEvent::class) { event ->
                logger.info("Task created: {}", event.name)
            }

            `when`(StatusCreatedEvent::class) { event ->
                logger.info("Status created: {}", event.statusName)
            }

            `when`(MemberAssignedToTaskEvent::class) { event ->
                logger.info("Task {} assigned to user {}: ", event.taskId, event.userId)
            }
        }
    }
}