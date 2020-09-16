package yutori.tf.hangul.network;

import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.http.*
import yutori.tf.hangul.data.*

interface NetworkService {

    //########### Member Controller ###########
    @POST("/api/user/signup")
    fun postJoinResponse(
            @Body() body: JsonObject
    ): Call<Void>

    @POST("/api/user/login")
    fun postLoginResponse(
            @Body() body: JsonObject
    ): Call<PostLoginResponse>

    @DELETE("/api/user/deleteMember")
    fun deleteMemberResponse(
            @Header("authorization") authorization: String?,
            @Query("userId") userId: Long?
    ): Call<Void>



    //########### Sentence Controller ###########
    @GET("/api/check/getSentence")
    fun getSentenceResponse(
            @Header("authorization") authorization: String?,
            @Query("sentenceTypes") sentenceTypes: String?,
            @Query("levelTypes") levelTypes: String?,
            @Query("numTypes") numTypes: String?
    ): Call<List<GetSentenceResponse>>

    @POST("/api/check/checkSentence")
    fun postCheckResponse(
            @Header("authorization") authorization: String?,
            @Body() body: JsonObject
    ): Call<PostCheckResponse>

    @GET("/api/check/wrongSentence")
    fun getWrongResponse(
            @Header("authorization") authorization: String?,
            @Query("userId") userId: Long?,
            @Query("sentenceId") sentenceId: Long?
    ): Call<GetWrongResponse>

    @POST("/api/check/savePractice")
    fun postPracticeResponse(
            @Header("authorization") authorization: String?,
            @Body() body: JsonObject
    ): Call<Void>


    //########### Mypage Controller ###########
    @GET("/api/mypage/getProfile")
    fun getProfileResponse(
            @Header("authorization") authorization: String?,
            @Query("userId") userId: Long?
    ): Call<GetProfileResponse>

    @GET("/api/mypage/getExamRecord")
    fun getExamRecordResponse(
            @Header("authorization") authorization: String?,
            @Query("userId") userId: Long?
    ): Call<ArrayList<GetExamRecordResponse>>

    @GET("/api/mypage/getPracticeRecord")
    fun getPracticeRecordResponse(
            @Header("authorization") authorization: String?,
            @Query("userId") userId: Long?
    ): Call<ArrayList<GetPracticeRecordResponse>>

}
