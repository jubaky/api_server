package org.jaram.jubaky.presenter.router

import io.ktor.application.call
import io.ktor.auth.authenticate
import io.ktor.response.header
import io.ktor.routing.Route
import io.ktor.routing.post
import org.jaram.jubaky.presenter.ext.bodyParam
import org.jaram.jubaky.presenter.ext.response
import org.jaram.jubaky.service.UserService

fun Route.user(userService: UserService) {
    post("/") {
        userService.registerUser(
            bodyParam("emailId"),
            bodyParam("password").toByteArray(),
            bodyParam("name")
        )
//        response(
//            userService.registerUser(
//                bodyParam("email_id"),
//                bodyParam("password").toByteArray(),
//                bodyParam("name")
//            )
//        )
    }

    authenticate("jwtAuth") {
        post("/login"){
            val emailId = bodyParam("emailId")
            val password = bodyParam("password").toByteArray()
            val name = bodyParam("name")
            val userData = userService.loginUser(emailId, password, name)

            call.response.header("token", userData.token) // jwt
            val responseData = mapOf("emailId" to userData.emailId, "name" to userData.name)

            response(
                responseData
            )
        }
    }

    /*
    post("/login") {
        val emailId = bodyParam("emailId")
        val password = bodyParam("password").toByteArray()
        val name = bodyParam("name")
        val userData = userService.loginUser(emailId, password, name)

        call.response.header("token", userData.token) // jwt
        val responseData = mapOf("emailId" to userData.emailId, "name" to userData.name)

        response(
            responseData
        )
    }
     */
}