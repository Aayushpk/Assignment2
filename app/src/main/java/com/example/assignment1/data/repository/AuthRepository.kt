package com.example.assignment1.data.repository

import com.example.assignment1.data.model.DashboardResponse
import com.example.assignment1.data.model.LoginRequest
import com.example.assignment1.data.model.LoginResponse
import com.example.assignment1.data.remote.ApiService
import com.example.assignment1.util.Resource
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Single source of truth for network data.
 *
 * - Depends on the ApiService INTERFACE (not a concrete Retrofit instance),
 *   which is what makes it trivial to mock in ViewModel unit tests.
 * - Converts Retrofit Response + exceptions into our own Resource type so
 *   the ViewModel never touches Retrofit/IOException directly.
 */
@Singleton
class AuthRepository @Inject constructor(
    private val api: ApiService
) {

    suspend fun login(
        campus: String,
        username: String,
        password: String
    ): Resource<LoginResponse> = safeCall {
        api.login(campus, LoginRequest(username, password))
    }

    suspend fun getDashboard(keypass: String): Resource<DashboardResponse> = safeCall {
        api.getDashboard(keypass)
    }

    /**
     * Wraps a Retrofit call: maps a successful body to Resource.Success,
     * an HTTP error (e.g. wrong password -> 401) to a friendly message,
     * and any thrown exception (no network, timeout) to Resource.Error.
     */
    private inline fun <T> safeCall(block: () -> retrofit2.Response<T>): Resource<T> {
        return try {
            val response = block()
            val body = response.body()
            if (response.isSuccessful && body != null) {
                Resource.Success(body)
            } else if (response.code() == 401 || response.code() == 400) {
                Resource.Error("Invalid username or password")
            } else {
                Resource.Error("Server error (${response.code()}). Please try again.")
            }
        } catch (e: Exception) {
            Resource.Error("Network error: ${e.localizedMessage ?: "unknown"}")
        }
    }
}
