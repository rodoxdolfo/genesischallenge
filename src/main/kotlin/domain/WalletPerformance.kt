package genesischallenge.domain

import kotlinx.serialization.Serializable

@Serializable
data class WalletPerformance(
    val totalValue: Double,
    val bestPerformingAsset: Asset,
    val worstPerformingAsset: Asset,
)