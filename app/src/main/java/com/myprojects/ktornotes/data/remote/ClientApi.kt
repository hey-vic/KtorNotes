package com.myprojects.ktornotes.data.remote

import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface ClientApi {

    @POST("/signup")
    suspend fun signUp(
        @Body request: AuthRequest
    )

    @POST("/signin")
    suspend fun signIn(
        @Body request: AuthRequest
    ): TokenResponse

    @GET("/authenticate")
    suspend fun authenticate()

    @GET("/note")
    suspend fun getAllUserNotes(): List<NoteResponse>

    @GET("/note/{id}")
    suspend fun getNoteById(@Path("id") id: String): NoteResponse

    @POST("/note")
    suspend fun insertNote(@Body request: NoteRequest)

    @PUT("/note/{id}")
    suspend fun updateNote(@Path("id") id: String, @Body request: NoteRequest)

    @DELETE("/note/{id}")
    suspend fun deleteNote(@Path("id") id: String)
}