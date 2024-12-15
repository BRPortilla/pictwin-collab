package cl.ucn.disc.dsm.pictwin.data.repository

import cl.ucn.disc.dsm.pictwin.data.network.NetworkModule
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import org.slf4j.LoggerFactory

class RepositoryTest {

    private var log = LoggerFactory.getLogger(Repository::class.java)

    private lateinit var repository: Repository

    @Before
    fun setup() {
        val gson = NetworkModule.provideGson()
        val api = NetworkModule.provideApiService(gson)

        repository = Repository(api)
    }

    @Test
    fun testAuthenticate() {
        runBlocking {
            log.debug("Testing authenticate() ...")

            val result = repository.authenticate("durrutia@ucn.cl","durrutia123")

            result.onSuccess {
                log.debug("Authentication successful: {}",it)
            }.onFailure {
                log.error("Authentication successful",it)
            }
        }
    }







}