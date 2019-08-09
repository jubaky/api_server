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

    post("/login"){
        val emailId: String = bodyParam("emailId")
        val password: ByteArray = bodyParam("password").toByteArray()
        val name: String = bodyParam("name")
        val userData: List<Any> = userService.loginUser(emailId, password, name)

        val responseData = mapOf("emailId" to userData[0], "name" to userData[1], "token" to userData[2])

        response(
            responseData
        )
    }
}