package genesischallenge.utils

import genesischallenge.domain.*

fun calculatePerformance(assets: List<Asset>): WalletPerformance {
    if (assets.isEmpty()) {
        throw IllegalArgumentException("Assets list must not be empty")
    }

    val updatedAssets = assets.map { asset ->
        val performance = (asset.updatedPrice / asset.priceUsd)
        asset.copy(updatedPrice = asset.updatedPrice, performance = performance, quantity = asset.quantity)
    }
    return WalletPerformance(
        updatedAssets.sumOf { it.quantity * it.updatedPrice },
        updatedAssets.maxBy { it.performance },
        updatedAssets.minBy { it.performance })
}