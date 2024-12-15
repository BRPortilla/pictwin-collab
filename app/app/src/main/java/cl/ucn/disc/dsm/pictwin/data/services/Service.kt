package cl.ucn.disc.dsm.pictwin.data.services

import cl.ucn.disc.dsm.pictwin.data.model.Persona
import cl.ucn.disc.dsm.pictwin.data.repository.Repository
import cl.ucn.disc.dsm.pictwin.data.store.ApplicationDataStore
import javax.inject.Inject

/**
 * Main Service.
 */
class Service @Inject constructor(
    private val repository: Repository,
    private val store: ApplicationDataStore,
) {

    /**
     * Retrieve the Persona.
     */
    suspend fun retrieve(): Result<Persona> = store.retrieve()

    /**
     * Refresh the current Persona.
     */

    /**
    suspend fun refresh(): Result<Persona> = kotlin.runCatching {
        val localPersona = store.retrieve().getOrThrow()

        //FIXME: add the fetch method.
        val updatedPersona = repository.fetch(localPersona.ulid).getOrElse {
            //If repository fetch fails, return local persona.
            return@runCatching localPersona
        }
        store.save(updatedPersona)
        updatedPersona
    }
    */

    /**
     * Clear the data.
     */
    suspend fun clear() {
        store.clear()
    }
}