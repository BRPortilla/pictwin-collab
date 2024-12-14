package cl.ucn.disc.dsm.pictwin.data.repository

import cl.ucn.disc.dsm.pictwin.data.model.Persona
import cl.ucn.disc.dsm.pictwin.data.model.PicTwin
import cl.ucn.disc.dsm.pictwin.data.network.ApiService
import org.slf4j.LoggerFactory
import org.slf4j.Logger
import javax.inject.Inject


class Repository @Inject constructor(
    private val apiService: ApiService,
    private val log: Logger = LoggerFactory.getLogger(Repository::class.java)
){
    suspend fun authenticate(email: String, password: String): Result<Persona> {

        //validate the input
        require(email.isNotBlank()) { "Email cannot be blank" }
        require(password.length >= 6) { "Password needs to be at least 6 characters long" }

        return runCatching {
            log.debug("Authenticating user with rut/email: $email ..")

            val response = apiService.authenticate(email, password)

            when {
                // 200 OK
                response.isSuccessful -> {
                    response.body() ?: throw IllegalStateException("Empty response body")
                }
                else -> {
                    val errorMessage = "Authentication failed: ${response.code()} -> ${response.errorBody()?.string()}"
                    log.error(errorMessage)
                    throw RuntimeException(errorMessage)
                }
            }
        }.onFailure { authError ->
            log.error("Authentication failed", authError)
        }
    }

    suspend fun getPicTwins(ulid: String): Result<List<PicTwin>> {

        // validate the input
        require(ulid.isNotBlank()) { "ULID cannot be blank" }

        return try {
            log.debug("Retrieving PicTwins for user with ULID: $ulid ..")

            val response = apiService.getPicTwins(ulid)

            when (response.code()) {

                // 200 OK
                200 -> {
                    val picTwins = response.body()
                    log.debug("Retrieveed PicTwins: {}", picTwins)

                    // maybe the picTwins is null?
                    if (picTwins == null) {
                        return Result.failure((RuntimeException("PicTwins is null")))
                    }
                    return Result.success(picTwins)
                }

                else -> {
                    val error = "Failed to retrieve PicTwins: code ${response.code()} -> ${response.code()}"
                    log.error(error)
                    return Result.failure(RuntimeException(error))
                }
            }

        } catch (e: Exception) {
            log.error("Failed to retrieve PicTwins: ${e.message}")
            return Result.failure(e)
        }
    }
}