package ru.quipy.controller.user

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ResponseStatusException
import ru.quipy.aggregate.user.*
import ru.quipy.api.user.UserChangedNameEvent
import ru.quipy.api.user.UserCreatedEvent
import ru.quipy.core.EventSourcingService
import ru.quipy.service.user.UserService
import java.lang.IllegalArgumentException
import java.util.*

@RestController
@RequestMapping("/users")
class UserController(
    val userEsService: EventSourcingService<UUID, UserAggregate, UserAggregateState>,
    val userService: UserService

) {
    @PostMapping
    fun addUser(@RequestParam name: String,
                @RequestParam nickname: String,
                @RequestParam password: String) : UserCreatedEvent {
        try {
            return userEsService.create { it.create(UUID.randomUUID(), name, nickname, password, userService) }
        } catch (e: UserAggregateError) {
            throw ResponseStatusException(HttpStatus.BAD_REQUEST, e.message)
        }
    }

    @GetMapping("{id}/tasks")
    fun getUserTasks(@PathVariable id: UUID) = userService.findUserTasks(id)

    @GetMapping("/nickname/{nickname}")
    fun checkIfNicknameExists(@PathVariable nickname: String) = userService.checkIfUserExists(nickname)

    @GetMapping("/substr/{field}/{substr}")
    fun findByNicknameSubstr(@PathVariable field: String, @PathVariable substr: String) =
        when (field) {
            "nickname" -> userService.findByNicknameSubstr(substr)
            "name" -> userService.findByNameSubstr(substr)
            else -> throw ResponseStatusException(HttpStatus.BAD_REQUEST, "Unexpected field: $field")
        }

    @PostMapping("/{userId}")
    fun changeUserName(@PathVariable userId: UUID,
                       @RequestParam newName: String) : UserChangedNameEvent {
        try {
            return userEsService.update(userId) {
                it.changeName(newName)
            }
        } catch (_: IllegalArgumentException) {
            throw ResponseStatusException(HttpStatus.NOT_FOUND)
        } catch (e: UserAggregateError) {
            throw ResponseStatusException(HttpStatus.BAD_REQUEST, e.message)
        }
    }
}