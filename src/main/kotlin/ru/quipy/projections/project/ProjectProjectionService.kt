package ru.quipy.projections.project

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import ru.quipy.aggregate.project.ProjectAggregate
import ru.quipy.api.project.*
import ru.quipy.streams.annotation.AggregateSubscriber
import ru.quipy.streams.annotation.SubscribeEvent
import java.util.*
import kotlin.collections.ArrayList

@Service
@AggregateSubscriber(ProjectAggregate::class, "project-projection-service")
class ProjectProjectionService {
    @Autowired
    lateinit var projectProjection: ProjectProjection

    @SubscribeEvent
    fun create(event: ProjectCreatedEvent) = with(event) {
        projectProjection.insert(ProjectProjectionData(id = event.projectId,
                title = event.title, creatorId = event.creatorId, projectMemberIds = ArrayList<UUID>(),
                defaultStatus = event.defaultStatus.id, tasks = ArrayList<UUID>(), projectStatus = arrayListOf(event.defaultStatus.id)))
    }

    @SubscribeEvent
    fun changeTitle(event: ProjectTitleChangedEvent) = with(event) {
        val project = projectProjection.findById(event.projectId).get();
        project.title = event.title;
        projectProjection.save(project)
    }

    @SubscribeEvent
    fun addUser(event: AddUserToProjectEvent) = with(event) {
        val project = projectProjection.findById(event.projectId).get()
        project.projectMemberIds.add(event.userId)
        projectProjection.save(project)
    }

    @SubscribeEvent
    fun addTask(event: TaskCreatedEvent) = with(event) {
        val project = projectProjection.findById(event.projectId).get()
        project.tasks.add(event.taskId)
        projectProjection.save(project)
    }

    @SubscribeEvent
    fun addStatus(event: StatusCreatedEvent) = with(event) {
        val project = projectProjection.findById(event.projectId).get()
        project.projectStatus.add(event.statusId)
        projectProjection.save(project)
    }

    @SubscribeEvent
    fun removeStatus(event: StatusDeletedEvent) = with(event) {
        val project = projectProjection.findById(event.projectId).get()
        if (project.defaultStatus == event.statusId){
            throw IllegalArgumentException("Try to delete default status")
        }
        project.projectStatus.remove(event.statusId)
        projectProjection.save(project)
    }
}