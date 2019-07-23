package org.jaram.jubaky.presenter.router

import io.ktor.routing.Route
import io.ktor.routing.post
import org.jaram.jubaky.presenter.ext.bodyParam
import org.jaram.jubaky.presenter.ext.response
import org.jaram.jubaky.service.UserService

fun Route.user(userService: UserService) {
    post("/") {
        response(
            userService.registerUser(
                bodyParam("email_id"),
                bodyParam("password").toByteArray(),
                bodyParam("name")
            )
        )
    }
}