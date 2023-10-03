package com.myprojects.ktornotes.di

import android.content.SharedPreferences
import com.myprojects.ktornotes.data.TokenManager
import com.myprojects.ktornotes.data.remote.AuthInterceptor
import com.myprojects.ktornotes.data.remote.ClientApi
import com.myprojects.ktornotes.data.repository.AuthRepositoryImpl
import com.myprojects.ktornotes.data.repository.NoteRepositoryImpl
import com.myprojects.ktornotes.domain.repository.AuthRepository
import com.myprojects.ktornotes.domain.repository.NoteRepository
import com.myprojects.ktornotes.domain.use_case.auth.AuthUseCases
import com.myprojects.ktornotes.domain.use_case.auth.Authenticate
import com.myprojects.ktornotes.domain.use_case.auth.SignIn
import com.myprojects.ktornotes.domain.use_case.auth.SignOut
import com.myprojects.ktornotes.domain.use_case.auth.SignUp
import com.myprojects.ktornotes.domain.use_case.notes.DeleteNote
import com.myprojects.ktornotes.domain.use_case.notes.GetAllUserNotes
import com.myprojects.ktornotes.domain.use_case.notes.GetNoteById
import com.myprojects.ktornotes.domain.use_case.notes.InsertNote
import com.myprojects.ktornotes.domain.use_case.notes.NoteUseCases
import com.myprojects.ktornotes.domain.use_case.notes.UpdateNote
import com.myprojects.ktornotes.util.Constants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideTokenManager(preferences: SharedPreferences): TokenManager {
        return TokenManager(preferences)
    }

    @Provides
    @Singleton
    fun provideAuthInterceptor(authManager: TokenManager): AuthInterceptor {
        return AuthInterceptor(authManager)
    }

    @Provides
    @Singleton
    fun provideInterceptor(authManager: TokenManager): Interceptor {
        return Interceptor {
            val request = it.request().newBuilder()
            val token = authManager.accessToken
            token?.let {
                request.addHeader("Authorization", "Bearer $token")
            }
            it.proceed(request.build())
        }
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(authInterceptor: AuthInterceptor): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(authInterceptor)
            .build()
    }

    @Provides
    @Singleton
    fun provideAuthApi(client: OkHttpClient): ClientApi {
        return Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create()
    }

    @Provides
    @Singleton
    fun provideAuthRepository(api: ClientApi, tokenManager: TokenManager): AuthRepository {
        return AuthRepositoryImpl(api, tokenManager)
    }

    @Provides
    @Singleton
    fun provideNoteRepository(api: ClientApi): NoteRepository {
        return NoteRepositoryImpl(api)
    }

    @Provides
    @Singleton
    fun provideAuthUseCases(
        authRepository: AuthRepository,
        tokenManager: TokenManager
    ): AuthUseCases {
        return AuthUseCases(
            authenticate = Authenticate(authRepository),
            signUp = SignUp(authRepository),
            signIn = SignIn(authRepository),
            signOut = SignOut(tokenManager)
        )
    }

    @Provides
    @Singleton
    fun provideNoteUseCases(
        noteRepository: NoteRepository
    ): NoteUseCases {
        return NoteUseCases(
            getAllUserNotes = GetAllUserNotes(noteRepository),
            getNoteById = GetNoteById(noteRepository),
            deleteNote = DeleteNote(noteRepository),
            insertNote = InsertNote(noteRepository),
            updateNote = UpdateNote(noteRepository)
        )
    }
}