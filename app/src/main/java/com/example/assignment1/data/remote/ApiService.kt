package com.example.assignment1.data.remote

import com.example.assignment1.data.model.DashboardResponse
import com.example.assignment1.data.model.LoginRequest
import com.example.assignment1.data.model.LoginResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path


interface ApiService {


    @POST("{campus}/auth")
    suspend fun login(
        @Path("campus") campus: String,
        @Body body: LoginRequest
    ): Response<LoginResponse>


    @GET("dashboard/{keypass}")
    suspend fun getDashboard(
        @Path("keypass") keypass: String
    ): Response<DashboardResponse>
}
