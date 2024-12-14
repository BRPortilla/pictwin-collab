package cl.ucn.disc.dsm.pictwin.data.network

import com.google.gson.FieldNamingPolicy
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.slf4j.LoggerFactory
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    const val URL = "http://localhost:7000"

    /**
     * Provides the Gson instance.
     */
    @Provides
    @Singleton
    fun provideGson(): Gson {
        return GsonBuilder()
            .setPrettyPrinting()
            .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
            // .setDateFormat("yyyy-MM-dd'T'HH:mm:ss")
            .create()
    }

    /**
    Provides the [ApiService] instance.
     */
    @Provides
    @Singleton
    fun provideApiService(gson: Gson): ApiService {

        val log = LoggerFactory.getLogger(ApiService::class.java)

        val logging = HttpLoggingInterceptor { message -> log.debug(message) }.apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        val client = OkHttpClient.Builder()
            .addInterceptor(logging)
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl(URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .client(client)
            .build()

        return retrofit.create(ApiService::class.java)
    }
}