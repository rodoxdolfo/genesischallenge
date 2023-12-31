package genesischallenge.usecase

import genesischallenge.domain.Asset
import genesischallenge.domain.WalletPerformance

fun calculatePerformance(assets: List<Asset>): WalletPerformance {
    if (assets.isEmpty()) {
        throw IllegalArgumentException("Assets list must not be empty")
    }

    val updatedAssets = assets.map { asset ->
        //measuring the performance of the asset between the original price and the updated price
        val performance = (asset.updatedPrice / asset.priceUsd)
        asset.copy(updatedPrice = asset.updatedPrice, performance = performance, quantity = asset.quantity)
    }
    return WalletPerformance(
        updatedAssets.sumOf { it.quantity * it.updatedPrice },
        updatedAssets.maxBy { it.performance },
        updatedAssets.minBy { it.performance })
}
