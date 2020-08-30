package yutori.tf.hangul.network;

import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.http.*
import yutori.tf.hangul.data.GetSentenceResponse
import yutori.tf.hangul.data.PostCheckResponse
import yutori.tf.hangul.data.PostLoginResponse

interface NetworkService {

    @POST("/api/user/signup")
    fun postJoinResponse(
            @Body() body: JsonObject
    ): Call<PostLoginResponse>

    @POST("/api/user/login")
    fun postLoginResponse(
            @Body() body: JsonObject
    ): Call<PostLoginResponse>

    @GET("/api/check/getSentence")
    fun getSentenceResponse(
            @Query("sentenceTypes") sentenceTypes: String?,
            @Query("levelTypes") levelTypes: String?,
            @Query("numTypes") numTypes: String?
    ): Call<List<GetSentenceResponse>>

    @POST("/api/check/checkSentence")
    fun postCheckResponse(
            @Body() body: JsonObject
    ): Call<PostCheckResponse>

}
