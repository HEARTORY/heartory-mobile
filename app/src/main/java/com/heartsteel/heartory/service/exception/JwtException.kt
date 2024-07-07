package com.heartsteel.heartory.service.exception

class JwtException(
    override val message: String
): RuntimeException(message) {

}