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
        val emailId: String = bodyParam("email_id")
        val password: ByteArray = bodyParam("password").toByteArray()
        val name: String = bodyParam("name")
        val userData: Map<String, Any> = userService.loginUser(emailId, password, name)

        val responseUserData: Map<String, Any?> = mapOf("email_id" to userData["emailId"], "name" to userData["name"], "token" to userData["token"])

        response(
            responseUserData
        )
    }
}