package ru.quipy.controller.project

import org.springframework.web.bind.annotation.*
import ru.quipy.api.project.*
import ru.quipy.core.EventSourcingService
import ru.quipy.aggregate.project.*
import ru.quipy.projections.project.ProjectProjection
import ru.quipy.projections.task.TaskProjection
import ru.quipy.projections.user.UserProjection
import ru.quipy.service.user.ProjectService
import java.util.*

@RestController
@RequestMapping("/projects")
class ProjectController(
    val projectEsService: EventSourcingService<UUID, ProjectAggregate, ProjectAggregateState>,
    val projectService: ProjectService,
    val taskProjection: TaskProjection,
    val projectProjection: ProjectProjection,
    val userProjection: UserProjection
) {

    @PostMapping("/{projectTitle}")
    fun createProject(@PathVariable projectTitle: String, @RequestParam creatorId: UUID) : ProjectCreatedEvent {
        return projectEsService.create { it.create(UUID.randomUUID(), projectTitle, creatorId) }
    }

    @GetMapping("/{projectId}")
    fun getProject(@PathVariable projectId: UUID) = projectService.findById(projectId)


    @PostMapping("/{projectId}/users/{userId}")
    fun addUserToProject(@PathVariable projectId: UUID, @PathVariable userId: UUID) : AddUserToProjectEvent {
        return projectEsService.update(projectId) { it.addUser(projectId, userId, projectProjection) }
    }

    @GetMapping("/{projectId}/users")
    fun getUsersInProject(@PathVariable projectId: UUID)  = projectService.getAllMembersInProject(projectId)

    @GetMapping("/{projectId}/users/{field}/{substr}")
    fun findUserInProject(@PathVariable projectId: UUID, @PathVariable field: String, @PathVariable substr: String) = projectService.getUserInProjectByNameOrNick(projectId, field, substr)

    @PatchMapping("/{projectId}/changeTitle/{newTitle}")
    fun changeProjectTitle(@PathVariable projectId: UUID, @PathVariable newTitle: String): ProjectTitleChangedEvent {
        return projectEsService.update(projectId){
            it.changeTitle(projectId, newTitle)
        }
    }

    @PostMapping("/{projectId}/status")
    fun createStatus(@PathVariable projectId: UUID, @RequestBody title: String, @RequestBody color: String) : StatusCreatedEvent {
        return projectEsService.update(projectId) { it.addStatus (title, color) }
    }

    @DeleteMapping("/{projectId}/status/{statusId}")
    fun removeStatus(@PathVariable projectId: UUID, @PathVariable statusId: UUID): StatusDeletedEvent {
        return projectEsService.update(projectId){ it.removeStatus(statusId, projectId, taskProjection, projectProjection) }
    }


    @PostMapping("/{projectId}/tasks/{taskName}")
    fun createTask(@PathVariable projectId: UUID, @PathVariable taskName: String) : TaskCreatedEvent {
        return projectEsService.update(projectId) {
            it.addTask(taskName)
        }
    }

    @PatchMapping("/{projectId}/tasks/{taskId}/changeTitle/{newTitle}")
    fun changeTaskTitle(@PathVariable projectId: UUID, @PathVariable taskId: UUID, @PathVariable newTitle: String): TaskTitleChangedEvent {
        return projectEsService.update(projectId){ it.changeTaskTitle(taskId, newTitle) }
    }

    @PatchMapping("/{projectId}/tasks/{taskId}/changeStatus/{newStatusId}")
    fun changeTaskStatus(@PathVariable projectId: UUID, @PathVariable taskId: UUID, @PathVariable newStatusId: UUID): TaskStatusChangedEvent {
        return projectEsService.update(projectId){ it.changeTaskStatus(taskId, newStatusId, projectProjection) }
    }

    @PatchMapping("/{projectId}/tasks/{taskId}/assign/{userId}")
    fun memberAssignToTask(@PathVariable projectId: UUID, @PathVariable taskId: UUID, @PathVariable userId: UUID) : MemberAssignedToTaskEvent {
        return projectEsService.update(projectId){ it.memberAssignedToTask(userId, taskId, taskProjection, userProjection) }
    }
}