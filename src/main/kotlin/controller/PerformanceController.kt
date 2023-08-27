package genesischallenge.controller


import genesischallenge.data.repository.AssetsRepository
import genesischallenge.data.repository.WalletRepository
import genesischallenge.domain.WalletPerformance
import genesischallenge.usecase.calculatePerformance
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class PerformanceController : KoinComponent {
    private val assetsRepository by inject<AssetsRepository>()
    private val walletRepository by inject<WalletRepository>()
    suspend fun getWalletPerformance(filePath: String): WalletPerformance = coroutineScope {
        val chunkSize = 3

        val assets = walletRepository.readWalletFile(filePath)


        val updatedAssets = assets.chunked(chunkSize).flatMap { group ->
            group.map { asset ->
                async {
                    val updatedPrice = try {
                        assetsRepository.fetchAssetPrice(asset.symbol).priceUsd
                    } catch (e: Exception) {
                        println("An exception happened when getting '${asset.symbol}' : ${e.message}")
                        0.0
                    }
                    asset.copy(updatedPrice = updatedPrice)
                }
            }
        }.awaitAll()

        calculatePerformance(updatedAssets)
    }
}
