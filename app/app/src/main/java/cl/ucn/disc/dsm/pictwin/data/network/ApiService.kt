package cl.ucn.disc.dsm.pictwin.data.network

import cl.ucn.disc.dsm.pictwin.data.model.Persona
import cl.ucn.disc.dsm.pictwin.data.model.PicTwin
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface ApiService {

    @FormUrlEncoded
    @POST("api/personas")
    suspend fun authenticate(
        @Field("email") email: String,
        @Field("password") password: String
    ): Response<Persona>


    @GET("api/personas/{ulid}/pictwins")
    suspend fun getPicTwins(@Path("ulid") ulid: String): Response<List<PicTwin>>

}