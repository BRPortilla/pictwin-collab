package cl.ucn.disc.dsm.pictwin.data.store

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import cl.ucn.disc.dsm.pictwin.data.model.Persona
import com.google.gson.Gson
import jakarta.inject.Inject
import kotlinx.coroutines.flow.first

/**
 * DataStore: Application.
 */
class ApplicationDataStore @Inject constructor(
    private val gson: Gson,
    private val dataStore: DataStore<Preferences>
) {
    /**
     * Key to store.
     */

    private object PreferenceKeys {
        val PERSONA_KEY = stringPreferencesKey("persona")
    }

    /**
     * Save the Persona.
     */
    suspend fun save(persona: Persona): Result <Persona> {
        val personaJson = gson.toJson(persona)
        dataStore.edit { preferences ->
            preferences[PreferenceKeys.PERSONA_KEY] = personaJson
        }
        return Result.success(persona)
    }

    /**
     * Retrieve the Persona.
     */
    suspend fun retrieve(): Result<Persona> {

        return try {

            val preferences = dataStore.data.first()
            val personaJson = preferences[PreferenceKeys.PERSONA_KEY]

            return if (personaJson == null) {
                Result.failure(NoSuchElementException("Persona not found"))
            } else {
                Result.success(gson.fromJson(personaJson, Persona::class.java))
            }

        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Clear the Persona.
     */
    suspend fun clear() {
        dataStore.edit { preferences ->
            preferences.remove(PreferenceKeys.PERSONA_KEY)
        }
    }

}