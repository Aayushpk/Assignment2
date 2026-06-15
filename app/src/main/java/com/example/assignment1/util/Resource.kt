package com.example.assignment1.util

/**
 * A generic wrapper used by the Repository to communicate the outcome
 * of a network call to the ViewModel WITHOUT leaking Retrofit/HTTP types
 * into the UI layer. Keeps the ViewModel framework-agnostic and testable.
 */
sealed class Resource<out T> {
    data class Success<T>(val data: T) : Resource<T>()
    data class Error(val message: String) : Resource<Nothing>()
}
