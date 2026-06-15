package com.example.assignment1.data.model

/**
 * Response from GET /dashboard/{keypass}.
 * Contains the list of entities plus the server-reported total count.
 */
data class DashboardResponse(
    val entities: List<Entity>,
    val entityTotal: Int
)
