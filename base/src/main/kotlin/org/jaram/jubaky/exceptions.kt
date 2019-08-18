package org.jaram.jubaky

import com.fasterxml.jackson.databind.node.JsonNodeFactory
import com.fasterxml.jackson.databind.node.ObjectNode
import org.jaram.jubaky.protocol.BaseResponse
import java.util.*

open class ApiException(
    val code: Int,
    val messageKr: String = "기타오류가 발생되었습니다. 고객센터로 문의해주세요.",
    val messageEn: String = "Exceptional errors have occurred. Please contact us.",
    val httpStatus: Int = 500,
    message: String = messageKr,
    cause: Throwable? = null
) : RuntimeException("API-EXCEPTION($code) : $message", cause) {

    open fun toResponse(locale: Locale): BaseResponse<Any> {
        return if (Locale.ENGLISH == locale) {
            BaseResponse(code, messageEn)
        } else {
            BaseResponse(code, messageKr)
        }
    }

    open fun toJson(): ObjectNode {
        val node = JsonNodeFactory.instance.objectNode()

        node.put("code", code)
        node.put("message", message)

        if (cause != null) {
            node.put("cause", cause?.message)
        }

        return node
    }
}

// 1 ~ 99 : 통합 적인 오류
class UnhandledException(cause: Throwable, message: String? = null) : ApiException(
    code = 1,
    message = message ?: cause.message ?: "Unhandled Exception",
    cause = cause
)

class ServerException(message: String, cause: Throwable? = null) : ApiException(
    code = 2,
    message = message,
    cause = cause
)

class CryptoException(
    cause: Throwable
) : ApiException(
    code = 3,
    cause = cause
)

class MissingParameterException(message: String, cause: Throwable? = null) : ApiException(
    code = 4,
    message = message,
    httpStatus = 400,
    cause = cause
)

class IllegalParameterException(message: String, cause: Throwable? = null) : ApiException(
    code = 5,
    message = message,
    httpStatus = 400,
    cause = cause
)

class MaintenanceException : ApiException(
    code = 6,
    messageKr = "현재 점검중입니다. 잠시 후 다시 시도해주세요.",
    messageEn = "현재 점검중입니다. 잠시 후 다시 시도해주세요.",
    httpStatus = 400
)

class IllegalValueException(
    message: String,
    cause: Throwable? = null
) : ApiException(
    code = 7,
    message = message,
    httpStatus = 400,
    cause = cause
)

class UnauthorizedException(
    val token: String,
    cause: Throwable? = null
) : ApiException(
    code = 8,
    messageKr = "인증에 실패하였습니다. 고객센터로 문의해주세요.",
    messageEn = "인증에 실패하였습니다. 고객센터로 문의해주세요.",
    httpStatus = 401,
    cause = cause
) {
    override fun toJson(): ObjectNode {
        return super.toJson().apply {
            put("token", token)
        }
    }
}

class QueryEmptyResultException(
    cause: Throwable? = null
) : ApiException(
    code = 9,
    cause = cause,
    message = cause?.message ?: ""
)

class AlreadyExistedUserException(
    cause: Throwable? = null
) : ApiException(
    code = 11,
    cause = cause,
    messageKr = "이미 존재하는 이메일입니다.",
    messageEn = "The e-mail already exists."
)

class NonExistedUserException(
    cause: Throwable? = null
) : ApiException(
    code = 12,
    cause = cause,
    messageKr = "존재하지 않는 사용자입니다.",
    messageEn = "User does not exist."
)

class IncorrectPasswordException(
    cause: Throwable? = null
) : ApiException(
    code = 13,
    cause = cause,
    messageKr = "비밀번호를 잘못 입력 하셨습니다.",
    messageEn = "You entered an incorrect password."
)

class IncorrectEmailIdOrPasswordException(
    cause: Throwable? = null
) : ApiException(
    code = 14,
    cause = cause,
    messageKr = "이메일 또는 비밀번호를 잘못 입력 하셨습니다.",
    messageEn = "You entered an incorrect e-mail or password."
)

