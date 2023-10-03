package com.myprojects.ktornotes.domain.use_case.auth

data class AuthUseCases(
    val authenticate: Authenticate,
    val signUp: SignUp,
    val signIn: SignIn,
    val signOut: SignOut
)
