package org.jaram.jubaky.service

import com.auth0.jwt.JWT
import com.auth0.jwt.JWTVerifier
import com.auth0.jwt.algorithms.Algorithm
import java.util.*

class TokenService(
    private val jwtIssuer: String,
    private val secret: String,
    private val jwtAudience: String,
    private val validityInMs: Int
){
    private val algorithm = Algorithm.HMAC512(this.secret)

    fun createToken(emailId: String, name: String) = JWT.create()
            .withSubject("Authentication")
            .withIssuer(jwtIssuer)
            .withAudience(jwtAudience)
            .withClaim("emailId", emailId)
            .withClaim("name", name)
            .withExpiresAt(getExpiration())
            .sign(algorithm)

    fun getExpiration() = Date(System.currentTimeMillis() + validityInMs)

    fun getJWTVerifier(): JWTVerifier {
        return JWT.require(algorithm).withIssuer(jwtIssuer).build()
    }
}