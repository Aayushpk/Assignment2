package com.example.assignment1.data.model

import java.io.Serializable

data class Entity(
    val assetType: String,
    val ticker: String,
    val currentPrice: Double,
    val dividendYield: Double,
    val description: String
) : Serializable