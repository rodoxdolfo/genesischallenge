package genesischallenge.controller

import genesischallenge.domain.WalletPerformance
import genesischallenge.usecase.UpdateAssetPrice
import genesischallenge.usecase.WalletReader
import genesischallenge.usecase.calculatePerformance

class PerformanceController {
    suspend fun getWalletPerformance(filePath: String): WalletPerformance {
        val assets = WalletReader().readWallet(filePath)
        val updatedAssets = UpdateAssetPrice().getUpdatedAssetPrice(assets)
        return calculatePerformance(updatedAssets)
    }
}
