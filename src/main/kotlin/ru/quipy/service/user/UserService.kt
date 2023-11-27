package ru.quipy.service.user

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import ru.quipy.projections.status.StatusProjection
import ru.quipy.projections.task.TaskProjection
import ru.quipy.projections.user.UserProjection
import ru.quipy.view.ProjectView
import ru.quipy.view.StatusView
import ru.quipy.view.TaskView
import ru.quipy.view.UserView
import java.util.*

@Service
class UserService {
    @Autowired
    private lateinit var userProjection: UserProjection

    @Autowired
    private lateinit var taskProjection: TaskProjection

    @Autowired
    private lateinit var statusProjection: StatusProjection

    @Autowired
    private lateinit var projectService: ProjectService

    fun findById(id: UUID): Optional<UserView> = UserView.fromProjection(userProjection.findById(id))

    fun findAllByIds(ids: Iterable<UUID>): MutableIterable<UserView> =
        UserView.fromProjection(userProjection.findAllById(ids))

    fun findByNameSubstr(nameSubs: String): MutableIterable<UserView> =
        UserView.fromProjection(userProjection.findAllByNameRegex(nameSubs))

    fun findByNicknameSubstr(nicknameSubs: String): MutableIterable<UserView> =
        UserView.fromProjection(userProjection.findAllByNicknameRegex(nicknameSubs))

    fun checkIfUserExists(nickname: String): Boolean = findByNickName(nickname).isPresent

    fun checkIfUserExists(id: UUID): Boolean = findById(id).isPresent

    fun findByNickName(nickname: String): Optional<UserView> =
        UserView.fromProjection(userProjection.findByNickname(nickname))

    fun findUserTasks(id: UUID): MutableIterable<TaskView> {
        val user = findById(id).get()
        return taskProjection.findAllByAssigneeId(id).map {
            val status =
                if (it.statusId != null)
                    StatusView.fromProjection(statusProjection.findById(it.statusId).get())
                else
                    null
            TaskView.fromProjection(it, user, status)
        }.toMutableList()
    }

    fun getUserProjects(id: UUID): List<ProjectView> {
        val projects =  projectService.getAllProjects()

        return projects.filter {
            it.projectMemberIds.contains(id)
        }
    }
}