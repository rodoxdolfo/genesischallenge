package genesischallenge.data.repository

import genesischallenge.data.api.AssetApiClient

class AssetsRepository(private val apiClient: AssetApiClient = AssetApiClient()) {

    suspend fun fetchAssetPrice(symbol: String) = fetchAsset(symbol)?.let { fetchPrice(it.id) }

    private suspend fun fetchAsset(symbol: String) = apiClient.fetchAsset(symbol).data.firstOrNull()

    private suspend fun fetchPrice(assetId: String) = apiClient.fetchPrice(assetId).data.firstOrNull()?.toDomain()
}