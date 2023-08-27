package genesischallenge.data.model

import genesischallenge.domain.Price
import kotlinx.serialization.Serializable

@Serializable
data class PriceResponse(
    val priceUsd: Double = 0.toDouble(),
) {
    fun toDomain() = Price(
        priceUsd = priceUsd
    )
}