// 100 ~ 199 : Jenkins API 오류
fun createJenkinsApiStatusException(jenkinsHttpStatus: Int, cause: Throwable? = null): ApiException {
    val resultException: ApiException

    when (jenkinsHttpStatus) {
        400 -> resultException = JenkinsApiBadRequestException(cause)
        401 -> resultException = JenkinsApiUnauthorizedException(cause)
        403 -> resultException = JenkinsApiForbiddenException(cause)
        404 -> resultException = JenkinsApiNotFoundException(cause)
        500 -> resultException = JenkinsApiInternalServerException(cause)
        else -> resultException = JenkinsApiException(cause)
    }

    return resultException
}

class JenkinsApiException(cause: Throwable? = null) : ApiException(
    code = 100,
    messageKr = "젠킨스 API 에서 알 수 없는 오류가 발생하였습니다.",
    messageEn = "Jenkins API error has occurred.",
    cause = cause
)

class JenkinsApiBadRequestException(cause: Throwable? = null) : ApiException(
    code = 101,
    messageKr = "젠킨스 API 에서 오류가 발생하였습니다. (원인 : Bad Request)",
    messageEn = "Jenkins API bad request error has occurred.",
    cause = cause
)

class JenkinsApiUnauthorizedException(cause: Throwable? = null) : ApiException(
    code = 102,
    messageKr = "젠킨스 API 에서 오류가 발생하였습니다. (원인 : Authorization Error)",
    messageEn = "Jenkins API authorization error has occurred.",
    cause = cause
)

class JenkinsApiForbiddenException(cause: Throwable? = null) : ApiException(
    code = 103,
    messageKr = "젠킨스 API 에서 오류가 발생하였습니다. (원인 : Forbidden Error)",
    messageEn = "Jenkins API forbidden error has occurred.",
    cause = cause
)

class JenkinsApiNotFoundException(cause: Throwable? = null) : ApiException(
    code = 104,
    messageKr = "젠킨스 API 에서 오류가 발생하였습니다. (원인 : Not Found)",
    messageEn = "Jenkins API 'Not Found' error has occurred.",
    cause = cause
)

class JenkinsApiInternalServerException(cause: Throwable? = null) : ApiException(
    code = 105,
    messageKr = "젠킨스 API 에서 오류가 발생하였습니다. (원인 : Internal Server Error)",
    messageEn = "Jenkins API internal server error has occurred.",
    cause = cause
)

class JenkinsBuildDuplicationException : ApiException(
    code = 110,
    messageKr = "하나의 어플리케이션은 하나의 빌드만 대기할 수 있습니다.",
    messageEn = "An application can only wait for just one build."
)

// 200 ~ 299 : Kubernetes 오류
fun createKubernetesApiException(message: String?, cause: Throwable? = null): ApiException {
    val resultException: ApiException

    when (message) {
        "Conflict" -> resultException = KubernetesApiConflictException(cause)
        "Bad Request" -> resultException = KubernetesApiBadRequestException(cause)
        "Not Found" -> resultException = KubernetesApiNotFoundException(cause)
        else -> resultException = KubernetesApiException(message ?: "Unknown", cause)
    }

    return resultException
}

class KubernetesApiException(message: String, cause: Throwable? = null) : ApiException(
    code = 200,
    messageKr = "쿠버네티스 API 에서 알 수 없는 오류가 발생하였습니다. (원인 : $message)",
    messageEn = "Kubernetes API error has occurred. (Cause : $message)",
    cause = cause
)

class KubernetesApiBadRequestException(cause: Throwable? = null) : ApiException(
    code = 201,
    messageKr = "쿠버네티스 API 에서 오류가 발생하였습니다. (원인 : Bad Request)",
    messageEn = "Kubernetes API bad request error has occurred.",
    cause = cause
)

class KubernetesApiNotFoundException(cause: Throwable? = null) : ApiException(
    code = 204,
    messageKr = "쿠버네티스 API 에서 오류가 발생하였습니다. (원인 : Not Found)",
    messageEn = "Kubernetes API 'Not Found' error has occurred.",
    cause = cause
)

class KubernetesApiConflictException(cause: Throwable? = null) : ApiException(
    code = 205,
    messageKr = "쿠버네티스 API 에서 오류가 발생하였습니다. (원인: Conflict)",
    messageEn = "Kubernetes API conflict error has occurred.",
    cause = cause
)