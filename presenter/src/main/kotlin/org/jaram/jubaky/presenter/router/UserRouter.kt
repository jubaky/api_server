package org.jaram.jubaky.presenter.router

import io.ktor.application.call
import io.ktor.routing.Route
import io.ktor.routing.post
import io.ktor.sessions.sessions
import io.ktor.sessions.set
import org.jaram.jubaky.domain.session.UserSession
import org.jaram.jubaky.presenter.ext.bodyParam
import org.jaram.jubaky.presenter.ext.response
import org.jaram.jubaky.service.UserService

fun Route.user(userService: UserService) {
    post("/") {
        response(
            userService.registerUser(
                bodyParam("email_id"),
                bodyParam("password").toByteArray(),
                bodyParam("name"),
                bodyParam("group_name")
            )
        )
    }

    post("/login"){
        val emailId: String = bodyParam("email_id")
        val password: ByteArray = bodyParam("password").toByteArray()
        val userData: Map<String, String> = userService.loginUser(emailId, password)

//        val loginEmailId = userService.getLoginUserEmailId(userData["token"])
//
//        call.sessions.set(UserSession(emailId = loginEmailId))

        val responseUserData = mapOf("email_id" to userData["emailId"], "name" to userData["name"], "token" to userData["token"])

        response(
            responseUserData
        )
    }

    /*
    authenticate("jwtAuth") {

    }
     */
}