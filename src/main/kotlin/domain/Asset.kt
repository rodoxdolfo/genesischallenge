package genesischallenge.domain

import kotlinx.serialization.Serializable

@Serializable
data class Asset(
    val id: String = "",
    val symbol: String = "",
    val priceUsd: Double = 0.0,
    val performance: Double = 0.0,
    val quantity: Double = 0.0,
    val updatedPrice: Double = 0.0,
)
