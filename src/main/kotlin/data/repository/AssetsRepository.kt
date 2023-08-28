package genesischallenge.data.repository

import genesischallenge.data.api.AssetApiClient
import genesischallenge.domain.Price

interface AssetsRepository {
    suspend fun fetchAssetPrice(symbol: String): Price
}

class AssetsRepositoryImpl(private val apiClient: AssetApiClient = AssetApiClient()) : AssetsRepository {

    override suspend fun fetchAssetPrice(symbol: String) =
        fetchAsset(symbol)?.let { fetchPrice(it.id) } ?: error("Price not found for Asset $symbol")

    private suspend fun fetchAsset(symbol: String) = apiClient.fetchAsset(symbol).data.firstOrNull()

    private suspend fun fetchPrice(assetId: String) = apiClient.fetchPrice(assetId).data.firstOrNull()?.toDomain()
}
