package genesischallenge.Controller


import genesischallenge.domain.WalletPerformance
import genesischallenge.data.repository.*
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import genesischallenge.service.*
import kotlinx.coroutines.coroutineScope
import genesischallenge.utils.*


suspend fun getWalletPerformance(filePath: String): WalletPerformance = coroutineScope {
    val chunkSize = 3

    val assets = readWalletFile(filePath)
    val repository = AssetsRepository()

    val updatedAssets = assets.chunked(chunkSize).flatMap { group ->
        group.map { asset ->
            async {
                val updatedPrice = try {
                    repository.fetchAssetPrice(asset.symbol)?.priceUsd ?: 0.0
                }
                catch (e: Exception) {
                    println("An exception happened when getting '${asset.symbol}' : ${e.message}")
                    0.0
                }
                asset.copy(updatedPrice = updatedPrice)
            }
        }
    }.awaitAll()

    calculatePerformance(updatedAssets)
}
