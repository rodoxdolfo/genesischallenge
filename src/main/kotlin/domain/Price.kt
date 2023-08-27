package genesischallenge.domain

import kotlinx.serialization.Serializable

@Serializable
data class Price(
    val priceUsd: Double = 0.toDouble(),
)