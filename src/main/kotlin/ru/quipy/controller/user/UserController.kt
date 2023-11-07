package ru.quipy.controller.user

import org.slf4j.LoggerFactory
import org.springframework.dao.NonTransientDataAccessException
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ResponseStatusException
import ru.quipy.aggregate.user.*
import ru.quipy.api.user.UserChangedNameEvent
import ru.quipy.api.user.UserCreatedEvent
import ru.quipy.core.EventSourcingService
import ru.quipy.projections.user.User
import ru.quipy.projections.user.UserProjection
import java.lang.IllegalArgumentException
import java.util.*

@RestController
@RequestMapping("/users")
class UserController(
    val userEsService: EventSourcingService<UUID, UserAggregate, UserAggregateState>,
    val userProjection: UserProjection,

) {
    @PostMapping
    fun addUser(@RequestParam name: String,
                @RequestParam nickname: String,
                @RequestParam password: String) : UserCreatedEvent {
        try {
            return userEsService.create {
                it.create(UUID.randomUUID(), name, nickname, password,
                    userProjection)
            }
        } catch (e: UserAggregateError) {
            throw ResponseStatusException(HttpStatus.BAD_REQUEST, e.message)
        }
    }

    @GetMapping("/id/{userId}")
    fun checkIfIdExists(@PathVariable userId: UUID) : Boolean
        = userProjection.findById(userId).isPresent

    @GetMapping("/{userId}")
    fun getUser(@PathVariable userId: UUID) : User {
        val user = userProjection.findById(userId)
        if (user.isEmpty)
            throw ResponseStatusException(HttpStatus.NOT_FOUND)
        return user.get()
    }

    @GetMapping("/nickname/{nickname}")
    fun checkIfNicknameExists(@PathVariable nickname: String) : Boolean
        = userProjection.findByNickName(nickname).isPresent

    @GetMapping("/findByNicknameSubstr/{nicknameSubstr}")
    fun findByNicknameSubstr(@PathVariable nicknameSubstr: String) : List<User>
        = userProjection.findByNickNameSubs(nicknameSubstr)

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