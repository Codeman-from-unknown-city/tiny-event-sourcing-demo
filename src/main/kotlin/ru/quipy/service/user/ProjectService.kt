package ru.quipy.service.user

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.web.server.ResponseStatusException
import ru.quipy.projections.project.ProjectProjection
import ru.quipy.projections.user.UserProjection
import ru.quipy.view.ProjectView
import ru.quipy.view.UserView
import java.util.*

@Service
class ProjectService {
    @Autowired
    private lateinit var userProjection: UserProjection

    @Autowired
    private lateinit var projectProjection: ProjectProjection

    @Autowired
    private lateinit var userService: UserService

    fun findById(id: UUID): Optional<ProjectView> = ProjectView.fromProjection(projectProjection.findById(id))

    fun getAllMembersInProject(id: UUID): MutableIterable<UserView> {
        val project = findById(id).get()
        return project.projectMemberIds.map {
            UserView.fromProjection(userProjection.findById(it).get())
        }.toMutableList()
    }

    fun getUserInProjectByNameOrNick(id: UUID, field: String, substr: String): MutableList<UserView?> {
        val project = findById(id).get()
        val users = when (field) {
            "nickname" -> userService.findByNicknameSubstr(substr)
            "name" -> userService.findByNameSubstr(substr)
            else -> throw ResponseStatusException(HttpStatus.BAD_REQUEST, "Unexpected field: $field")
        }

        return users.map {
            if (project.projectMemberIds.contains(it.id)){
                UserView.fromProjection(userProjection.findById(it.id).get())
            }
            else{
                null
            }
        }.toMutableList()
    }

    fun getAllProjects(): MutableList<ProjectView> {
        return ProjectView.fromProjection(projectProjection.findAll())
    }
}