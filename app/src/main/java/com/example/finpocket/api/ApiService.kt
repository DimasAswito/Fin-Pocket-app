package com.example.finpocket.api

import com.example.finpocket.model.Category
import com.example.finpocket.model.User
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path

interface ApiService {
    @POST("/users")
    suspend fun registerUser(@Body user: User): Response<Void>

    @Multipart
    @POST("/users/{id}/picture")
    suspend fun uploadProfilePicture(
        @Path("id") userId: String, @Part picture: MultipartBody.Part
    ): Response<Void>

    @GET("/categories")
    suspend fun getCategories(): Response<List<Category>>
}
