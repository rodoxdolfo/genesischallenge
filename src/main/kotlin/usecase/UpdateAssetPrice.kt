package genesischallenge.usecase

import genesischallenge.data.repository.AssetsRepository
import genesischallenge.domain.Asset
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class UpdateAssetPrice : KoinComponent {
    private val assetsRepository by inject<AssetsRepository>()
    private val chunkSize = 3
     suspend fun getUpdatedAssetPrice(assets: List<Asset>) = coroutineScope {
        assets.chunked(chunkSize).flatMap { group ->
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
    }
}
